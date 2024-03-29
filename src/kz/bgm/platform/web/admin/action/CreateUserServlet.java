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
        req.setCharacterEncoding("UTF-8");

        String name = req.getParameter("login");
        String pass = req.getParameter("pass");
        String fullName = req.getParameter("full_name");
        String email = req.getParameter("email");
        String strCustomerId = req.getParameter("customer-id");

        log.info("Creating user request");

        if (name == null || pass == null || strCustomerId == null) {
            resp.sendRedirect("../create-user-form.jsp");
            log.warn("Some params are empty");
            return;
        }

        User oldUser = storage.getUser(name);

        if (oldUser != null) {
            resp.sendRedirect("../create-user-form.jsp?err=101"
                    + strCustomerId);

            return;
        }

        Long custId = Long.parseLong(strCustomerId);
        User user = new User();
        user.setLogin(name);
        user.setPass(pass);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setCustomerId(custId);
        long userId = storage.createUser(user);

        if (userId > 0) {
            log.info("New user created with:\n" +
                    "login         :    " + user.getLogin() + "\n" +
                    "name          :    " + user.getFullName() + "\n" +
                    "email         :    " + user.getEmail() + "\n" +
                    "id            :    " + userId + "\n" +
                    "customer-id   :    " + user.getCustomerId());

            resp.sendRedirect("/admin/view/customer-detail?customer_id=" + strCustomerId);
        } else {
            resp.sendRedirect("/admin/view/customer-detail?customer_id=" + strCustomerId + "&err=user not created");
        }
    }


}
