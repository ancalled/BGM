package kz.bgm.platform.web.customer.action;


import kz.bgm.platform.model.domain.Track;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
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

    CatalogStorage storage;

    @Override
    public void init() throws ServletException {
        storage = CatalogFactory.getStorage();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        HttpSession session = req.getSession();
        TrackBasket basket = (TrackBasket) session.getAttribute("basket");

        if (basket != null) {
            List<Track> idList = getTracks(req);
            basket.addTrack(idList);
        }

        resp.sendRedirect("/customer/view/search-result");

    }

    private List<Track> getTracks(HttpServletRequest req) {
        Enumeration<String> names = req.getParameterNames();
        List<Track> trackList = new ArrayList<>();
        while (names.hasMoreElements()) {
            String elName = names.nextElement();
            String strId = (String) req.getParameter(elName);

            if (strId != null) {
                Long id = Long.parseLong(strId);
                Track track = storage.getTrack(id);

                if (track != null) {
                    trackList.add(track);
                }
            }
        }
        return trackList;
    }
}
