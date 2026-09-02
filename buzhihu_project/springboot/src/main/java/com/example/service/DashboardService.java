package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.mapper.DashboardMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * 数据看板业务处理
 */
@Service
public class DashboardService {

    @Resource
    private DashboardMapper dashboardMapper;

    public Map<String, Object> statistics() {
        Map<String, Object> data = new HashMap<>();
        data.put("userCount", dashboardMapper.countUser());
        data.put("blogCount", dashboardMapper.countBlog());
        data.put("activityCount", dashboardMapper.countActivity());
        data.put("commentCount", dashboardMapper.countComment());
        data.put("signCount", dashboardMapper.countSign());
        data.put("likeCount", dashboardMapper.countLike());
        data.put("collectCount", dashboardMapper.countCollect());
        data.put("pendingReportCount", dashboardMapper.countPendingReport());

        // 近7日新增博客/用户趋势
        List<Map<String, Object>> trend = new ArrayList<>();
        for (int i = 6; i >= 0; i--) {
            String day = DateUtil.format(DateUtil.offsetDay(new Date(), -i), "yyyy-MM-dd");
            Map<String, Object> item = new HashMap<>();
            item.put("date", day);
            item.put("blogCount", dashboardMapper.blogCountByDay(day));
            item.put("userCount", dashboardMapper.userCountByDay(day));
            trend.add(item);
        }
        data.put("trend", trend);

        // 分类文章数分布
        data.put("categoryBlogCount", dashboardMapper.categoryBlogCount());

        // 互动转化榜单：报名最多的活动 / 点赞最多的文章
        data.put("topActivitySigns", dashboardMapper.topActivitySign());
        data.put("topBlogLikes", dashboardMapper.topBlogLikes());
        return data;
    }
}
