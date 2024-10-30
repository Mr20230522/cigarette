<template>
  <div id="map" class="map">
    <div class="demo-title">
      <h1>北京公交线路行驶路线分类</h1>
      <h3>北京全市范围内各公交线路运行情况</h3>
    </div>
  </div>
</template>

<script>
export default {
  name: 'BusMap',
  mounted() {
    this.initMap();
  },
  methods: {
    initMap() {
      // 请确保你已经替换为你的高德地图开发者Key
      var script = document.createElement('script');
      script.src = 'https://webapi.amap.com/maps?v=2.0&key=4cb4e39b657f9849e3b39e14fc41c2b6&plugin=AMap.Scale,AMap.ToolBar';
      document.head.appendChild(script);

      script.onload = () => {
        var map = new AMap.Map('map', {
          zoom: 11.2,
          center: [116.352734, 39.8447],
          showLabel: false,
          viewMode: '3D',
          mapStyle: 'amap://styles/dark',
          pitch: 50,
        });

        var locaScript = document.createElement('script');
        locaScript.src = 'https://webapi.amap.com/loca?v=2.0.0&key=4cb4e39b657f9849e3b39e14fc41c2b6';
        document.head.appendChild(locaScript);

        locaScript.onload = () => {
          var loca = new Loca.Container({
            map,
          });
          window._loca = loca;

          var geo = new Loca.GeoJSONSource({
            url: 'https://a.amap.com/Loca/static/loca-v2/demos/mock_data/bj_bus.json',
          });

          var layer = new Loca.PulseLineLayer({
            zIndex: 10,
            opacity: 1,
            visible: true,
            zooms: [2, 22],
          });

          var headColors = ['#EFBB51', '#7F3CFF', '#4CC19B', '#0B5D74', '#E06AC4', '#223F9B', '#F15C1A', '#7A0FA6'];

          layer.setSource(geo);
          layer.setStyle({
            altitude: 0,
            lineWidth: 2,
            headColor: (_, feature) => {
              return headColors[feature.properties.type - 1];
            },
            trailColor: 'rgba(128, 128, 128, 0.5)',
            interval: 0.25,
            duration: 5000,
          });
          loca.add(layer);
          loca.animate.start();

          var legend = new Loca.Legend({
            loca: loca,
            title: {
              label: '公交类型',
              fontColor: 'rgba(255,255,255,0.4)',
              fontSize: '16px'
            },
            style: {
              backgroundColor: 'rgba(255,255,255,0.1)',
              left: '20px',
              bottom: '40px',
              fontSize: '12px'
            },
            dataMap: [
              { label: 'A类型', color: headColors[7] },
              { label: 'B类型', color: headColors[6] },
              { label: 'C类型', color: headColors[5] },
              { label: 'D类型', color: headColors[4] },
              { label: 'E类型', color: headColors[3] },
              { label: 'F类型', color: headColors[2] },
              { label: 'G类型', color: headColors[1] },
              { label: 'H类型', color: headColors[0] },
            ],
          });

          // loca.animate.start();
          // var dat = new Loca.Dat();
          // dat.addLayer(layer, '公交');
        };
      };
    }
  }
};
</script>

<style scoped>
html,
body,
#map {
  width: 100%;
  height: 100%;
  margin: 0;
  padding: 0;
}

.demo-title {
  position: absolute;
  top: 25px;
  left: 25px;
  z-index: 1;
}

h1 {
  font-size: 18px;
  margin: 0;
  color: rgb(180, 180, 190);
}

h3 {
  font-size: 12px;
  font-weight: normal;
  margin-top: 5px;
  color: rgb(150, 150, 150);
}
</style>