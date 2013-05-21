package kz.bgm.platform.web.customer.action;


import kz.bgm.platform.model.service.TrackBasket;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class PutToBasketServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        TrackBasket basket = (TrackBasket) session.getAttribute("basket");

        if (basket != null) {
            List<Long> idList = getBasketAttrs(req);
            basket.addTrack(idList);
        }

        resp.sendRedirect("/customer/view/search-result");

    }

    private List<Long> getBasketAttrs(HttpServletRequest req) {

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
