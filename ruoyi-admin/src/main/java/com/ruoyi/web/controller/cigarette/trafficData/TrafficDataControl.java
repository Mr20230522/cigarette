package com.ruoyi.web.controller.cigarette.trafficData;

import com.github.pagehelper.PageHelper;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.TrafficData;
import com.ruoyi.system.domain.undefine.SearchOfTheSuspectedVehicle;
import com.ruoyi.system.domain.vo.CameraIdNameVO;
import com.ruoyi.system.service.ITrafficDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cigarette/trafficData")
public class TrafficDataControl extends BaseController {

    @Autowired
    private ITrafficDataService TrafficDataService;

    /**
     * 查询车辆表前十条
     */
    @GetMapping("/tenList")
    public List<TrafficData> tenList(TrafficData trafficData){
        return TrafficDataService.tenList(trafficData);
    }


    /**
     * 查询车辆表前十条
     */
    @GetMapping("/allList")
    public List<TrafficData> allList( TrafficData trafficData){
        return TrafficDataService.allList(trafficData);
    }


    /**
     * 查询车辆表前十条
     */
    @GetMapping("/overIdList")
    public List<TrafficData> overIdList(TrafficData trafficData){
        return TrafficDataService.overIdList(trafficData);
    }


    /**
     * 通过id获取视屏地址
     */
    @GetMapping("/byIdGetVideoPath")
    public List<TrafficData> byIdGetVideoPath(TrafficData trafficData){
        return TrafficDataService.byIdGetVideoPath(trafficData);
    }

    /**
     * 通过嫌疑查询条件获取视频地址（自带分页）
     * 前端仍用 GET /cigarette/trafficData/searchVehicle
     */
    @GetMapping("/searchVehicle")
    public TableDataInfo searchVehicle(SearchOfTheSuspectedVehicle search){
        // 如果前端没传分页参数，默认第1页，每页4条
        int pageNum  = search.getPageNum()  == null ? 1 : search.getPageNum();
        int pageSize = search.getPageSize() == null ? 7 : search.getPageSize();
        PageHelper.startPage(pageNum, pageSize);

        List<TrafficData> list = TrafficDataService.searchVehicle(search);
        return getDataTable(list);   // RuoYi 自带，返回 {total, rows}
    }

    /**
     * 获取所有摄像头ID和名称的配对数据
     */
    @GetMapping("/allCameraIdNamePairs")
    public List<CameraIdNameVO> getAllCameraIdNamePairs() {
        return TrafficDataService.getAllCameraIdNamePairs();
    }
}
