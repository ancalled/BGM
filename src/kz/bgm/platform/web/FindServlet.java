package kz.bgm.platform.web;

import kz.bgm.platform.items.Track;
import kz.bgm.platform.service.CatalogFactory;
import kz.bgm.platform.service.CatalogStorage;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


public class FindServlet extends HttpServlet {

    public static final String TRACK_LIST = "track-list";
    public static final String QUERY = "query";

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        CatalogStorage catalogService = CatalogFactory.getStorage();

        String find = req.getParameter("find");
        String type = req.getParameter(FindTrackServletJson.FIND_TYPE);

        HttpSession session = req.getSession();
        if (find != null && !"".equals(find) && type != null) {

            List<Track> found;
            if (type.equals("like")) {
                found = catalogService.searchByArtistLike(find);

            } else if (type.equals("equals")) {
                found = catalogService.searchByArtist(find);

            } else {
                found = catalogService.search(find);
            }


            session.setAttribute(TRACK_LIST, found);


        }

        session.setAttribute(QUERY, find);
        resp.sendRedirect("/search.jsp?q=" + find);
    }

}
