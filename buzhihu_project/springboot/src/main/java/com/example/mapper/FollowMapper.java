package com.example.mapper;

import com.example.entity.Follow;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface FollowMapper {

    @Insert("insert into follow (user_id, follow_id, time) values (#{userId}, #{followId}, #{time})")
    void insert(Follow follow);

    @Select("select * from follow where user_id = #{userId} and follow_id = #{followId}")
    Follow selectByUserAndFollow(@Param("userId") Integer userId, @Param("followId") Integer followId);

    @Delete("delete from follow where user_id = #{userId} and follow_id = #{followId}")
    void deleteByUserAndFollow(@Param("userId") Integer userId, @Param("followId") Integer followId);

    @Select("select count(*) from follow where follow_id = #{followId}")
    int countFollowers(@Param("followId") Integer followId);

    @Select("select count(*) from follow where user_id = #{userId}")
    int countFollowing(@Param("userId") Integer userId);
}
