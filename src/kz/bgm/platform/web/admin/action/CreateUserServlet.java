package kz.bgm.platform.web.admin.action;


import kz.bgm.platform.model.domain.User;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class CreateUserServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(CreateUserServlet.class);
    private CatalogStorage storage;

    @Override
    public void init() throws ServletException {
        storage = CatalogFactory.getStorage();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String name = req.getParameter("login");
        String pass = req.getParameter("pass");
        String strCustomerId = req.getParameter("customer-id");

        log.info("Creating user request");

        if (name == null || pass == null || strCustomerId == null) {
            resp.sendRedirect("create-user-form.html");
            log.warn("Some params are empty");
            return;
        }


        Long custId = Long.parseLong(strCustomerId);

        User user = new User();
        user.setLogin(name);
        user.setPass(pass);
        user.setCustomerId(custId);
        long userId = storage.createUser(user);

        if (userId > 0) {
            log.info("New user created with:\n" +
                    "login         :    " + user.getLogin() + "\n" +
                    "id            :    " + userId + "\n" +
                    "customer-id   :    " + user.getCustomerId());

            resp.sendRedirect("/admin/view/customer-detail?customer_id=" + strCustomerId);
        } else {
            resp.sendRedirect("/admin/view/customer-detail?customer_id=" + user.getCustomerId() + "&err=user not created");
        }
    }


}
