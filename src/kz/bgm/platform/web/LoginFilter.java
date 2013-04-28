package kz.bgm.platform.web;

import kz.bgm.platform.model.domain.User;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginFilter implements Filter {

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
        HttpSession session = req.getSession();

        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        User user = (User) session.getAttribute("user");

        if (user == null) {
            resp.sendRedirect("/login.html");
        } else {
            filterChain.doFilter(servletRequest, servletResponse);
        }


    }

    @Override
    public void destroy() {
    }
}
