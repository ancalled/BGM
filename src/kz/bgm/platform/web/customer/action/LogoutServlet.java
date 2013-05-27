package kz.bgm.platform.web.customer.action;

import kz.bgm.platform.model.domain.User;
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

        User user = (User) req.getSession().getAttribute("user");
        if (user != null) {
            req.getSession().setAttribute("user", null);
            req.getSession().setAttribute("basket", null);
            log.info(user.getLogin() + " logged out");
        }
        resp.sendRedirect("/customer/admin-customer-login.html");
    }
}

