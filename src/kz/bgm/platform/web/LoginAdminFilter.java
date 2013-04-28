package kz.bgm.platform.web;

import kz.bgm.platform.model.domain.Admin;
import kz.bgm.platform.model.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginAdminFilter implements Filter {


    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain) throws IOException, ServletException {


        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;
        HttpSession ses = req.getSession(false);

        if (ses != null) {
            Admin admin = (Admin) ses.getAttribute("admin");
            User user = (User) ses.getAttribute("user");

            if (admin != null || user != null) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }
        resp.sendRedirect("/index.html");

    }

    @Override
    public void destroy() {

    }
}
