package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

@Data
public class ToVehicleRealTimMonitoring extends BaseEntity {
    // 主键ID
    private Long id;

    // 车牌号码
    private String plateNumber;

    // 车牌颜色（如 蓝色、黄色、绿色等）
    private String plateColor;

    // 车牌类型（如 民用、军警、临时等）
    private String plateType;

    // 车身颜色（如 白色、黑色等）
    private String carColor;

    // 车辆类型（如 小型轿车、大货车、摩托车等）
    private String vehicleType;

    // 品牌型号（如 丰田卡罗拉、特斯拉Model 3）
    private String brand;

    // 行驶方向（如 东向西、南向北）
    private String direction;

    // 通道编号（摄像头或道路通道编号）
    private String channelId;

    // 授权类型（如 白名单车辆、临时放行等）
    private String authorizationType;

    // 抓拍时间，格式为 yyyy-MM-dd HH:mm:ss
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date captureTime;

    // 车速，单位：km/h，支持小数
    private BigDecimal speed;

    // 所在车道号（如 1车道、2车道）
    private String laneNumber;

    // 数据类型（如 实时数据、历史数据、违法数据等）
    private String dataType;

    // 违法类型（如 闯红灯、不系安全带、打电话等）
    private String violationType;

    // 驾驶员是否系安全带（true=系，false=未系）
    private Boolean driverSeatbelt;

    // 副驾驶是否系安全带
    private Boolean passengerSeatbelt;

    // 驾驶员是否戴墨镜
    private Boolean driverSunglass;

    // 副驾驶是否戴墨镜
    private Boolean passengerSunglass;

    // 驾驶员是否正在通话
    private Boolean phoneCall;

    // 是否有人从天窗探出身体
    private Boolean sunroofPerson;

    // 是否为黄标车
    private Boolean yellowLabelCar;

    // 是否为危险品运输车
    private Boolean hazardousMaterials;

    // 是否有挂件（挡风玻璃挂件）
    private Boolean pendant;

    // 车辆轴数（如 2轴、3轴、4轴）
    private Byte axleCount;

    // 轴型（如 单桥、双桥、三桥）
    private String axleType;

    // 车辆详细型号
    private String detailedModel;

    // 视频文件访问 URL（可用于预览或下载）
    private String videoUrl;

    // 车牌图像 URL
    private String plateImageUrl;

    // 全车图像 URL
    private String imageUrl;

}
