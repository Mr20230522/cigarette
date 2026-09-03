# =============================================================================
# call.py - 烟草运输车辆预警模型服务
# 功能：加载训练好的孤立森林模型，提供 HTTP API 进行单条过车记录预警
# 接口：
#   GET  /health   - 健康检查
#   POST /predict  - 预测，接收原始过车记录 JSON，返回嫌疑度及风险因子
# =============================================================================

import os
import json
import warnings
import pandas as pd
import numpy as np
import joblib
from flask import Flask, request, jsonify

warnings.filterwarnings('ignore')

app = Flask(__name__)

# ------------------------------ 1. 加载模型包 ------------------------------
MODEL_DIR = os.path.dirname(os.path.abspath(__file__))
MODEL_PATH = os.path.join(MODEL_DIR, 'tobacco_warning_model.pkl')

print(f"正在加载模型文件: {MODEL_PATH}")
model_package = joblib.load(MODEL_PATH)

iso_model = model_package['iso_model']
scaler = model_package['scaler']
score_scaler = model_package['score_scaler']
FEATURES = model_package['features']
explainer = model_package['explainer']
weights = model_package['feature_weights']   # 特征权重数组，顺序与 FEATURES 一致

print(f"特征列表: {FEATURES}")
print(f"特征权重: {weights}")

# ------------------------------ 2. 辅助函数 ------------------------------
def iso_score_to_suspect(decision_score):
    """将孤立森林的决策分数映射为 0~1 的嫌疑度（越大越可疑）"""
    return float(score_scaler.transform([[-decision_score]])[0][0])

# 车牌省份映射（与训练代码一致）
PLATE_PROVINCE = {
    '京': '北京', '津': '天津', '沪': '上海', '渝': '重庆',
    '冀': '河北', '晋': '山西', '辽': '辽宁', '吉': '吉林', '黑': '黑龙江',
    '苏': '江苏', '浙': '浙江', '皖': '安徽', '闽': '福建', '赣': '江西',
    '鲁': '山东', '豫': '河南', '鄂': '湖北', '湘': '湖南', '粤': '广东',
    '桂': '广西', '琼': '海南', '川': '四川', '贵': '贵州', '云': '云南',
    '藏': '西藏', '陕': '陕西', '甘': '甘肃', '青': '青海', '宁': '宁夏',
    '新': '新疆', '蒙': '内蒙古'
}

MONITOR_PROVINCE = '云南'
MONITOR_CITY_CODE = '云D'

# 车型风险评分（与训练代码一致）
RISK_SCORE_MAP = {
    "面包车": 1, "SUV/MPV": 1, "SUV": 1, "小货车": 1,
    "货车": 0, "皮卡车": 0, "大型客车": 0, "中型客车": 0,
    "小型客车": 0, "轿车": 0, "二轮车": 0, "挂车": 0, "三轮车": 0,
    "unknown": 0
}

# 高风险品牌
HIGH_RISK_BRANDS = ['五菱', '东风', '金杯', '江铃', '福特', '依维柯', '福田']

def get_province(plate):
    """从车牌号获取省份"""
    if not isinstance(plate, str) or len(plate) < 1:
        return '未知'
    return PLATE_PROVINCE.get(plate[0], '其他')

def feature_engineering(record):
    """
    从原始过车记录字典构建特征数组（顺序与 FEATURES 一致）。
    record 需包含字段：Plate, CaptureTime, VehicleType, VehicleLogo
    """
    plate = record.get('Plate', '')
    capture_time_str = record.get('CaptureTime', '')
    vehicle_type = record.get('VehicleType', '')
    vehicle_logo = record.get('VehicleLogo', '')

    # 解析时间
    try:
        capture_time = pd.to_datetime(capture_time_str)
    except:
        capture_time = pd.Timestamp.now()   # 解析失败使用当前时间，实际项目中可另行处理

    hour = capture_time.hour
    weekday = capture_time.weekday()
    month = capture_time.month

    is_weekend = 1 if weekday >= 5 else 0
    is_night = 1 if hour in [22, 23, 0, 1, 2, 3, 4, 5] else 0
    is_dead_night = 1 if 2 <= hour <= 5 else 0
    is_q4 = 1 if month in [9, 10, 11, 12] else 0

    # 车牌归属
    plate_province = get_province(plate)
    plate_city_code = plate[:2] if isinstance(plate, str) and len(plate) >= 2 else ''
    is_foreign = 1 if plate_province != MONITOR_PROVINCE else 0
    is_q4_foreign = 1 if (is_q4 == 1 and is_foreign == 1) else 0
    is_nonq4_province_notcity = 1 if (is_q4 == 0 and plate_province == MONITOR_PROVINCE and plate_city_code != MONITOR_CITY_CODE) else 0

    # 车型风险
    vehicleType_risk_score = RISK_SCORE_MAP.get(vehicle_type, 0)

    # 品牌风险
    is_high_risk_brand = 1 if any(brand in vehicle_logo for brand in HIGH_RISK_BRANDS) else 0

    # 按 FEATURES 顺序组装特征数组
    features = [
        is_weekend,
        is_night,
        is_dead_night,
        is_q4,
        is_q4_foreign,
        is_nonq4_province_notcity,
        vehicleType_risk_score,
        is_high_risk_brand
    ]
    return features

# ------------------------------ 3. 预测逻辑 ------------------------------
def predict_record(record, top_k=5):
    """
    接收原始记录字典，返回预测结果。
    也支持直接传入特征数组/字典（若 record 中包含 'features' 字段则优先使用）。
    """
    # 如果直接提供了特征值，则跳过特征工程
    if 'features' in record:
        feat = record['features']
        if isinstance(feat, dict):
            x_raw = np.array([[feat.get(f, 0) for f in FEATURES]])
        else:
            x_raw = np.array(feat).reshape(1, -1)
    else:
        # 从原始记录构建特征
        feat_values = feature_engineering(record)
        x_raw = np.array([feat_values])

    # 标准化 + 权重
    x_scaled = scaler.transform(x_raw) * weights

    # 孤立森林决策分数
    iso_score = iso_model.decision_function(x_scaled)[0]
    suspect = iso_score_to_suspect(iso_score)

    # SHAP 解释
    shap_vals = explainer.shap_values(x_scaled)
    if isinstance(shap_vals, list):
        shap_vals = shap_vals[0] if len(shap_vals) == 1 else shap_vals[1]
    elif isinstance(shap_vals, np.ndarray) and shap_vals.ndim == 3:
        shap_vals = shap_vals[0] if shap_vals.shape[0] == 1 else shap_vals[1]
    shap_vals = np.array(shap_vals).reshape(1, -1)
    contributions = shap_vals[0].flatten()

    # 构建特征贡献列表
    top_reasons = []
    for i, fname in enumerate(FEATURES):
        top_reasons.append({
            'feature': fname,
            'value': round(float(x_raw[0, i]), 4),
            'shap_value': round(float(contributions[i]), 6)
        })
    top_reasons.sort(key=lambda x: abs(x['shap_value']), reverse=True)
    top_reasons = top_reasons[:top_k]

    return {
        'suspect_degree': round(suspect, 4),
        'is_high_risk': bool(suspect > 0.8),
        'top_reasons': top_reasons,
        'all_features': {f: round(float(x_raw[0, i]), 4) for i, f in enumerate(FEATURES)}
    }

# ------------------------------ 4. Flask 路由 ------------------------------
@app.route('/health', methods=['GET'])
def health():
    return jsonify({'status': 'ok'})

@app.route('/predict', methods=['POST'])
def predict():
    try:
        data = request.get_json(force=True)
        if not data:
            return jsonify({'error': '请求体不能为空'}), 400
        result = predict_record(data, top_k=5)
        return jsonify(result)
    except Exception as e:
        return jsonify({'error': str(e)}), 500

# ------------------------------ 5. 启动服务 ------------------------------
if __name__ == '__main__':
    # 监听所有网络接口，端口 5000（与 Java 配置一致）
    app.run(host='0.0.0.0', port=5000, debug=False)