package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.*;
import com.example.mapper.*;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 内容举报业务处理
 */
@Service
public class ReportService {

    @Resource
    private ReportMapper reportMapper;
    @Resource
    private BlogMapper blogMapper;
    @Resource
    private ActivityMapper activityMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private BlogService blogService;
    @Resource
    private ActivityService activityService;
    @Resource
    private CommentService commentService;
    @Resource
    private UserService userService;

    /**
     * 新增举报
     */
    public void add(Report report) {
        Account currentUser = TokenUtils.getCurrentUser();
        report.setUserId(currentUser.getId());
        report.setTime(DateUtil.now());
        report.setStatus(0);
        reportMapper.insert(report);
    }

    /**
     * 分页查询
     */
    public PageInfo<Report> selectPage(Report report, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<Report> list = reportMapper.selectAll(report);
        return PageInfo.of(list);
    }

    /**
     * 处理举报：IGNORE 忽略 / DELETE 删除内容 / BAN 封禁内容作者
     */
    public void handle(Integer id, String action) {
        Report report = reportMapper.selectById(id);
        if (report == null) {
            return;
        }
        Account currentUser = TokenUtils.getCurrentUser();
        report.setStatus(1);
        report.setHandleResult(action);
        report.setHandlerId(currentUser.getId());
        report.setHandleTime(DateUtil.now());
        reportMapper.updateById(report);

        if ("DELETE".equals(action)) {
            if ("博客".equals(report.getModule())) {
                blogService.deleteById(report.getFid());
            } else if ("活动".equals(report.getModule())) {
                activityService.deleteById(report.getFid());
            } else if ("评论".equals(report.getModule())) {
                commentService.deleteById(report.getFid());
            }
        } else if ("BAN".equals(action)) {
            Integer ownerId = findOwnerId(report);
            if (ownerId != null) {
                userService.ban(ownerId);
            }
        }
    }

    /**
     * 查询被举报内容的作者ID
     */
    private Integer findOwnerId(Report report) {
        if ("博客".equals(report.getModule())) {
            Blog blog = blogMapper.selectById(report.getFid());
            return blog == null ? null : blog.getUserId();
        }
        if ("活动".equals(report.getModule())) {
            Activity activity = activityMapper.selectById(report.getFid());
            return activity == null ? null : activity.getUserId();
        }
        if ("评论".equals(report.getModule())) {
            Comment comment = commentMapper.selectById(report.getFid());
            return comment == null ? null : comment.getUserId();
        }
        return null;
    }
}
