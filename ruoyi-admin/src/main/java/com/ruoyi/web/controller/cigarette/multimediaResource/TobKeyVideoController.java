package com.ruoyi.web.controller.cigarette.multimediaResource;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.common.config.RuoYiConfig;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.file.FileUploadUtils;
import com.ruoyi.common.utils.file.MimeTypeUtils;
//import jdk.nashorn.internal.objects.Global;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.TobKeyVideo;
import com.ruoyi.system.service.ITobKeyVideoService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.web.multipart.MultipartFile;

/**
 * 关键帧视频Controller
 * 
 * @author muyouzhi
 * @date 2024-10-27
 */
@RestController
@RequestMapping("/cigarette/multimediaResource/keyVideo")
public class TobKeyVideoController extends BaseController
{
    @Autowired
    private ITobKeyVideoService tobKeyVideoService;

    /**
     * 查询关键帧视频列表
     */
    @PreAuthorize("@ss.hasPermi('multimediaResource:keyVideo:list')")
    @GetMapping("/list")
    public TableDataInfo list(TobKeyVideo tobKeyVideo)
    {
        startPage();
        List<TobKeyVideo> list = tobKeyVideoService.selectTobKeyVideoList(tobKeyVideo);
        return getDataTable(list);
    }

    /**
     * 导出关键帧视频列表
     */
    @PreAuthorize("@ss.hasPermi('multimediaResource:keyVideo:export')")
    @Log(title = "关键帧视频", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TobKeyVideo tobKeyVideo)
    {
        List<TobKeyVideo> list = tobKeyVideoService.selectTobKeyVideoList(tobKeyVideo);
        ExcelUtil<TobKeyVideo> util = new ExcelUtil<TobKeyVideo>(TobKeyVideo.class);
        util.exportExcel(response, list, "关键帧视频数据");
    }

    /**
     * 获取关键帧视频详细信息
     */
    @PreAuthorize("@ss.hasPermi('multimediaResource:keyVideo:query')")
    @GetMapping(value = "/{videoId}")
    public AjaxResult getInfo(@PathVariable("videoId") Long videoId)
    {
        return success(tobKeyVideoService.selectTobKeyVideoByVideoId(videoId));
    }

    /**
     * 新增关键帧视频
     */
    @PreAuthorize("@ss.hasPermi('multimediaResource:keyVideo:add')")
    @Log(title = "关键帧视频", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TobKeyVideo tobKeyVideo)
    {
        return toAjax(tobKeyVideoService.insertTobKeyVideo(tobKeyVideo));
    }

    /**
     * 修改关键帧视频
     */
    @PreAuthorize("@ss.hasPermi('multimediaResource:keyVideo:edit')")
    @Log(title = "关键帧视频", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TobKeyVideo tobKeyVideo)
    {
        return toAjax(tobKeyVideoService.updateTobKeyVideo(tobKeyVideo));
    }

    /**
     * 删除关键帧视频
     */
    @PreAuthorize("@ss.hasPermi('multimediaResource:keyVideo:remove')")
    @Log(title = "关键帧视频", businessType = BusinessType.DELETE)
	@DeleteMapping("/{videoIds}")
    public AjaxResult remove(@PathVariable Long[] videoIds)
    {
        return toAjax(tobKeyVideoService.deleteTobKeyVideoByVideoIds(videoIds));
    }
//
//    @Log(title = "用户头像", businessType = BusinessType.UPDATE)
//    @PostMapping("/avatar")
//    public AjaxResult avatar(@RequestParam("avatarfile") MultipartFile file) throws Exception
//    {
//        if (!file.isEmpty())
//        {
//            LoginUser loginUser = getLoginUser();
//            String avatar = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), file, MimeTypeUtils.IMAGE_EXTENSION);
//            if (userService.updateUserAvatar(loginUser.getUsername(), avatar))
//            {
//                AjaxResult ajax = AjaxResult.success();
//                return ajax;
//            }
//        }
//        return error("上传图片异常，请联系管理员");}

    @PostMapping("/uploadVideo")
    public AjaxResult uploadVideo(@RequestParam("file") MultipartFile file) throws Exception {
        try {
            // 使用FileUploadUtils上传文件
            String fileName = FileUploadUtils.upload(RuoYiConfig.getAvatarPath(), file, MimeTypeUtils.VIDEO_EXTENSION);
            // 假设您返回的是一个包含文件访问URL的对象
            System.out.println("上传成功");
            return AjaxResult.success("上传成功",  fileName);
        } catch (Exception e) {
            e.printStackTrace(); // 打印异常堆栈，方便调试
            return AjaxResult.error("上传视频异常，请联系管理员");
        }
    }
}
