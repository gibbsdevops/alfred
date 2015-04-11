package com.gibbsdevops.alfred.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class RequestLogger implements HandlerInterceptor {

    private static final Logger LOG = LoggerFactory.getLogger(RequestLogger.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        StringBuffer uri = new StringBuffer();
        uri.append(request.getScheme()).append("://");
        uri.append(request.getServerName()).append(":");
        uri.append(request.getServerPort());
        uri.append(request.getRequestURI());

        LOG.info("{} {}", request.getMethod(), uri.toString());

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
