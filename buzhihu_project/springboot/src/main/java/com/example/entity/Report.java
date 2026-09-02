package com.example.entity;

/**
 * 内容举报
 */
public class Report {

    private Integer id;
    /** 关联业务ID(博客/活动/评论ID) */
    private Integer fid;
    /** 所属模块(博客/活动/评论) */
    private String module;
    /** 举报原因 */
    private String reason;
    /** 举报人ID */
    private Integer userId;
    /** 举报时间 */
    private String time;
    /** 处理状态(0待处理 1已处理) */
    private Integer status;
    /** 处理结果(IGNORE/DELETE/BAN) */
    private String handleResult;
    /** 处理人ID */
    private Integer handlerId;
    /** 处理时间 */
    private String handleTime;

    /** 举报人姓名(关联查询) */
    private String reporterName;
    /** 举报人头像(关联查询) */
    private String reporterAvatar;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public Integer getFid() { return fid; }
    public void setFid(Integer fid) { this.fid = fid; }
    public String getModule() { return module; }
    public void setModule(String module) { this.module = module; }
    public String getReason() { return reason; }
    public void setReason(String reason) { this.reason = reason; }
    public Integer getUserId() { return userId; }
    public void setUserId(Integer userId) { this.userId = userId; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public Integer getStatus() { return status; }
    public void setStatus(Integer status) { this.status = status; }
    public String getHandleResult() { return handleResult; }
    public void setHandleResult(String handleResult) { this.handleResult = handleResult; }
    public Integer getHandlerId() { return handlerId; }
    public void setHandlerId(Integer handlerId) { this.handlerId = handlerId; }
    public String getHandleTime() { return handleTime; }
    public void setHandleTime(String handleTime) { this.handleTime = handleTime; }
    public String getReporterName() { return reporterName; }
    public void setReporterName(String reporterName) { this.reporterName = reporterName; }
    public String getReporterAvatar() { return reporterAvatar; }
    public void setReporterAvatar(String reporterAvatar) { this.reporterAvatar = reporterAvatar; }
}
