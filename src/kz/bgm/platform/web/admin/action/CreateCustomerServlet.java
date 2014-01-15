package kz.bgm.platform.web.admin.action;


import kz.bgm.platform.model.domain.Customer;
import kz.bgm.platform.model.domain.CustomerType;
import kz.bgm.platform.model.domain.RightType;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;

public class CreateCustomerServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(CreateCustomerServlet.class);
    private CatalogStorage storage;

    @Override
    public void init() throws ServletException {
        storage = CatalogFactory.getStorage();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {


        if (hasNullParameter(req)) {
            resp.sendRedirect("/admin/view/customers");
        }
        req.setCharacterEncoding("UTF-8");

        String name = req.getParameter("name");
        String right = req.getParameter("rights");
        String authorRoyaltyStr = req.getParameter("authorRoyalty");
        String relatedRoyaltyStr = req.getParameter("relatedRoyalty");
        String contract = req.getParameter("contract");
        String companyType = req.getParameter("type");

        authorRoyaltyStr = authorRoyaltyStr.replace(",", ".");

        log.info("Got request to create customer " + name);


        CustomerType type;
        if (companyType.equalsIgnoreCase("MOBILE_AGGREGATOR")) {
            type = CustomerType.MOBILE_AGGREGATOR;
        } else if (companyType.equalsIgnoreCase("PUBLIC_RIGHTS_SOCIETY")) {
            type = CustomerType.PUBLIC_RIGHTS_SOCIETY;
        } else {
            type = CustomerType.NONE;
        }


        Customer customer = new Customer();
        customer.setContract(contract);
        customer.setName(name);
        customer.setRightType(RightType.valueOf(right));
        customer.setAuthorRoyalty(Float.parseFloat(authorRoyaltyStr));
        customer.setAuthorRoyalty(Float.parseFloat(relatedRoyaltyStr));
        customer.setCustomerType(type);

        long id = storage.createCustomer(customer);

        if (id == -1L) {
            log.info("Customer cud not created");
        } else {
            log.info("Customer created id: " + id);
        }

        resp.sendRedirect("/admin/view/customers");
    }

    private boolean hasNullParameter(HttpServletRequest req) {
        Enumeration<String> pars = req.getParameterNames();

        while (pars.hasMoreElements()) {
            String pName = pars.nextElement();
            String val = req.getParameter(pName);
            if (val == null) {
                return true;
            }
        }
        return false;
    }
}
