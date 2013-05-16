package kz.bgm.platform.web.admin.action;


import kz.bgm.platform.model.domain.AdminUser;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    private CatalogStorage catalogStorage;
    private static final Logger log = Logger.getLogger(kz.bgm.platform.web.customer.action.LoginServlet.class);

    @Override
    public void init() throws ServletException {
        catalogStorage = CatalogFactory.getStorage();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String login = req.getParameter("u");
        String pass = req.getParameter("p");

        log.info("Admin authorization");

        if (login != null && pass != null) {
            AdminUser user = catalogStorage.getAdmin(login, pass);
            if (user != null) {
                HttpSession session = req.getSession();
                session.setAttribute("user", user);

                log.info("Admin authorized login : " + login);

            } else {
                log.info("Admin user '" + login + "' was not found or pass incorrect");
                resp.sendRedirect("/admin-login.html?er=no-user-found");
                return;
            }
        }
        resp.sendRedirect("/admin/view/index");
    }


}
