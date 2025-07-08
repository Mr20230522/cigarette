package com.ruoyi.web.controller.cigarette.detection;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.TobDistrict;
import com.ruoyi.system.service.ITobDistrictService;
import com.ruoyi.common.utils.poi.ExcelUtil;

/**
 * 地区管理Controller
 *
 * @author ruoyi
 * @date 2024-07-24
 */
@RestController
@RequestMapping("/cigarette/detection/district")
public class TobDistrictController extends BaseController
{
    @Autowired
    private ITobDistrictService tobDistrictService;

    /**
     * 查询地区管理列表
     */
    @PreAuthorize("@ss.hasPermi('cigarette:district:list')")
    @GetMapping("/list")
    public AjaxResult list(TobDistrict tobDistrict)
    {
        List<TobDistrict> list = tobDistrictService.selectTobDistrictList(tobDistrict);
        return success(list);
    }

    /**
     * 导出地区管理列表
     */
    @PreAuthorize("@ss.hasPermi('cigarette:district:export')")
    @Log(title = "地区管理", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TobDistrict tobDistrict)
    {
        List<TobDistrict> list = tobDistrictService.selectTobDistrictList(tobDistrict);
        ExcelUtil<TobDistrict> util = new ExcelUtil<TobDistrict>(TobDistrict.class);
        util.exportExcel(response, list, "地区管理数据");
    }

    /**
     * 获取地区管理详细信息
     */
    @PreAuthorize("@ss.hasPermi('cigarette:district:query')")
    @GetMapping(value = "/{districtId}")
    public AjaxResult getInfo(@PathVariable("districtId") Long districtId)
    {
        return success(tobDistrictService.selectTobDistrictByDistrictId(districtId));
    }

    /**
     * 新增地区管理
     */
    @PreAuthorize("@ss.hasPermi('cigarette:district:add')")
    @Log(title = "地区管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TobDistrict tobDistrict)
    {
        return toAjax(tobDistrictService.insertTobDistrict(tobDistrict));
    }

    /**
     * 修改地区管理
     */
    @PreAuthorize("@ss.hasPermi('cigarette:district:edit')")
    @Log(title = "地区管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TobDistrict tobDistrict)
    {
        return toAjax(tobDistrictService.updateTobDistrict(tobDistrict));
    }

    /**
     * 删除地区管理
     */
    @PreAuthorize("@ss.hasPermi('cigarette:district:remove')")
    @Log(title = "地区管理", businessType = BusinessType.DELETE)
	@DeleteMapping("/{districtIds}")
    public AjaxResult remove(@PathVariable Long[] districtIds)
    {
        return toAjax(tobDistrictService.deleteTobDistrictByDistrictIds(districtIds));
    }

    /**
     * 获取地区树列表
     */
    @PreAuthorize("@ss.hasPermi('cigarette:district:list')")
    @GetMapping("/districtTree")
    public AjaxResult districtTree(TobDistrict district )
    {
        return success(tobDistrictService.selectTobDistrictTreeList(district));
    }

    @PreAuthorize("@ss.hasPermi('cigarette:district:list')")
    @GetMapping("/getDistrictId") //小地址
    public AjaxResult getDistrictId(String districtName){
        System.out.println("@@@districtName@@@"+districtName);
        Long districtId=tobDistrictService.getDistrictId(districtName);
        System.out.println("###districtId###"+districtId);
        if(districtId==null){
            return error();
        }
        return success(districtId);
    }

}
