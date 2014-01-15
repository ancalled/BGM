package kz.bgm.platform.web.admin.action;


import kz.bgm.platform.model.domain.Catalog;
import kz.bgm.platform.model.domain.RightType;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreateCatalogServlet extends HttpServlet {
    private static final Logger log = Logger.getLogger(CreateCatalogServlet.class);
    private CatalogStorage storage;

    @Override
    public void init() throws ServletException {
        storage = CatalogFactory.getStorage();
    }

    @Override
    protected void doPost(HttpServletRequest req,
                          HttpServletResponse resp)
            throws ServletException, IOException {

        String name = req.getParameter("name");
        String royalStr = req.getParameter("royal");
        String rights = req.getParameter("rights");
        String platformIdStr = req.getParameter("platformId");

        if (isParamEmpty(name) || isParamEmpty(royalStr) ||
                isParamEmpty(rights) || isParamEmpty(platformIdStr)) {
            resp.sendRedirect("/admin/view/add-catalog?err=not_all_params");
        } else {

            float royal = Float.parseFloat(royalStr);
            long platformId = Long.parseLong(platformIdStr);

            RightType rightType = null;
            if (rights.equalsIgnoreCase("AUTHOR")) {
                rightType = RightType.AUTHOR;
            } else if (rights.equalsIgnoreCase("RELATED")) {
                rightType = RightType.RELATED;
            } else if (rights.equalsIgnoreCase("AUTHOR_RELATED")) {
                rightType = RightType.AUTHOR_RELATED;
            }


            Catalog catalog = new Catalog();

            catalog.setName(name);
            catalog.setRoyalty(royal);
            catalog.setRightType(rightType);
            catalog.setPlatformId(platformId);

            storage.createCatalog(catalog);
            resp.sendRedirect("/admin/view/index");
        }

    }

    private boolean isParamEmpty(String param) {
        return param == null && "".equals(param);
    }

}
