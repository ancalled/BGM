package kz.bgm.platform.web;


import kz.bgm.platform.items.User;
import kz.bgm.platform.service.CatalogFactory;
import kz.bgm.platform.service.CatalogStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginServlet extends HttpServlet {

    private CatalogStorage service;

    @Override
    public void init() throws ServletException {
        service = CatalogFactory.getStorage();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String login = req.getParameter("u");
        String pass = req.getParameter("p");

        if (login != null && pass != null) {
            User user = checkUserInBase(login, pass);
            if (user != null) {
                HttpSession session = req.getSession();
                session.setAttribute("user", user);
            }

        }
        resp.sendRedirect("/index.html");
    }

    private User checkUserInBase(String userName, String pass) {
        return service.getUser(userName, pass);
    }
}
