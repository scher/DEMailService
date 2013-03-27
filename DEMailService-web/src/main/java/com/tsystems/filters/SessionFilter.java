package com.tsystems.filters;

import javax.faces.application.ResourceHandler;
import javax.faces.context.FacesContext;
import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: apronin
 * Date: 19.03.13
 * Time: 11:10
 * To change this template use File | Settings | File Templates.
 */
@WebFilter(servletNames = {"Faces Servlet"})
public class SessionFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String loginUrl = req.getContextPath() + "/login.jsf";
        String regUrl = req.getContextPath() + "/registration.jsf";
        String mailUrl = req.getContextPath() + "/mailbox.jsf";
        String ctxPath = req.getContextPath() + "/";

        HttpSession session = req.getSession(false);

        String userLogin = (session != null) ? (String) session.getAttribute("userLogin") : null;

        if (userLogin == null) {
            if ((req.getRequestURI().equals(mailUrl)) || (req.getRequestURI().equals(ctxPath))) {
                ((HttpServletResponse) response).sendRedirect(loginUrl);
                return;
            }
        } else {
            //login != null
            if ((req.getRequestURI().equals(regUrl)) || (req.getRequestURI().equals(loginUrl))) {
                ((HttpServletResponse) response).sendRedirect(mailUrl);
                return;
            }
        }
        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }
}
