package kz.bgm.platform.web.action;


import kz.bgm.platform.model.domain.Admin;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginAdminServlet extends HttpServlet {

    private CatalogStorage catalogStorage;
    private static final Logger log = Logger.getLogger(LoginCustomerServlet.class);

    @Override
    public void init() throws ServletException {
        catalogStorage = CatalogFactory.getStorage();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String login = req.getParameter("u");
        String pass = req.getParameter("p");

        log.info("Admin authorization ");

        if (login != null && pass != null) {
            Admin admin = catalogStorage.getAdmin(login, pass);
            if (admin != null && admin.getId() > 0) {
                HttpSession session = req.getSession();
                session.setAttribute("admin", admin);

                log.info("Admin authorized");
                log.info("login : " + login);

            } else {
                log.info("user-admin '" + login + "' was not found or pass incorrect");
                resp.sendRedirect("/login-admin.html?er=no-user-found");
                return;
            }
        }
        resp.sendRedirect("/main.html");
    }


}
