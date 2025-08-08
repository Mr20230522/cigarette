package com.ruoyi.web.controller.cigarette.caution.factor;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.Factor;
import com.ruoyi.system.service.IFactorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/factor")
public class FactorController extends BaseController {

    @Autowired
    private IFactorService factorService;

    /**
     * 查询因子列表
     */
    @GetMapping("/list")
    public TableDataInfo list(Factor factor) {
        startPage();
        List<Factor> list = factorService.selectFactorList(factor);
        return getDataTable(list);
    }

    /**
     * 获取因子详情
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(factorService.selectFactorById(id));
    }

    /**
     * 新增因子
     */
    @Log(title = "因子管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody Factor factor) {
        return toAjax(factorService.insertFactor(factor));
    }

    /**
     * 修改因子
     */
    @Log(title = "因子管理", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody Factor factor) {
        return toAjax(factorService.updateFactor(factor));
    }

    /**
     * 删除因子
     */
    @Log(title = "因子管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(factorService.deleteFactorByIds(ids));
    }
}

