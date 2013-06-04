package kz.bgm.platform.web.customer.action;


import kz.bgm.platform.model.domain.User;
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

    private CatalogStorage service;
    private static final Logger log = Logger.getLogger(LoginServlet.class);

    @Override
    public void init() throws ServletException {
        service = CatalogFactory.getStorage();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String login = req.getParameter("u");
        String pass = req.getParameter("p");

        log.info("Authorization request");


        if (login != null && pass != null) {
            User user = service.getUser(login, pass);
            if (user != null) {
                HttpSession session = req.getSession();
                session.setAttribute("user", user);

                log.info("User authorized");
                log.info("login:         " + login);
                log.info("customer ID:   " + user.getCustomerId());

            } else {
                log.info("user '" + login + "' was not found or pass incorrect");
                resp.sendRedirect("/customer-login.html?er=no-user-found");
                return;
            }
        }
        resp.sendRedirect("/customer/view/index");
    }

}
