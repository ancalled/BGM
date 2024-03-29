package kz.bgm.platform.web.admin.action;

import kz.bgm.platform.model.domain.AdminUser;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(LogoutServlet.class);


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        AdminUser user = (AdminUser) req.getSession().getAttribute("admin");

        if (user != null) {
            req.getSession().setAttribute("admin", null);
            log.info(user.getLogin() + " logged out");
        }

        resp.sendRedirect("/admin-login.html");
    }
}

