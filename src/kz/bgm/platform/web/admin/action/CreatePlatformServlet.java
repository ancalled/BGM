package kz.bgm.platform.web.admin.action;


import kz.bgm.platform.model.domain.Platform;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CreatePlatformServlet extends HttpServlet{

    private static final Logger log = Logger.getLogger(CreatePlatformServlet.class);
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

        if(name!=null ||!"".equals(name)){

            Platform platform= new Platform();

            platform.setName(name);
            platform.setRights(true);

            storage.createPlatform(platform);

            resp.sendRedirect("/admin/view/index");
        }else {
            resp.sendRedirect("/admin/view/add-platform?error=name_is_empty");
        }
    }
}
