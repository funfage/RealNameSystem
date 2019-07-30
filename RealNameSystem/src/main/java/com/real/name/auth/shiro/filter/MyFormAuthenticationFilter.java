package com.real.name.auth.shiro.filter;

import com.alibaba.fastjson.JSONObject;
import com.real.name.auth.entity.User;
import com.real.name.auth.service.AuthUtils;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.util.UUID;

public class MyFormAuthenticationFilter extends FormAuthenticationFilter {

    /**
     * 跨域配置
     */
    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (httpRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            AuthUtils.setHeader(httpRequest,httpResponse);
            return true;
        }
        return super.preHandle(request,response);
    }


    /**
     * 让Shiro对OPTIONS Request不进行鉴权操作
     */
    @Override
    protected boolean isAccessAllowed(ServletRequest request, ServletResponse response, Object mappedValue) {
        if (request instanceof HttpServletRequest) {
            if (((HttpServletRequest) request).getMethod().toUpperCase().equals("OPTIONS")) {
                return true;
            }
        }
        return super.isAccessAllowed(request, response, mappedValue);
//        return SecurityUtils.getSubject() != null;
    }



    /**
     * 用户登录过期或未登录
     */
    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response)
            throws Exception {
        saveRequest(request);
        AuthUtils.setHeader((HttpServletRequest) request, (HttpServletResponse) response);
        PrintWriter out = response.getWriter();
        //生成一组16位的随机数作为盐值
        int hashcodeV = UUID.randomUUID().hashCode();
        if(hashcodeV < 0){
            hashcodeV = -hashcodeV;
        }
        String uuidSalt = String.format("%016d", hashcodeV);
        //把uuid的盐值，同时保存到前后端中
        SecurityUtils.getSubject().getSession().setAttribute("uuidSalt", uuidSalt);
        out.write(JSONObject.toJSONString(ResultVo.failure(uuidSalt, ResultError.USER_UN_LOGIN)));
        return false;
    }

}




















