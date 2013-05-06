package kz.bgm.platform.web.admin.action;

import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteUserServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(DeleteUserServlet.class);
    private CatalogStorage storage;

    @Override
    public void init() throws ServletException {
        storage = CatalogFactory.getStorage();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String strUserId = req.getParameter("user-id");
        String strCid = req.getParameter("cid");

        log.info("Delete user request");
        log.info("Params\n" +
                "User id      :" + strUserId + "\n" +
                "Customer id  :" + strCid);

        if (strUserId == null) {
            log.warn("user id is null");
            resp.sendRedirect("../admin/view/customer-detail?err=1&customer_id=" + strCid);
            return;          //todo think about errors
        }

        long userId = Long.parseLong(strUserId);
        storage.removeUser(userId);
        resp.sendRedirect("../admin/view/customer-detail?customer_id=" + strCid);
    }


}
