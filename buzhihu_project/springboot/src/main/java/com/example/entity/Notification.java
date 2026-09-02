package com.example.entity;

/**
 * 站内通知
 */
public class Notification {

    private Integer id;
    /** 接收人ID */
    private Integer userId;
    /** 触发人ID */
    private Integer actorId;
    /** 类型：COMMENT/REPLY/LIKE/COLLECT/SIGN/SYSTEM */
    private String type;
    /** 所属模块：博客/活动 */
    private String module;
    /** 关联业务ID(博客/活动ID) */
    private Integer fid;
    /** 通知文案 */
    private String content;
    /** 是否已读(0未读 1已读) */
    private Integer isRead;
    /** 通知时间 */
    private String time;

    /** 触发人姓名(关联查询) */
    private String actorName;
    /** 触发人头像(关联查询) */
    private String actorAvatar;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getActorId() { return actorId; }
    public void setActorId(Integer actorId) { this.actorId = actorId; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }
    public Integer getFid() { return fid; }
    public void setFid(Integer fid) { this.fid = fid; }
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    public Integer getIsRead() { return isRead; }
    public void setIsRead(Integer isRead) { this.isRead = isRead; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public String getActorName() { return actorName; }
    public void setActorName(String actorName) { this.actorName = actorName; }
    public String getActorAvatar() { return actorAvatar; }
    public void setActorAvatar(String actorAvatar) { this.actorAvatar = actorAvatar; }
}
