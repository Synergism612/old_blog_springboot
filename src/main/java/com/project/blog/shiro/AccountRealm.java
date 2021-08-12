package com.project.blog.shiro;

import cn.hutool.core.bean.BeanUtil;
import com.project.blog.entity.User;
import com.project.blog.service.UserService;
import org.apache.shiro.authc.*;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
@Component
public class AccountRealm extends AuthorizingRealm {

    @Override
    public boolean supports(AuthenticationToken token) {
        return token instanceof JwtToken;//传进来的是否是JwtToken，不是返回flase
    }

    //权限校验
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
        return null;
    }

    //导入工具类
    @Autowired
    JwtUtils jwtUtils;
    //用户服务类
    @Autowired
    UserService userService;

    //登录校验
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken Token) throws AuthenticationException {
        JwtToken jwt = (JwtToken) Token;
        //jwtUtils.getClaimByToken()会返回一个Claims，一般的userId放在SUBJECT中
        String userId = jwtUtils.getClaimByToken((String) jwt.getPrincipal()).getSubject();
        //通过userId(转换为Long)来找到用户
        User user = userService.getById(Long.valueOf(userId));
        //为null说明用户不存在
        if (user == null){
            throw new UnknownAccountException("不存在该用户");
        }
        //获取用户状态码，如果为-1说明被锁定
        if (user.getStatus() == -1){
            throw new LockedAccountException("用户已被锁定");
        }
        //不能直接把全部信息交给shiro，我们需要进行一步封装，只将基本信息给shiro
        AccountProfile profile = new AccountProfile();
        //将user中的部分数据copy到profile中
        BeanUtil.copyProperties(user, profile);
        //SimpleAuthenticationInfo所需要的参数，principal身份信息 credentials凭证信息 realmName领域名称
        return new SimpleAuthenticationInfo(profile,jwt.getCredentials(),getName());
    }
}