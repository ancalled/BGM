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

public class RemoveFromUserCatalogServlet extends HttpServlet {

    private CatalogStorage service;
    private static final Logger log = Logger.getLogger(RemoveFromUserCatalogServlet.class);

    @Override
    public void init() throws ServletException {
        service = CatalogFactory.getStorage();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String strTrackId = req.getParameter("track_id");
        long trackId = Long.parseLong(strTrackId);

        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");

        if (user != null) {


            log.info("\nRemoving track " + trackId + " from user catalog \n" +
                    "Owner of catalog \n" +
                    "user id    : " + user.getId() + "\n" +
                    "user login : " + user.getLogin());

            service.removeTrackFromUserCatalog(trackId,user.getId());

            log.info("track removed");
            resp.sendRedirect("/customer/view/user-catalog");
        }
    }
}
