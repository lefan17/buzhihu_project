package com.example.controller;

import com.example.common.Result;
import com.example.entity.Follow;
import com.example.service.FollowService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * 关注接口
 */
@RestController
@RequestMapping("/follow")
public class FollowController {

    @Resource
    private FollowService followService;

    /**
     * 关注/取消关注
     */
    @PostMapping("/set")
    public Result set(@RequestBody Follow follow) {
        followService.set(follow);
        return Result.success();
    }

    /**
     * 当前用户是否已关注某人
     */
    @GetMapping("/isFollowing")
    public Result isFollowing(@RequestParam Integer followId) {
        return Result.success(followService.isFollowing(followId));
    }

    /**
     * 某用户的粉丝数/关注数
     */
    @GetMapping("/count")
    public Result count(@RequestParam Integer userId) {
        return Result.success(followService.count(userId));
    }
}
