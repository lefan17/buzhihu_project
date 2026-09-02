package com.example.service;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.crypto.SecureUtil;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.entity.Admin;
import com.example.entity.User;
import com.example.exception.CustomException;
import com.example.mapper.BlogMapper;
import com.example.mapper.FollowMapper;
import com.example.mapper.UserMapper;
import com.example.utils.TokenUtils;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private BlogMapper blogMapper;

    @Resource
    private FollowMapper followMapper;

    public void add(User user) {
        //判断用户账号是否为空
        User dbUser = userMapper.selectByUsername(user.getUsername());
        if (dbUser != null) {
            throw new CustomException(ResultCodeEnum.USER_EXIST_ERROR);
        }
        //判断密码是否为空
        if (ObjectUtil.isEmpty(user.getPassword())) {
            user.setPassword(Constants.USER_DEFAULT_PASSWORD);
        }
        //密码MD5加密存储
        user.setPassword(SecureUtil.md5(user.getPassword()));
        //姓名默认使用用户名
        if (ObjectUtil.isEmpty(user.getName())) {
            user.setName(user.getUsername());
        }
        //判断角色类型
        user.setRole(RoleEnum.USER.name());
        user.setStatus("0");
        user.setCreateTime(DateUtil.today());
        userMapper.insert(user);
    }

    public void deleteById(Integer id) {
        userMapper.deleteById(id);
    }

    /**
     * 封禁/解封用户
     */
    public void ban(Integer id) {
        userMapper.updateStatus(id, "1");
    }

    public void unban(Integer id) {
        userMapper.updateStatus(id, "0");
    }

    //批量删除
    public void deleteBatch(List<Integer> ids) {
        for (Integer id : ids) {
            this.deleteById(id);
        }
    }

    public void updateById(User user) {
        userMapper.updateById(user);
    }

    public User selectById(Integer id) {
        return userMapper.selectById(id);
    }

    /**
     * 公开个人主页信息（博客数/粉丝数/关注数/是否已关注）
     */
    public Map<String, Object> publicInfo(Integer id) {
        User user = selectById(id);
        if (ObjectUtil.isNull(user)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("user", user);
        map.put("blogCount", blogMapper.selectUserBlog(id).size());
        map.put("followers", followMapper.countFollowers(id));
        map.put("following", followMapper.countFollowing(id));
        Account currentUser = TokenUtils.getCurrentUser();
        boolean isFollowing = currentUser.getId() != null
                && followMapper.selectByUserAndFollow(currentUser.getId(), id) != null;
        map.put("isFollowing", isFollowing);
        return map;
    }

    public List<User> selectAll(User user) {
        return userMapper.selectAll(user);
    }

    public PageInfo<User> selectPage(User user, Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        List<User> list = userMapper.selectAll(user);
        return PageInfo.of(list);
    }

    public Account login(Account account) {
        Account dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!SecureUtil.md5(account.getPassword()).equals(dbUser.getPassword())) {
            throw new CustomException(ResultCodeEnum.USER_ACCOUNT_ERROR);
        }
        if ("1".equals(dbUser.getStatus())) {
            throw new CustomException(ResultCodeEnum.USER_BANNED);
        }
        // 生成token
        String tokenData = dbUser.getId() + "-" + RoleEnum.USER.name();
        String token = TokenUtils.createToken(tokenData, dbUser.getPassword());
        dbUser.setToken(token);
        return dbUser;
    }

    /**
     * 注册
     */
    /**
     * 注册
     */
    public void register(Account account) {
        User user = new User();
        BeanUtils.copyProperties(account, user);
        add(user);
    }

    public void updatePassword(Account account) {
        User dbUser = userMapper.selectByUsername(account.getUsername());
        if (ObjectUtil.isNull(dbUser)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if (!SecureUtil.md5(account.getPassword()).equals(dbUser.getPassword())) {
            throw new CustomException(ResultCodeEnum.PARAM_PASSWORD_ERROR);
        }
        dbUser.setPassword(SecureUtil.md5(account.getNewPassword()));
        this.updateById(dbUser);
    }
}