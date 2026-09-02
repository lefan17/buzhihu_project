package com.example.mapper;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;
import java.util.Map;

/**
 * 数据看板统计
 */
public interface DashboardMapper {

    @Select("select count(*) from user")
    int countUser();

    @Select("select count(*) from blog")
    int countBlog();

    @Select("select count(*) from activity")
    int countActivity();

    @Select("select count(*) from comment")
    int countComment();

    @Select("select count(*) from activity_sign")
    int countSign();

    @Select("select count(*) from likes")
    int countLike();

    @Select("select count(*) from collect")
    int countCollect();

    @Select("select count(*) from report where status = 0")
    int countPendingReport();

    @Select("select count(*) from blog where date = #{day}")
    int blogCountByDay(@Param("day") String day);

    @Select("select count(*) from user where create_time = #{day}")
    int userCountByDay(@Param("day") String day);

    @Select("select c.name as name, count(b.id) as cnt from category c " +
            "left join blog b on b.category_id = c.id " +
            "group by c.id, c.name")
    List<Map<String, Object>> categoryBlogCount();
}
