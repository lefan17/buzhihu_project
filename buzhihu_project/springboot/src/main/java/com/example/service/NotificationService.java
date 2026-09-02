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
 * 站内通知业务处理
 */
@Service
public class NotificationService {

    @Resource
    private NotificationMapper notificationMapper;
    @Resource
    private BlogMapper blogMapper;
    @Resource
    private ActivityMapper activityMapper;
    @Resource
    private CommentMapper commentMapper;
    @Resource
    private UserMapper userMapper;

    /**
     * 新增通知
     */
    public void add(Notification notification) {
        notification.setTime(DateUtil.now());
        if (notification.getIsRead() == null) {
            notification.setIsRead(0);
        }
        notificationMapper.insert(notification);
    }

    /**
     * 创建通知（自动跳过"自己通知自己"）
     */
    public void create(Integer recipientId, Integer actorId, String type, String module, Integer fid, String content) {
        if (recipientId == null || actorId == null || recipientId.equals(actorId)) {
            return;
        }
        Notification notification = new Notification();
        notification.setUserId(recipientId);
        notification.setActorId(actorId);
        notification.setType(type);
        notification.setModule(module);
        notification.setFid(fid);
        notification.setContent(content);
        add(notification);
    }

    private String actorName(Integer actorId) {
        if (actorId == null) {
            return "有人";
        }
        User user = userMapper.selectById(actorId);
        if (user == null) {
            return "有人";
        }
        return user.getName() != null ? user.getName() : user.getUsername();
    }

    /**
     * 评论/回复触发通知
     */
    public void onComment(Comment comment) {
        Integer authorId = null;
        String type = "COMMENT";
        String targetTitle = null;
        if (comment.getPid() != null) {
            // 回复：通知被回复的评论作者
            Comment parent = commentMapper.selectById(comment.getPid());
            if (parent != null) {
                authorId = parent.getUserId();
                type = "REPLY";
            }
        }
        if (authorId == null && "博客".equals(comment.getModule())) {
            Blog blog = blogMapper.selectById(comment.getFid());
            if (blog != null) {
                authorId = blog.getUserId();
                targetTitle = blog.getTitle();
            }
        } else if (authorId == null && "活动".equals(comment.getModule())) {
            Activity activity = activityMapper.selectById(comment.getFid());
            if (activity != null) {
                authorId = activity.getUserId();
                targetTitle = activity.getName();
            }
        }
        String content;
        if ("REPLY".equals(type)) {
            content = actorName(comment.getUserId()) + " 回复了你的评论";
        } else if ("博客".equals(comment.getModule())) {
            content = actorName(comment.getUserId()) + " 评论了你的博客《" + (targetTitle != null ? targetTitle : "未命名") + "》";
        } else {
            content = actorName(comment.getUserId()) + " 评论了你的活动《" + (targetTitle != null ? targetTitle : "未命名") + "》";
        }
        create(authorId, comment.getUserId(), type, comment.getModule(), comment.getFid(), content);
    }

    /**
     * 点赞触发通知
     */
    public void onLike(Likes likes) {
        Integer authorId = null;
        String targetTitle = null;
        if ("博客".equals(likes.getModule())) {
            Blog blog = blogMapper.selectById(likes.getFid());
            if (blog != null) {
                authorId = blog.getUserId();
                targetTitle = blog.getTitle();
            }
        } else if ("活动".equals(likes.getModule())) {
            Activity activity = activityMapper.selectById(likes.getFid());
            if (activity != null) {
                authorId = activity.getUserId();
                targetTitle = activity.getName();
            }
        }
        String content = actorName(likes.getUserId()) + " 赞了你的" +
                ("博客".equals(likes.getModule()) ? "博客" : "活动") +
                "《" + (targetTitle != null ? targetTitle : "未命名") + "》";
        create(authorId, likes.getUserId(), "LIKE", likes.getModule(), likes.getFid(), content);
    }

    /**
     * 收藏触发通知
     */
    public void onCollect(Collect collect) {
        Integer authorId = null;
        String targetTitle = null;
        if ("博客".equals(collect.getModule())) {
            Blog blog = blogMapper.selectById(collect.getFid());
            if (blog != null) {
                authorId = blog.getUserId();
                targetTitle = blog.getTitle();
            }
        } else if ("活动".equals(collect.getModule())) {
            Activity activity = activityMapper.selectById(collect.getFid());
            if (activity != null) {
                authorId = activity.getUserId();
                targetTitle = activity.getName();
            }
        }
        String content = actorName(collect.getUserId()) + " 收藏了你的" +
                ("博客".equals(collect.getModule()) ? "博客" : "活动") +
                "《" + (targetTitle != null ? targetTitle : "未命名") + "》";
        create(authorId, collect.getUserId(), "COLLECT", collect.getModule(), collect.getFid(), content);
    }

    /**
     * 报名活动触发通知
     */
    public void onSign(ActivitySign sign) {
        Activity activity = activityMapper.selectById(sign.getActivityId());
        if (activity == null) {
            return;
        }
        String content = actorName(sign.getUserId()) + " 报名了你的活动《" + (activity.getName() != null ? activity.getName() : "未命名") + "》";
        create(activity.getUserId(), sign.getUserId(), "SIGN", "活动", activity.getId(), content);
    }

    /**
     * 分页查询当前用户的通知
     */
    public PageInfo<Notification> selectPage(Notification notification, Integer pageNum, Integer pageSize) {
        Account currentUser = TokenUtils.getCurrentUser();
        notification.setUserId(currentUser.getId());
        PageHelper.startPage(pageNum, pageSize);
        List<Notification> list = notificationMapper.selectAll(notification.getUserId());
        return PageInfo.of(list);
    }

    /**
     * 当前用户未读数
     */
    public int unreadCount() {
        Account currentUser = TokenUtils.getCurrentUser();
        return notificationMapper.unreadCount(currentUser.getId());
    }

    /**
     * 全部标记已读
     */
    public void readAll() {
        Account currentUser = TokenUtils.getCurrentUser();
        notificationMapper.readAll(currentUser.getId());
    }

    /**
     * 单条标记已读
     */
    public void read(Integer id) {
        Account currentUser = TokenUtils.getCurrentUser();
        notificationMapper.read(id, currentUser.getId());
    }
}
