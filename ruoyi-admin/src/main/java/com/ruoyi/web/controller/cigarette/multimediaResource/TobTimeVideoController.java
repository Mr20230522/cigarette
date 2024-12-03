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
import com.ruoyi.system.domain.TobTimeVideo;
import com.ruoyi.system.service.ITobTimeVideoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 片段化视频Controller
 * 
 * @author muyouzhi
 * @date 2024-10-27
 */
@RestController
@RequestMapping("/cigarette/multimediaResource/timeVideo")
public class TobTimeVideoController extends BaseController
{
    @Autowired
    private ITobTimeVideoService tobTimeVideoService;

    /**
     * 查询片段化视频列表
     */
    @PreAuthorize("@ss.hasPermi('multimediaResource:timeVideo:list')")
    @GetMapping("/list")
    public TableDataInfo list(TobTimeVideo tobTimeVideo)
    {
        startPage();
        List<TobTimeVideo> list = tobTimeVideoService.selectTobTimeVideoList(tobTimeVideo);
        return getDataTable(list);
    }

    /**
     * 导出片段化视频列表
     */
    @PreAuthorize("@ss.hasPermi('multimediaResource:timeVideo:export')")
    @Log(title = "片段化视频", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TobTimeVideo tobTimeVideo)
    {
        List<TobTimeVideo> list = tobTimeVideoService.selectTobTimeVideoList(tobTimeVideo);
        ExcelUtil<TobTimeVideo> util = new ExcelUtil<TobTimeVideo>(TobTimeVideo.class);
        util.exportExcel(response, list, "片段化视频数据");
    }

    /**
     * 获取片段化视频详细信息
     */
    @PreAuthorize("@ss.hasPermi('multimediaResource:timeVideo:query')")
    @GetMapping(value = "/{videoId}")
    public AjaxResult getInfo(@PathVariable("videoId") Long videoId)
    {
        return success(tobTimeVideoService.selectTobTimeVideoByVideoId(videoId));
    }

    /**
     * 新增片段化视频
     */
    @PreAuthorize("@ss.hasPermi('multimediaResource:timeVideo:add')")
    @Log(title = "片段化视频", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TobTimeVideo tobTimeVideo)
    {
        return toAjax(tobTimeVideoService.insertTobTimeVideo(tobTimeVideo));
    }

    /**
     * 修改片段化视频
     */
    @PreAuthorize("@ss.hasPermi('multimediaResource:timeVideo:edit')")
    @Log(title = "片段化视频", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TobTimeVideo tobTimeVideo)
    {
        return toAjax(tobTimeVideoService.updateTobTimeVideo(tobTimeVideo));
    }

    /**
     * 删除片段化视频
     */
    @PreAuthorize("@ss.hasPermi('multimediaResource:timeVideo:remove')")
    @Log(title = "片段化视频", businessType = BusinessType.DELETE)
	@DeleteMapping("/{videoIds}")
    public AjaxResult remove(@PathVariable Long[] videoIds)
    {
        return toAjax(tobTimeVideoService.deleteTobTimeVideoByVideoIds(videoIds));
    }
}
