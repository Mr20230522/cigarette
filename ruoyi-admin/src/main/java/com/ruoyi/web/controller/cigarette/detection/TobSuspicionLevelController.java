package com.ruoyi.web.controller.cigarette.detection;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.service.ITobSuspicionLevelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/system/suspicion")
public class TobSuspicionLevelController {
    @Autowired
    private ITobSuspicionLevelService tobSuspicionLevelService;

    @GetMapping("/level")
    public AjaxResult getLevel() {
        return AjaxResult.success(tobSuspicionLevelService.getCurrentLevel());
    }

    @PostMapping("/level")
    public AjaxResult updateLevel(@RequestParam Double level) {
        try {
            return tobSuspicionLevelService.updateLevel(level)
                    ? AjaxResult.success()
                    : AjaxResult.error("更新失败");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}