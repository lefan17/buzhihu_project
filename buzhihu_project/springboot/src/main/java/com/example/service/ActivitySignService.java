package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.ActivitySign;
import com.example.exception.CustomException;
import com.example.mapper.ActivitySignMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class ActivitySignService {

    @Resource
    ActivitySignMapper activitySignMapper;

    @Resource
    NotificationService notificationService;

    public void add(ActivitySign activitySign) {
        Account currentUser = TokenUtils.getCurrentUser();
        ActivitySign as = this.selectByActivityIdAndUserId(activitySign.getActivityId(), currentUser.getId());  // 查看用户是否已经报名
        if (as != null) {
            throw new CustomException(ResultCodeEnum.ACTIVITY_SIGN_ERROR);
        }
        activitySign.setUserId(currentUser.getId());
        activitySign.setTime(DateUtil.now());
        activitySignMapper.insert(activitySign);
        // 站内通知：通知活动创建人
        notificationService.onSign(activitySign);
    }

    public ActivitySign selectByActivityIdAndUserId(Integer actId, Integer userId) {
        return activitySignMapper.selectByActivityIdAndUserId(actId, userId);
    }

    public PageInfo<ActivitySign> selectPage(ActivitySign activitySign, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<ActivitySign> list = activitySignMapper.selectAll(activitySign);
        return PageInfo.of(list);
    }

    public void deleteById(Integer id) {
        Account currentUser = TokenUtils.getCurrentUser();
        if (!RoleEnum.ADMIN.name().equals(currentUser.getRole())) {
            throw new CustomException(ResultCodeEnum.NO_AUTH);
        }
        activitySignMapper.deleteById(id);
    }

    /**
     * 当前用户取消某活动的报名
     */
    public void deleteByActivityId(Integer activityId) {
        Account currentUser = TokenUtils.getCurrentUser();
        ActivitySign sign = this.selectByActivityIdAndUserId(activityId, currentUser.getId());
        if (sign != null) {
            activitySignMapper.deleteById(sign.getId());
        }
    }

    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }
}