package kz.bgm.platform.web.admin;

import kz.bgm.platform.model.domain.AdminUser;
import kz.bgm.platform.model.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class AuthFilter implements Filter {


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
            AdminUser adminUser = (AdminUser) ses.getAttribute("admin");

            if (adminUser != null) {
                filterChain.doFilter(servletRequest, servletResponse);
                return;
            }
        }

        resp.sendRedirect("/admin-login.html");

    }

    @Override
    public void destroy() {

    }
}
