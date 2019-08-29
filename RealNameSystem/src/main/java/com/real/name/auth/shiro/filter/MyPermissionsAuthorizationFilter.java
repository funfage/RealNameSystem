package com.real.name.auth.shiro.filter;

import com.alibaba.fastjson.JSONObject;
import com.real.name.auth.service.AuthUtils;
import com.real.name.common.result.ResultError;
import com.real.name.common.result.ResultVo;
import org.apache.shiro.web.filter.authz.PermissionsAuthorizationFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class MyPermissionsAuthorizationFilter extends PermissionsAuthorizationFilter {

    private Logger logger = LoggerFactory.getLogger(MyPermissionsAuthorizationFilter.class);

    @Override
    protected boolean preHandle(ServletRequest request, ServletResponse response) throws Exception {
        HttpServletResponse httpResponse = (HttpServletResponse) response;
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        if (httpRequest.getMethod().equals(RequestMethod.OPTIONS.name())) {
            AuthUtils.setHeader(httpRequest,httpResponse);
            return true;
        }
        return super.preHandle(request, response);
    }

    @Override
    protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws IOException {
        AuthUtils.setHeader((HttpServletRequest) request, (HttpServletResponse) response);
        PrintWriter out = response.getWriter();
        if (getSubject(request,response).getPrincipal() == null) {
            logger.error("接口访问的路径为:{}, 用户未登录", ((HttpServletRequest) request).getRequestURI());
            out.write(JSONObject.toJSONString(ResultVo.failure(ResultError.USER_UN_LOGIN)));
        }else {
            logger.error("接口访问的路径为:{}, 没有访问权限", ((HttpServletRequest) request).getRequestURI());
            out.write(JSONObject.toJSONString(ResultVo.failure(ResultError.USER_UN_AUTHORIZED)));
        }
        return false;
    }

}
