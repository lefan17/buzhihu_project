package com.example.controller;

import com.example.common.Result;
import com.example.service.DashboardService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.Map;

/**
 * 数据看板接口
 */
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    @Resource
    private DashboardService dashboardService;

    @GetMapping("/statistics")
    public Result statistics() {
        Map<String, Object> data = dashboardService.statistics();
        return Result.success(data);
    }
}
