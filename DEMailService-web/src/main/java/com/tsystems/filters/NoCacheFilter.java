package com.tsystems.filters;

import javax.faces.application.ResourceHandler;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: apronin
 * Date: 15.03.13
 * Time: 19:38
 * To change this template use File | Settings | File Templates.
 */

//@WebFilter(servletNames={"Faces Servlet"})
//public class NoCacheFilter implements Filter {
//
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
//        HttpServletRequest httpReq = (HttpServletRequest) request;
//        HttpServletResponse httpRes = (HttpServletResponse) response;
//
//        if (!httpReq.getRequestURI().startsWith(httpReq.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER)) {
//            httpRes.setHeader("Cache-Control", "no-cache, no-store, must-revalidate");
//            httpRes.setHeader("Pragma", "no-cache");
//            httpRes.setDateHeader("Expires", 0);
//        }
//
//        chain.doFilter(request, response);
//    }
//
//    @Override
//    public void init(FilterConfig filterConfig) throws ServletException {
//
//    }
//
//    @Override
//    public void destroy() {
//
//    }
//}
