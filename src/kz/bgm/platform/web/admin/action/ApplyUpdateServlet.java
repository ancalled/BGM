package kz.bgm.platform.web.admin.action;

import kz.bgm.platform.model.domain.CatalogUpdate;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class ApplyUpdateServlet extends HttpServlet {


    public static final String VIEWS_HOME_URL = "../view";
    public static final String RESULT_URL = VIEWS_HOME_URL + "/catalog-update";


    private static final Logger log = Logger.getLogger(ApplyUpdateServlet.class);

    private CatalogStorage storage;

    @Override
    public void init() throws ServletException {
        storage = CatalogFactory.getStorage();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        log.info("Applying catalog updates");

        storage.applyCatalogUpdate();

        log.info("Applied.");

        HttpSession ses = req.getSession();
        CatalogUpdate upd = (CatalogUpdate) ses.getAttribute("catalog-update");
        if (upd != null) {
            upd.setApplied(true);
        }

        resp.sendRedirect(RESULT_URL);
    }


}


