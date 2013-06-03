package kz.bgm.platform.web.customer.action;


import kz.bgm.platform.model.domain.User;
import kz.bgm.platform.model.domain.UserCatalogItem;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PutToUserCatalogServlet extends HttpServlet {

    CatalogStorage storage;
    private static final Logger log = Logger.getLogger(PutToUserCatalogServlet.class);

    @Override
    public void init() throws ServletException {
        storage = CatalogFactory.getStorage();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();

        User user = (User) session.getAttribute("user");

        if (user != null) {


            log.info("Adding tracks in user catalog of user \n" +
                    "    login :" + user.getLogin() + "\n" +
                    "    id    :" + user.getId());

            List<Long> idList = getTrackIdList(req);

            for (Long id : idList) {
                UserCatalogItem item = new UserCatalogItem();

                log.info(id);

                item.setTrackId(id);
                item.setUserId(user.getId());

                storage.saveUserCatalogItem(item);
            }

        } else {
            log.warn("User not found");
        }

        resp.sendRedirect("/customer/view/search-result");

    }

    private List<Long> getTrackIdList(HttpServletRequest req) {
        Enumeration<String> names = req.getParameterNames();
        List<Long> idList = new ArrayList<>();
        while (names.hasMoreElements()) {
            String elName = names.nextElement();
            String strId = (String) req.getParameter(elName);

            if (strId != null) {
                Long id = Long.parseLong(strId);
                idList.add(id);
            }
        }
        return idList;
    }
}
