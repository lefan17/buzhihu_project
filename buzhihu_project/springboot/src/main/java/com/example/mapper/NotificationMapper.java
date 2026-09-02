package com.example.mapper;

import com.example.entity.Notification;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

public interface NotificationMapper {

    @Insert("insert into notification (user_id, actor_id, type, module, fid, content, is_read, time) " +
            "values (#{userId}, #{actorId}, #{type}, #{module}, #{fid}, #{content}, #{isRead}, #{time})")
    void insert(Notification notification);

    @Select("select n.*, u.name as actorName, u.avatar as actorAvatar " +
            "from notification n " +
            "left join user u on n.actor_id = u.id " +
            "where n.user_id = #{userId} " +
            "order by n.id desc")
    List<Notification> selectAll(@Param("userId") Integer userId);

    @Select("select count(*) from notification where user_id = #{userId} and is_read = 0")
    int unreadCount(@Param("userId") Integer userId);

    @Update("update notification set is_read = 1 where user_id = #{userId}")
    void readAll(@Param("userId") Integer userId);

    @Update("update notification set is_read = 1 where id = #{id} and user_id = #{userId}")
    void read(@Param("id") Integer id, @Param("userId") Integer userId);
}
