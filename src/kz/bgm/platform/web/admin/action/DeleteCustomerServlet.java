package kz.bgm.platform.web.admin.action;


import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteCustomerServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(DeleteCustomerServlet.class);
    private CatalogStorage storage;

    @Override
    public void init() throws ServletException {
        storage = CatalogFactory.getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String StrCustomerId = req.getParameter("customer-id");

        log.info("Delete customer request");
        log.info("Params\n" +
                "Customer id      :" + StrCustomerId);

        if (StrCustomerId == null) {
            log.warn("user id is null");
            resp.sendRedirect("/admin/view/customers");
            return;          //todo think about errors
        }

        long id = Long.parseLong(StrCustomerId);
        storage.removeCustomer(id);
        resp.sendRedirect("/admin/view/customers");
    }
}
