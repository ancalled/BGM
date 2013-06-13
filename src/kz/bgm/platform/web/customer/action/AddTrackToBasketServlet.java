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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class AddTrackToBasketServlet extends HttpServlet {

    CatalogStorage storage;
    private static final Logger log = Logger.getLogger(AddTrackToBasketServlet.class);

    @Override
    public void init() throws ServletException {
        storage = CatalogFactory.getStorage();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();

        User user = (User) session.getAttribute("user");

        if (user != null) {

            log.info("Add tracks to basket \n" +
                    "    login :" + user.getLogin() + "\n" +
                    "    id    :" + user.getId());

            List<Long> tracks = getTrackIds(req);

            for (Long trackId : tracks) {
                storage.addItemToBasket(user.getCustomerId(), trackId);
            }

        } else {
            log.warn("User not found");
        }

        resp.sendRedirect("/customer/view/basket");

    }

    private List<Long> getTrackIds(HttpServletRequest req) {
        Enumeration<String> names = req.getParameterNames();
        List<Long> res = new ArrayList<>();
        while (names.hasMoreElements()) {
            String elName = names.nextElement();
            String strId = req.getParameter(elName);
            if (strId != null) {
                res.add(Long.parseLong(strId));
            }
        }
        return res;
    }
}
