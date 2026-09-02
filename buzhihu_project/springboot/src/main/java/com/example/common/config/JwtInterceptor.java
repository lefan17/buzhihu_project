package com.example.common.config;

import cn.hutool.core.util.ObjectUtil;
import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.common.Constants;
import com.example.common.enums.ResultCodeEnum;
import com.example.common.enums.RoleEnum;
import com.example.entity.Account;
import com.example.exception.CustomException;
import com.example.service.AdminService;
import com.example.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * jwt拦截器
 */
@Component
public class JwtInterceptor implements HandlerInterceptor {

    private static final Logger log = LoggerFactory.getLogger(JwtInterceptor.class);

    @Resource
    private AdminService adminService;

    @Resource
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 1. 从http请求的header中获取token
        String token = request.getHeader(Constants.TOKEN);
        if (ObjectUtil.isEmpty(token)) {
            // 如果没拿到，从参数里再拿一次
            token = request.getParameter(Constants.TOKEN);
        }
        // 2. 开始执行认证
        if (ObjectUtil.isEmpty(token)) {
            throw new CustomException(ResultCodeEnum.TOKEN_INVALID_ERROR);
        }
        Account account = null;
        String role = null;
        try {
            // 解析token获取存储的数据
            String userRole = JWT.decode(token).getAudience().get(0);
            String userId = userRole.split("-")[0];
            role = userRole.split("-")[1];
            // 根据userId查询数据库
            if (RoleEnum.ADMIN.name().equals(role)) {
                account = adminService.selectById(Integer.valueOf(userId));
            }else if (RoleEnum.USER.name().equals(role)) {
                account = userService.selectById(Integer.valueOf(userId));
            }
        } catch (Exception e) {
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }
        if (ObjectUtil.isNull(account)) {
            throw new CustomException(ResultCodeEnum.USER_NOT_EXIST_ERROR);
        }
        if ("1".equals(account.getStatus())) {
            throw new CustomException(ResultCodeEnum.USER_BANNED);
        }
        try {
            // 用户密码加签验证 token
            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(account.getPassword())).build();
            jwtVerifier.verify(token); // 验证token
        } catch (JWTVerificationException e) {
            throw new CustomException(ResultCodeEnum.TOKEN_CHECK_ERROR);
        }
        // 3. 后台管理接口仅允许管理员访问
        if (requireAdmin(request.getRequestURI()) && !RoleEnum.ADMIN.name().equals(role)) {
            throw new CustomException(ResultCodeEnum.NO_AUTH);
        }
        return true;
    }

    /**
     * 判断请求路径是否需要管理员权限
     */
    private boolean requireAdmin(String uri) {
        if (uri.startsWith("/admin/")) {
            return true;
        }
        // user 模块：除本人修改资料(/user/update)外均需管理员
        if (uri.startsWith("/user/") && !uri.startsWith("/user/update")) {
            return true;
        }
        // category / notice 的写操作需管理员
        if (uri.startsWith("/category/") && !uri.startsWith("/category/select")) {
            return true;
        }
        if (uri.startsWith("/notice/") && !uri.startsWith("/notice/select")) {
            return true;
        }
        // activity 的写操作需管理员（阅读数+1 除外）
        if (uri.startsWith("/activity/") && !uri.startsWith("/activity/select") && !uri.contains("/updateReadCount")) {
            return true;
        }
        // 报名记录的管理端分页查询
        if (uri.startsWith("/activitySign/selectPage")) {
            return true;
        }
        // 举报处理与数据看板仅管理员
        if (uri.startsWith("/report/") && (uri.contains("/handle") || uri.contains("/selectPage"))) {
            return true;
        }
        if (uri.startsWith("/dashboard/")) {
            return true;
        }
        // 评论的管理端查询/修改
        if (uri.startsWith("/comment/") && (uri.contains("/selectPage") || uri.contains("/selectAll") || uri.contains("/update"))) {
            return true;
        }
        return false;
    }
}