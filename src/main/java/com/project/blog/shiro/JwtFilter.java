package com.project.blog.shiro;

import cn.hutool.json.JSONUtil;
import com.project.blog.lang.Result;
import com.project.blog.service.UserService;
import io.jsonwebtoken.Claims;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.ExpiredCredentialsException;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.AuthenticatingFilter;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtFilter extends AuthenticatingFilter {

    @Override
    //合成令牌
    protected AuthenticationToken createToken(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        //这个方法是用来合成我们需要的swtToken的，如果没有获取到说明没有就返回空

//        System.out.println("AuthenticationToken>>");

        //获取在请求头中的jwt
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt =  request.getHeader("Authorization");
        if (StringUtils.isEmpty(jwt)){
            //jwt为空，返回空
            return null;
        }
        //不为空则合成jwtToken并返回
        return new JwtToken(jwt);
    }

    @Autowired
    JwtUtils jwtUtils;

    @Override
    //是否自动登录
    protected boolean onAccessDenied(ServletRequest servletRequest, ServletResponse servletResponse) throws Exception {
        //拦截校验，当头部没有Authorization的时候我们通过，当有的时候，我们首先校验有效性，没问题就可以进行executeLogin方法自动登录
        //获取在请求头中的jwt
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        String jwt =  request.getHeader("Authorization");
        if (StringUtils.isEmpty(jwt)){
            //jwt为空，直接通过到控制器
            return true;
        }else {
            //jwt不为空，进行校验
            Claims claims = jwtUtils.getClaimByToken(jwt);
            if (claims==null || jwtUtils.isTokenExpired(claims.getExpiration())){
                //为空说明jwt是异常的，后面的是过期
                //如果发生这两种情况，则抛出异常
                throw new ExpiredCredentialsException("token已失效，请重新登录");
            }
            //校验通过则执行登录
            return executeLogin(servletRequest, servletResponse);
            //如果登录出现异常时，按照方法体说明，会执行onLoginFailure()方法，我们最好重写此方法来让异常处理统一规范
        }
    }

    @Override
    //登录异常格式规范
    protected boolean onLoginFailure(AuthenticationToken token, AuthenticationException e, ServletRequest request, ServletResponse response) {

//        System.out.println("onLoginFailure>>");

        //执行登录时如果出现异常
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;
        //获取异常，返回给throwable
        Throwable throwable = e.getCause() == null? e:e.getCause();
        //用Result的错误函数fail返回Result格式的错误
        Result result = Result.fail(throwable.getMessage());
        //因为最后需要用json来返回到页面，所以我们转换为json格式
        String json = JSONUtil.toJsonStr(result);
        //通过httpServletResponse.getWriter()来将json传递给页面
        try {
            httpServletResponse.getWriter().print(json);
        } catch (IOException ex) {

        }
        return false;
    }

    @Autowired
    private UserService userService;
    @Override
    //在登录成功时拦截
    protected boolean onLoginSuccess(AuthenticationToken token, Subject subject, ServletRequest request, ServletResponse response) throws Exception {
        //我们需要在此时进行对管理员的辨别
        JwtToken jwt = (JwtToken) token;
        HttpServletRequest servletRequest = (HttpServletRequest) request;
        StringBuffer url = servletRequest.getRequestURL();
        if (url.toString().contains("admin")){
            String userId = jwtUtils.getClaimByToken((String) jwt.getPrincipal()).getSubject();
            if (userService.getById(userId).getStatus()!=1){
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                //用Result的错误函数fail返回Result格式的错误
                Result result = Result.fail("do not have permission");
                //因为最后需要用json来返回到页面，所以我们转换为json格式
                String json = JSONUtil.toJsonStr(result);
                //通过httpServletResponse.getWriter()来将json传递给页面
                try {
                    httpServletResponse.getWriter().print(json);
                } catch (IOException ex) {

                }
                return false;
            }
        }

        return super.onLoginSuccess(token, subject, request, response);
    }

    @Override
    //跨域处理
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {

//        System.out.println("preHandle>>");

        HttpServletRequest httpServletRequest = WebUtils.toHttp(request);
        HttpServletResponse httpServletResponse = WebUtils.toHttp(response);
        //同意携带证书
        httpServletResponse.setHeader("Access-Control-Allow-Credentials","true");
        httpServletResponse.setHeader("Access-control-Allow-Origin", httpServletRequest.getHeader("Origin"));
        httpServletResponse.setHeader("Access-Control-Allow-Methods", "GET,POST,OPTIONS,PUT,DELETE,PATCH");
        httpServletResponse.setHeader("Access-Control-Allow-Headers", httpServletRequest.getHeader("Access-Control-Request-Headers"));
        // 复杂请求跨域时会首先发送一个OPTIONS请求，这里我们给OPTIONS请求直接返回正常状态
        if (httpServletRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            httpServletResponse.setStatus(org.springframework.http.HttpStatus.OK.value());
            return false;
        }
        return super.preHandle(request, response);
    }
}