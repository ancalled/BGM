package kz.bgm.platform.web;

import kz.bgm.platform.items.Track;
import kz.bgm.platform.service.CatalogFactory;
import kz.bgm.platform.service.CatalogStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class FindServlet extends HttpServlet {

    public static final String TRACK_LIST = "track-list";
    public static final String QUERY = "query";

    public static CatalogStorage catalogService;

    @Override
    public void init() throws ServletException {
        catalogService = CatalogFactory.getStorage();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String find = req.getParameter("find");
        String type = req.getParameter(FindTrackServletJson.FIND_TYPE);

        if (find != null && !"".equals(find) && type != null) {

            List<Track> found;
            if (type.equals("like")) {
                found = catalogService.searchByArtistLike(find);

            } else if (type.equals("equals")) {
                found = catalogService.searchByArtist(find);

            } else {
                found = catalogService.search(find);
            }

            req.setAttribute(TRACK_LIST, found);
            req.setAttribute(QUERY, find);
            req.getRequestDispatcher("/find.jsp").forward(req, resp);

        } else {
            req.setAttribute(QUERY, find);
            req.getRequestDispatcher("/find.jsp").forward(req, resp);
        }
    }

}
