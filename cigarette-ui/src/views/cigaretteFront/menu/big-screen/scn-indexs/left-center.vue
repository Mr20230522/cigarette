<template>
  <div class="container">
    <div class="left-content">
      <!-- 左侧预警类型选择 -->
      <div
        v-for="type in earlyWarningTypes"
        :key="type.value"
        class="channel-item"
        :class="{ active: activeType === type.value }"
        @click="selectType(type.value)"
      >
        {{ type.name }}
      </div>
    </div>
    <div class="right-content">
      <!-- 右侧图表展示 -->
      <div class="chart-container">
        <Echart
          id="leftCenter"
          :options="options"
          class="left_center_inner"
          v-if="pageflag"
          ref="charts"
        />
        <Reacquire v-else @onclick="getData" style="line-height:200px">
          重新获取
        </Reacquire>
      </div>
    </div>
  </div>
</template>

<script>
import Reacquire from "@/components/scn-reacquire/reacquire.vue";
import Echart from "@/components/scn-echart/index.vue"
import {listVehicleBehaviorVo} from '@/api/cigarette/vehicle/vehicleBehavior'
import {listDetection} from  '@/api/cigarette/detection/detection'
import detection from "@/views/cigarette/detection/detection/index.vue";

export default {
  components: {
    Reacquire,
    Echart
  },
  data() {
    return {
      options: {},
      // 预警类型数据

      earlyWarningTypes: [
        { name: '季节', value: 'quarter' },
        { name: '昼夜', value: 'dayAndNight' },
        { name: '小时', value: 'hours' },
        { name: '车辆类型', value: 'vehicleType' },
        { name: '车辆颜色', value: 'vehicleColor' },
        { name: '站点出现频率', value: 'detection' },
        { name: '操作行为预警', value: 'operation' },
        { name: '综合预警分析', value: 'comprehensive' }
      ],
      activeType: 'quarter', // 当前选中的预警类型
      // 模拟数据
      warningData: {
        quarter: {
          total: 0,
          categories: [
          ]
        },
        dayAndNight: {
          total: 0,
          categories: []
        },
        hours: {
          total: 0,
          categories: [

          ]
        },
        vehicleType: {
          total: 0,
          categories: []
        },
        vehicleColor: {
          total: 0,
          categories: []
        },
        detection: {
          total: 0,
          categories: []
        },
        operation: {
          total: 134,
          categories: [
            { name: "违规操作", value: 45, color: "#FF6B6B" },
            { name: "高风险操作", value: 67, color: "#ECA444" },
            { name: "异常登录", value: 22, color: "#33A1DB" }
          ]
        },
        comprehensive: {
          total: 387,
          categories: [
            { name: "一级预警", value: 56, color: "#FF6B6B" },
            { name: "二级预警", value: 134, color: "#ECA444" },
            { name: "三级预警", value: 197, color: "#33A1DB" }
          ]
        }
      },
      pageflag: true,
      timer: null,
      // 查询参数
      queryParams: {
        pageNum: 1,
        pageSize: 1000,
        carId: null,
        carTypeId: null,
        carColor: null,
        degreeSuspicion: null,
        driverId: null,
        drivingDirection: null,
        illegalStatus: null,
        status: null,
        accompliceId: null
      },
    };
  },
  created() {
    this.getData();
    this.getList();
  },
  mounted() {
    // this.switper();
  },
  beforeDestroy() {
    this.clearData();
  },
  methods: {
    clearData() {
      if (this.timer) {
        clearInterval(this.timer);
        this.timer = null;
      }
    },
    // 模拟API请求
    getData() {
      this.pageflag = true;
      // 模拟API请求延迟
      setTimeout(() => {
        this.initChart();
      }, 500);
    },
    // 选择预警类型
    selectType(type) {
      this.activeType = type;
      this.getList()
      this.warningData[type].total=0;
      this.warningData[type].categories=[]
    },
    // 轮询更新数据
    switper() {
      if (this.timer) {
        return;
      }
      let looper = () => {
        // 模拟数据变化
        this.randomizeData();
        this.initChart();
      };
      this.timer = setInterval(looper, 10000); // 10秒更新一次

      // 鼠标悬停时停止轮询
      let myChart = this.$refs.charts?.chart;
      if (myChart) {
        myChart.on('mouseover', () => {
          this.clearData();
        });
        myChart.on('mouseout', () => {
          this.timer = setInterval(looper, 10000);
        });
      }
    },
    // 随机变化数据，模拟实时更新
    randomizeData() {
      Object.keys(this.warningData).forEach(type => {
        const data = this.warningData[type];
        // 总数随机变化±5%
        const totalChange = Math.floor(data.total * (0.95 + Math.random() * 0.1));
        data.total = Math.max(50, totalChange); // 确保不小于50

        // 各类别数据随机变化
        data.categories.forEach(category => {
          const change = Math.floor(category.value * (0.9 + Math.random() * 0.2));
          category.value = Math.max(5, change); // 确保不小于5
        });

        // 调整总和与总数一致
        const sum = data.categories.reduce((acc, cur) => acc + cur.value, 0);
        const diff = data.total - sum;
        if (diff !== 0) {
          // 将差值随机分配到某个类别
          const randomIndex = Math.floor(Math.random() * data.categories.length);
          data.categories[randomIndex].value += diff;
          // 确保调整后不小于5
          data.categories[randomIndex].value = Math.max(5, data.categories[randomIndex].value);
        }
      });
    },
    // 初始化图表
    initChart() {
      const currentData = this.warningData[this.activeType];
      const colors = currentData.categories.map(item => item.color);

      const piedata = {
        name: "预警分布",
        type: "pie",
        radius: ["42%", "65%"],
        avoidLabelOverlap: false,
        itemStyle: {
          borderRadius: 4,
          borderColor: "rgba(0,0,0,0)",
          borderWidth: 2,
        },
        color: colors,
        data: currentData.categories.map(item => ({
          value: item.value,
          name: item.name,
          label: {
            shadowColor: item.color,
          },
        })),
      };

      this.options = {
        title: {
          text: ["{value|" + currentData.total + "}", "{name|预警总数}"].join("\n"),
          top: "center",
          left: "center",
          textStyle: {
            rich: {
              value: {
                color: "#ffffff",
                fontSize: 24,
                fontWeight: "bold",
                lineHeight: 20,
              },
              name: {
                color: "#ffffff",
                lineHeight: 20,
              },
            },
          },
        },
        tooltip: {
          trigger: "item",
          backgroundColor: "rgba(0,0,0,.6)",
          borderColor: "rgba(147, 235, 248, .8)",
          textStyle: {
            color: "#FFF",
          },
          formatter: params => {
            const percent = Math.round(params.percent);
            return `
              <div style="font-size:14px;font-weight:bold;margin-bottom:5px;">${params.name}</div>
              <div>数量: ${params.value}个</div>
              <div>占比: ${percent}%</div>
            `;
          }
        },
        legend: {
          show: false,
          orient: 'vertical',
          right: '5%',
          top: 'center',
          textStyle: {
            color: '#fff',
            fontSize: 12
          },
          formatter: name => {
            const item = currentData.categories.find(item => item.name === name);
            const value = item ? item.value : 0;
            const percent = Math.round((value / currentData.total) * 100);
            return `${name}  ${value} (${percent}%)`;
          }
        },
        series: [
          {
            ...piedata,
            tooltip: { show: true },
            label: {
              // formatter: "   {b|{b}}   \n   {c|{c}个}   {per|{d}%}  ",
              rich: {
                b: {
                  color: "#fff",
                  fontSize: 12,
                  lineHeight: 26,
                },
                c: {
                  color: "#31ABE3",
                  fontSize: 14,
                },
                per: {
                  color: "#31ABE3",
                  fontSize: 14,
                },
              },
            },
            labelLine: {
              // length: 20,
              // length2: 36,
              show: false,
            },
            emphasis: {
              itemStyle: {
                shadowBlur: 10,
                shadowOffsetX: 0,
                shadowColor: 'rgba(0, 0, 0, 0.5)'
              }
            },
          },
          {
            ...piedata,
            tooltip: { show: false },
            itemStyle: {},
            label: {
              backgroundColor: "auto",
              height: 0,
              width: 0,
              lineHeight: 0,
              borderRadius: 2.5,
              shadowBlur: 8,
              shadowColor: "auto",
              padding: [2.5, -2.5, 2.5, -2.5],
            },
            labelLine: {
              show: false,
            },
          },
        ],
      };
    },
    getList(){
      listVehicleBehaviorVo(this.queryParams).then(response=>{
        if(response.code===200&&response.rows){
          this.processData(response.rows)
          switch(this.activeType){
            case 'quarter':
              this.processData(response.rows)
              break
            case 'dayAndNight':
              this.processDataDayAndData(response.rows)
              break
            case 'hours':
              this.processDataHours(response.rows)
              break
            case 'vehicleType':
              this.processDataVehicleType(response.rows)
              break
            case 'vehicleColor':
              this.processDataVehicleColor(response.rows)
              break
            case 'detection':
              this.processDataDetection(response.rows)
              break
            default:
              console.log('还没有实现');
          }
          this.initChart();

        }else{
          console.error("数据格式异常:", error);
        }
      }).catch(error=>{
        console.error("数据格式异常:", error);
      })
    },
    processData(range){
      let spring=0
      let summer=0
      let autumn=0
      let winter=0

      range.forEach(item => {
        if (!item.createTime) return;
        const month = new Date(item.createTime).getMonth() + 1;
        if(month>=1&&month<=3){
          spring++
        }else if(month>=4&&month<=6){
          summer++
        }else if(month>=7&&month<=9){
          autumn++
        }else if(month>=10&&month<=12){
          winter++
        }
      })
      this.warningData.quarter.total=spring+summer+autumn+winter
      this.warningData.quarter.categories=[{
        name:'spring',
        value:spring,
        color:'#FF6B6B'
      },{
        name:'summer',
        value:summer,
        color:'#ECA444'
      },{
        name:'autumn',
        value:autumn,
        color:'#33A1DB'
      },{
        name:'winter',
        value:winter,
        color:'#60ff00'
      }
      ]
    },
    processDataDayAndData(range){
      let day=0
      let night=0
      range.forEach(item=>{
        if(!item.createTime) return;
        let hour = new Date(item.createTime).getHours() + 1;
        if(hour<18){
          day++
        }else{
          night++
        }
      })
      this.warningData.dayAndNight.total=day+night
      this.warningData.dayAndNight.categories=[{
        name:'day',
        value:day,
        color:'#ff0000'
      },{
        name:'night',
        value:night,
        color:'#62ff00'
      },
      ]
    },
    processDataHours(range){
      const hours=Array(24).fill(0)
      range.forEach(item=>{
        if(!item.createTime) return;
        const hour=new Date(item.createTime).getHours()+1
        hours[hour]++
      })
      hours.forEach((value,index)=>{
        if(value!==0){
          this.warningData.hours.categories.push({
          name:`${index}:00`,
          value:value,
          // color:index%2===0? '#ff0000':'#62ff00'
        })
          this.warningData.hours.total+=value
        }
      })
      this.warningData.hours.categories.forEach((value,index)=>{
        value.color=index%2===0? '#ff0000':'#62ff00'
      })
      // this.warningData.hours.categories=hours.map((value,index)=>{
      //   return{
      //     name:index.toString(),
      //     value:value,
      //     color:index/2===0? '#ff0000':'#62ff00'
      //   }
      // })
    },
    processDataVehicleType(range){
      const vehicleTypeLists=range.reduce((acc,{carTypeId})=>{
        acc[carTypeId]=(acc[carTypeId]||0)+1
        return acc
      },{})
      //这里做了生序处理
      const vehicleTypeList=Object.entries(vehicleTypeLists).sort(([,a],[,b])=>a-b).reduce((acc,[key,val])=>{
        acc[key]=val
        return acc
      },{})

      //这里可以写给那几个生序降序
      //
      //
      Object.keys(vehicleTypeList).forEach((key,index)=>{
        this.warningData.vehicleType.categories.push({
          name:key,
          value:vehicleTypeList[key],
          color:index%2===0? '#ff0000':'#62ff00'
        })
        this.warningData.vehicleType.total+=vehicleTypeList[key]
      })
    },
    processDataVehicleColor(range){
      const vehicleColorList=range.reduce((arr,range)=>{
        if(range.carColor){
          arr[range.carColor]=(arr[range.carColor]||0)+1
        }
        return arr
      },{})
      Object.keys(vehicleColorList).forEach((key,index)=>{
        this.warningData.vehicleColor.categories.push({
          name:key,
          value:vehicleColorList[key],
          color:index%2===0? '#ff0000':'#62ff00'
        })
        this.warningData.vehicleColor.total+=vehicleColorList[key]
      })
    },
    async processDataDetection(range){
      const DetectionIdList = range.reduce((arr,{detectionId})=>{
        arr[detectionId]=(arr[detectionId]||0)+1
        return arr
      },{})
      let DetectionNameList=[]
      try{
        const response=await listDetection({})
        DetectionNameList = response.rows.map(item => ({
          [item.detectionName]: item.detectionId
        }));
      }catch{
        console.error('请求失败:', error);
        throw error;
      }
      const mergeArray=DetectionNameList.map(item=>{
        const nameKey=Object.keys(item)[0]
        const nameValue=item[nameKey]
        if(DetectionIdList[nameValue]!=null){
           const idValue=DetectionIdList[nameValue]
        return{[nameKey]:idValue}
        }
        return null
      }).filter(Boolean);
      console.log('mergeArray',mergeArray)
      mergeArray.forEach((obj,index)=>{
        this.warningData.detection.categories.push({
          name:Object.keys(obj)[0],
          value:obj[Object.keys(obj)[0]],
          color:index%2===0? '#ff0000':'#62ff00'
        })
        this.warningData.detection.total+=obj[Object.keys(obj)[0]]
      })
      this.initChart()
    }
  },
};
</script>

<style lang="scss" scoped>
.container {
  display: flex;
  height: 100%;
  width: 100%;
  background: rgba(13, 42, 67, 0.5);
  border-radius: 8px;
  box-shadow: 0 0 15px rgba(0, 114, 255, 0.2);
}

.left-content {
  width: 25%;
  padding: 15px 10px;
  display: flex;
  flex-direction: column;
  gap: 12px;
  max-height: 80%;
  overflow-y: auto;
  overflow-x: hidden;
  border-right: 1px solid rgba(0, 114, 255, 0.3);
}

.right-content {
  width: 75%;
  display: flex;
  justify-content: center;
  align-items: center;
  padding: 10px;
}

.chart-container {
  width: 100%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.left_center_inner {
  width: 100%;
  height: 100%;
  min-height: 300px;
}

.channel-item {
  text-align: center;
  padding: 10px 0;
  background: rgba(0, 114, 255, 0.1);
  border: 1px solid rgba(0, 114, 255, 0.3);
  border-radius: 6px;
  color: #e0e0e0;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 14px;
  font-weight: 500;

  &:hover {
    background: rgba(0, 114, 255, 0.3);
    transform: translateX(5px);
    box-shadow: 0 0 10px rgba(0, 114, 255, 0.3);
  }

  &.active {
    background: rgba(0, 114, 255, 0.5);
    border-color: rgba(0, 114, 255, 0.8);
    box-shadow: 0 0 15px rgba(0, 114, 255, 0.5);
    color: #fff;
    font-weight: bold;
  }
}

/* 滚动条样式 */
.left-content::-webkit-scrollbar {
  width: 4px;
}

.left-content::-webkit-scrollbar-thumb {
  background: rgba(0, 114, 255, 0.5);
  border-radius: 4px;
}

.left-content::-webkit-scrollbar-track {
  background: transparent;
}
</style>
