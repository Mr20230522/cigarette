package com.ruoyi.web.controller.cigarette.vehicle;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.ToVehicleRealTimMonitoring;
import com.ruoyi.system.service.IToVehicleRealTimMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.UUID;


@RestController
@RequestMapping("/toVehicleMonitoring")
public class ToVehicleRealTimMonitoringController {
    @Autowired
    private IToVehicleRealTimMonitoringService vehicleService;

    //    @Autowired
//    private IToVehicleRealTimMonitoringService monitoringService;
//
//    @GetMapping("/list")
//    public AjaxResult list(ToVehicleRealTimMonitoring query) {
//        System.out.println("进入 list 接口了");
//        List<ToVehicleRealTimMonitoring> list = monitoringService.selectToVehicleRealTimMonitoringList(query);
//        return AjaxResult.success(list);
//    }


//    @GetMapping("/list")
//    public AjaxResult getNextRecord(@RequestParam(value = "lastId", defaultValue = "0") Long lastId) {
//        ToVehicleRealTimMonitoring next = vehicleService.getNextRecord(lastId);
//        if (next != null) {
//            return AjaxResult.success(next);
//        } else {
//            return AjaxResult.error("没有更多数据");
//        }
//    }

//    @GetMapping("/list")
//    public AjaxResult getLastedRecord(@RequestParam(value = "captureTime", required = false) Date captureTime) {
//        if (captureTime == null) {
//            captureTime = new Date();
//        }
//        ToVehicleRealTimMonitoring next = vehicleService.getLastedRecord(captureTime);
//        if (next != null) {
//            return AjaxResult.success(next);
//        } else {
//            return AjaxResult.error("没有更多数据");
//        }
//    }


//    @GetMapping("/list")
//    public AjaxResult getLastedRecord(
//        @RequestParam(value = "captureTime", required = false) Date captureTime,
//        @RequestParam(value = "lastId", required = false) Long lastId) {
//
//    ToVehicleRealTimMonitoring next;
//
//    if (lastId != null) {
//        // 有 lastId，则调用 getNextRecord
//        next = vehicleService.getNextRecord(lastId);
//    } else {
//        // 第一次请求：获取最新数据
//        if (captureTime == null) {
//            captureTime = new Date();
//        }
//        next = vehicleService.getLastedRecord(captureTime);
//    }
//
//    if (next != null) {
//        return AjaxResult.success(next);
//    } else {
//        return AjaxResult.error("没有更多数据");
//    }
//}

    //    @GetMapping("/list")
//    public AjaxResult getLastedRecord(
//            @RequestParam(value = "captureTime", required = false) Date captureTime,
//            HttpSession session) {
//
//        // 从 Session 获取上一次的 lastId
//        Long lastId = (Long) session.getAttribute("lastId");
//
//        ToVehicleRealTimMonitoring next;
//
//        if (lastId != null) {
//            // 如果已经有 lastId，直接按 lastId 获取下一条数据
//            next = vehicleService.getNextRecord(lastId);
//        } else {
//            // 第一次请求：按 captureTime 获取最新数据
//            if (captureTime == null) {
//                captureTime = new Date();
//            }
//            next = vehicleService.getLastedRecord(captureTime);
//        }
//
//        if (next != null) {
//            // 记录当前返回数据的 id，给下一次请求用
//            session.setAttribute("lastId", next.getId());
//            return AjaxResult.success(next);
//        } else {
//            return AjaxResult.error("没有更多数据");
//        }
//    }


//    @GetMapping("/list")
//    public AjaxResult getLastedRecord(
//            @RequestParam(value = "captureTime", required = false) Date captureTime,
//            HttpSession session) {
//
//        // 如果是新会话，清空 lastId
//        if (session.isNew()) {
//            session.removeAttribute("lastId");
//        }
//
//        // 从 Session 获取上一次的 lastId
//        Long lastId = (Long) session.getAttribute("lastId");
//
//        ToVehicleRealTimMonitoring next;
//
//        if (lastId != null) {
//            // 如果已经有 lastId，直接按 lastId 获取下一条数据
//            next = vehicleService.getNextRecord(lastId);
//        } else {
//            // 第一次请求：按 captureTime 获取最新数据
//            if (captureTime == null) {
//                captureTime = new Date();
//            }
//            next = vehicleService.getLastedRecord(captureTime);
//        }
//
//        if (next != null) {
//            // 记录当前返回数据的 id，给下一次请求用
//            session.setAttribute("lastId", next.getId());
//            return AjaxResult.success(next);
//        } else {
//            return AjaxResult.error("没有更多数据");
//        }
//    }

    //这个是最接近成功的版本
//    private static final String PAGE_TOKEN_COOKIE = "PAGE_TOKEN";
//
//    @GetMapping("/list")
//    public AjaxResult getLastedRecord(
//            @RequestParam(value = "captureTime", required = false) Date captureTime,
//            HttpSession session,
//            HttpServletRequest request,
//            HttpServletResponse response) {
//
//        // 获取客户端的 pageToken
//        String clientPageToken = getCookieValue(request, PAGE_TOKEN_COOKIE);
//        String serverPageToken = (String) session.getAttribute("pageToken");
//
//        // 如果 pageToken 不存在或不匹配，说明是新页面或刷新
//        if (clientPageToken == null || !clientPageToken.equals(serverPageToken)) {
//            // 清空 lastId
//            session.removeAttribute("lastId");
//
//            // 生成新的 pageToken
//            String newToken = UUID.randomUUID().toString();
//            session.setAttribute("pageToken", newToken);
//
//            // 设置 cookie（浏览器自动带回）
//            Cookie cookie = new Cookie(PAGE_TOKEN_COOKIE, newToken);
//            cookie.setPath("/");  // 作用范围全局
//            cookie.setHttpOnly(false);
//            cookie.setMaxAge(-1); // 浏览器关闭后删除
//            response.addCookie(cookie);
//        }
//
//        Long lastId = (Long) session.getAttribute("lastId");
//        ToVehicleRealTimMonitoring next;
//
//        if (lastId != null) {
//            next = vehicleService.getNextRecord(lastId);
//        } else {
//            if (captureTime == null) {
//                captureTime = new Date();
//            }
//            next = vehicleService.getLastedRecord(captureTime);
//        }
//
//        if (next != null) {
//            session.setAttribute("lastId", next.getId());
//            return AjaxResult.success(next);
//        } else {
//            return AjaxResult.error("没有更多数据");
//        }
//    }
//
//    // 工具方法：获取 cookie 值
//    private String getCookieValue(HttpServletRequest request, String name) {
//        if (request.getCookies() != null) {
//            for (Cookie cookie : request.getCookies()) {
//                if (name.equals(cookie.getName())) {
//                    return cookie.getValue();
//                }
//            }
//        }
//        return null;
//    }
//最完美的版本
    private static final String PAGE_TOKEN_COOKIE = "PAGE_TOKEN";

    @GetMapping("/list")
    public AjaxResult getLastedRecord(
            @RequestParam(value = "captureTime", required = false) Date captureTime,
            @RequestParam(value = "pageToken", required = false) String pageToken, // 新增
            HttpSession session,
            HttpServletRequest request,
            HttpServletResponse response) {

        String serverPageToken = (String) session.getAttribute("pageToken");
        String clientPageToken;

        if (pageToken != null && !pageToken.trim().isEmpty()) {
            // 1. 前端传了 pageToken（新版）
            clientPageToken = pageToken;
        } else {
            // 2. 没传就走旧逻辑，从 Cookie 取
            clientPageToken = getCookieValue(request, PAGE_TOKEN_COOKIE);
        }

        // 如果 pageToken 不存在或不匹配 → 新页面或刷新
        if (clientPageToken == null || !clientPageToken.equals(serverPageToken)) {
            session.removeAttribute("lastId");

            // 记录新的 pageToken（可能来自前端，也可能自己生成）
            String newToken = (clientPageToken != null) ? clientPageToken : UUID.randomUUID().toString();
            session.setAttribute("pageToken", newToken);

            // 旧逻辑需要设置 cookie（兼容旧版本）
            if (pageToken == null) {
                Cookie cookie = new Cookie(PAGE_TOKEN_COOKIE, newToken);
                cookie.setPath("/");
                cookie.setHttpOnly(false);
                cookie.setMaxAge(-1); // 浏览器关闭后删除
                response.addCookie(cookie);
            }
        }

        Long lastId = (Long) session.getAttribute("lastId");
        ToVehicleRealTimMonitoring next;

        if (lastId != null) {
            next = vehicleService.getNextRecord(lastId);
        } else {
            if (captureTime == null) {
                captureTime = new Date();
            }
            next = vehicleService.getLastedRecord(captureTime);
        }

        if (next != null) {
            session.setAttribute("lastId", next.getId());
            return AjaxResult.success(next);
        } else {
            return AjaxResult.error("没有更多数据");
        }
    }

    // 工具方法：获取 cookie 值
    private String getCookieValue(HttpServletRequest request, String name) {
        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (name.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

//    @GetMapping("/list")
//    public AjaxResult getLastedRecord(
//            @RequestParam(value = "captureTime", required = false) Date captureTime,
//            HttpSession session) {
//
//        Long lastAccessTime = (Long) session.getAttribute("lastAccessTime");
//        long now = System.currentTimeMillis();
//
//        // 如果是第一次访问，或者距离上次访问超过1秒，认为是刷新，重置 lastId
//        if (lastAccessTime == null || now - lastAccessTime > 1000) {
//            session.removeAttribute("lastId");
//        }
//
//        session.setAttribute("lastAccessTime", now);
//
//        Long lastId = (Long) session.getAttribute("lastId");
//        ToVehicleRealTimMonitoring next;
//
//        if (lastId != null) {
//            next = vehicleService.getNextRecord(lastId);
//        } else {
//            if (captureTime == null) {
//                captureTime = new Date();
//            }
//            next = vehicleService.getLastedRecord(captureTime);
//        }
//
//        if (next != null) {
//            session.setAttribute("lastId", next.getId());
//            return AjaxResult.success(next);
//        } else {
//            return AjaxResult.error("没有更多数据");
//        }
//    }


//    private static final String PAGE_TOKEN_COOKIE = "PAGE_TOKEN";
//
//    @GetMapping("/list")
//    public AjaxResult getLastedRecord(
//            @RequestParam(value = "captureTime", required = false) Date captureTime,
//            @RequestParam(value = "pageToken", required = false) String clientPageToken,
//            HttpSession session,
//            HttpServletRequest request,
//            HttpServletResponse response) {
//
//        // 如果请求参数没有 pageToken，再从 Cookie 里取（兼容旧前端）
//        if (clientPageToken == null) {
//            clientPageToken = getCookieValue(request, PAGE_TOKEN_COOKIE);
//        }
//
//        String serverPageToken = (String) session.getAttribute("pageToken");
//
//        if (clientPageToken == null || !clientPageToken.equals(serverPageToken)) {
//            // 新页面或刷新，清空 lastId
//            session.removeAttribute("lastId");
//
//            // 生成新的 pageToken 并保存到 Session
//            String newToken = UUID.randomUUID().toString();
//            session.setAttribute("pageToken", newToken);
//
//            // 设置 cookie，方便老版本前端或其他请求使用
//            Cookie cookie = new Cookie(PAGE_TOKEN_COOKIE, newToken);
//            cookie.setPath("/");
//            cookie.setHttpOnly(false);
//            cookie.setMaxAge(-1);
//            response.addCookie(cookie);
//        }
//
//        Long lastId = (Long) session.getAttribute("lastId");
//        ToVehicleRealTimMonitoring next;
//
//        if (lastId != null) {
//            next = vehicleService.getNextRecord(lastId);
//        } else {
//            if (captureTime == null) {
//                captureTime = new Date();
//            }
//            next = vehicleService.getLastedRecord(captureTime);
//        }
//
//        if (next != null) {
//            session.setAttribute("lastId", next.getId());
//            return AjaxResult.success(next);
//        } else {
//            return AjaxResult.error("没有更多数据");
//        }
//    }
//
//    private String getCookieValue(HttpServletRequest request, String name) {
//        if (request.getCookies() == null) return null;
//        for (Cookie cookie : request.getCookies()) {
//            if (name.equals(cookie.getName())) {
//                return cookie.getValue();
//            }
//        }
//        return null;
//    }
}
