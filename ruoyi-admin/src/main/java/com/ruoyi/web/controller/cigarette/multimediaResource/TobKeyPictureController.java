package com.ruoyi.web.controller.cigarette.multimediaResource;

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
import com.ruoyi.system.domain.TobKeyPicture;
import com.ruoyi.system.service.ITobKeyPictureService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 关键帧图片Controller
 * 
 * @author muyouzhi
 * @date 2024-10-27
 */
@RestController
@RequestMapping("/cigarette/multimediaResource/picture")
public class TobKeyPictureController extends BaseController
{
    @Autowired
    private ITobKeyPictureService tobKeyPictureService;

    /**
     * 查询关键帧图片列表
     */
    @PreAuthorize("@ss.hasPermi('multimediaResource:picture:list')")
    @GetMapping("/list")
    public TableDataInfo list(TobKeyPicture tobKeyPicture)
    {
        startPage();
        List<TobKeyPicture> list = tobKeyPictureService.selectTobKeyPictureList(tobKeyPicture);
        return getDataTable(list);
    }

    /**
     * 导出关键帧图片列表
     */
    @PreAuthorize("@ss.hasPermi('multimediaResource:picture:export')")
    @Log(title = "关键帧图片", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TobKeyPicture tobKeyPicture)
    {
        List<TobKeyPicture> list = tobKeyPictureService.selectTobKeyPictureList(tobKeyPicture);
        ExcelUtil<TobKeyPicture> util = new ExcelUtil<TobKeyPicture>(TobKeyPicture.class);
        util.exportExcel(response, list, "关键帧图片数据");
    }

    /**
     * 获取关键帧图片详细信息
     */
    @PreAuthorize("@ss.hasPermi('multimediaResource:picture:query')")
    @GetMapping(value = "/{keyPictureId}")
    public AjaxResult getInfo(@PathVariable("keyPictureId") Long keyPictureId)
    {
        return success(tobKeyPictureService.selectTobKeyPictureByKeyPictureId(keyPictureId));
    }

    /**
     * 新增关键帧图片
     */
    @PreAuthorize("@ss.hasPermi('multimediaResource:picture:add')")
    @Log(title = "关键帧图片", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TobKeyPicture tobKeyPicture)
    {
        return toAjax(tobKeyPictureService.insertTobKeyPicture(tobKeyPicture));
    }

    /**
     * 修改关键帧图片
     */
    @PreAuthorize("@ss.hasPermi('multimediaResource:picture:edit')")
    @Log(title = "关键帧图片", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TobKeyPicture tobKeyPicture)
    {
        return toAjax(tobKeyPictureService.updateTobKeyPicture(tobKeyPicture));
    }

    /**
     * 删除关键帧图片
     */
    @PreAuthorize("@ss.hasPermi('multimediaResource:picture:remove')")
    @Log(title = "关键帧图片", businessType = BusinessType.DELETE)
	@DeleteMapping("/{keyPictureIds}")
    public AjaxResult remove(@PathVariable Long[] keyPictureIds)
    {
        return toAjax(tobKeyPictureService.deleteTobKeyPictureByKeyPictureIds(keyPictureIds));
    }
}
