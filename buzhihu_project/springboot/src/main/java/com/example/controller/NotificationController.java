package com.example.controller;

import com.example.common.Result;
import com.example.entity.Notification;
import com.example.service.NotificationService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 站内通知接口
 */
@RestController
@RequestMapping("/notification")
public class NotificationController {

    @Resource
    private NotificationService notificationService;

    /**
     * 分页查询当前用户的通知
     */
    @GetMapping("/selectPage")
    public Result selectPage(Notification notification,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Notification> page = notificationService.selectPage(notification, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 未读数量
     */
    @GetMapping("/unreadCount")
    public Result unreadCount() {
        return Result.success(notificationService.unreadCount());
    }

    /**
     * 全部标记已读
     */
    @PutMapping("/readAll")
    public Result readAll() {
        notificationService.readAll();
        return Result.success();
    }

    /**
     * 单条标记已读
     */
    @PutMapping("/read/{id}")
    public Result read(@PathVariable Integer id) {
        notificationService.read(id);
        return Result.success();
    }
}
