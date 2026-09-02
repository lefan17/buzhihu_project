package com.example.entity;

/**
 * 用户关注
 */
public class Follow {

    private Integer id;
    /** 关注者ID */
    private Integer userId;
    /** 被关注者ID */
    private Integer followId;
    /** 关注时间 */
    private String time;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public Integer getFollowId() { return followId; }
    public void setFollowId(Integer followId) { this.followId = followId; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
}
