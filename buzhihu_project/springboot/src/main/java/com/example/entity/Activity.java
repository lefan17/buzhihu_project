package com.example.entity;

public class Activity {

    /** ID */
    private Integer id;
    /** 活动名称 */
    private String name;
    /** 活动简介 */
    private String descr;
    /** 开始时间 */
    private String start;
    /** 结束时间 */
    private String end;
    /** 活动形式 */
    private String form;
    /** 活动地址 */
    private String address;
    /** 主办方 */
    private String host;
    /** 浏览量 */
    private Integer readCount;
    private String content;
    private String cover;

    private Boolean isEnd;
    private Boolean isSign;
    private Boolean isLike;
    private Boolean isCollect;

    private Integer likesCount;
    private Integer collectCount;

    /** 报名人数上限(0=不限) */
    private Integer maxCount;
    /** 已报名人数 */
    private Integer signCount;

    private Integer userId;


    public void setEnd(Boolean end) {
        isEnd = end;
    }

    public Boolean getSign() {
        return isSign;
    }

    public void setSign(Boolean sign) {
        isSign = sign;
    }

    public Boolean getLike() {
        return isLike;
    }

    public void setLike(Boolean like) {
        isLike = like;
    }

    public Boolean getCollect() {
        return isCollect;
    }

    public void setCollect(Boolean collect) {
        isCollect = collect;
    }

    public Integer getLikesCount() {
        return likesCount;
    }

    public void setLikesCount(Integer likesCount) {
        this.likesCount = likesCount;
    }

    public Integer getCollectCount() {
        return collectCount;
    }

    public void setCollectCount(Integer collectCount) {
        this.collectCount = collectCount;
    }

    public Integer getMaxCount() {
        return maxCount;
    }

    public void setMaxCount(Integer maxCount) {
        this.maxCount = maxCount;
    }

    public Integer getSignCount() {
        return signCount;
    }

    public void setSignCount(Integer signCount) {
        this.signCount = signCount;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer id) {
    }
    public void setIsSign(Boolean isSign) {
        this.isSign = isSign;
    }

    public Boolean getIsSign() {
        return this.isSign;
    }
    public void setIsEnd(Boolean isEnd) {
        this.isEnd = isEnd;
    }

    public Boolean getIsEnd() {
        return this.isEnd;
    }



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getForm() {
        return form;
    }

    public void setForm(String form) {
        this.form = form;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public Integer getReadCount() {
        return readCount;
    }

    public void setReadCount(Integer readCount) {
        this.readCount = readCount;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }


}