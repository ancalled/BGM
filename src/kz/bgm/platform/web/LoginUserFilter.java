package kz.bgm.platform.web;

import kz.bgm.platform.model.domain.Admin;
import kz.bgm.platform.model.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginUserFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest,
                         ServletResponse servletResponse,
                         FilterChain filterChain)
            throws IOException, ServletException {


        //todo make role check and role reaction admin and all users.

        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        HttpSession ses = req.getSession(false);

        if (ses != null) {
            User user = (User) ses.getAttribute("user");
            Admin admin = (Admin) ses.getAttribute("admin");

            if (user != null || admin != null) {
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
