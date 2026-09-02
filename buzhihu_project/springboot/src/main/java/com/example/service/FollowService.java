package com.example.service;

import cn.hutool.core.date.DateUtil;
import com.example.entity.Account;
import com.example.entity.Follow;
import com.example.mapper.FollowMapper;
import com.example.utils.TokenUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

/**
 * 用户关注业务处理
 */
@Service
public class FollowService {

    @Resource
    private FollowMapper followMapper;

    /**
     * 关注/取消关注（切换）
     */
    public void set(Follow follow) {
        Account currentUser = TokenUtils.getCurrentUser();
        follow.setUserId(currentUser.getId());
        Follow dbFollow = followMapper.selectByUserAndFollow(follow.getUserId(), follow.getFollowId());
        if (dbFollow == null) {
            follow.setTime(DateUtil.now());
            followMapper.insert(follow);
        } else {
            followMapper.deleteByUserAndFollow(follow.getUserId(), follow.getFollowId());
        }
    }

    /**
     * 当前用户是否已关注某人
     */
    public boolean isFollowing(Integer followId) {
        Account currentUser = TokenUtils.getCurrentUser();
        return followMapper.selectByUserAndFollow(currentUser.getId(), followId) != null;
    }

    /**
     * 粉丝数/关注数
     */
    public Map<String, Integer> count(Integer userId) {
        Map<String, Integer> map = new HashMap<>();
        map.put("followers", followMapper.countFollowers(userId));
        map.put("following", followMapper.countFollowing(userId));
        return map;
    }
}
