package com.example.controller;

import com.example.common.Result;
import com.example.entity.Report;
import com.example.service.ReportService;
import com.github.pagehelper.PageInfo;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 内容举报接口
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Resource
    private ReportService reportService;

    /**
     * 新增举报
     */
    @PostMapping("/add")
    public Result add(@RequestBody Report report) {
        reportService.add(report);
        return Result.success();
    }

    /**
     * 分页查询（后台）
     */
    @GetMapping("/selectPage")
    public Result selectPage(Report report,
                             @RequestParam(defaultValue = "1") Integer pageNum,
                             @RequestParam(defaultValue = "10") Integer pageSize) {
        PageInfo<Report> page = reportService.selectPage(report, pageNum, pageSize);
        return Result.success(page);
    }

    /**
     * 处理举报（后台）
     */
    @PostMapping("/handle")
    public Result handle(@RequestParam Integer id, @RequestParam String action) {
        reportService.handle(id, action);
        return Result.success();
    }
}
