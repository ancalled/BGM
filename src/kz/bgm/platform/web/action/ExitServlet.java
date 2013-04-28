package kz.bgm.platform.web.action;

import kz.bgm.platform.model.domain.User;
import kz.bgm.platform.web.action.UploadReportMobileServlet;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ExitServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(ExitServlet.class);

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User user = (User) req.getSession().getAttribute("user");

        log.info(user.getLogin()+" is logout");

        req.getSession().setAttribute("user", null);

        resp.sendRedirect("/login.html");
    }
}
