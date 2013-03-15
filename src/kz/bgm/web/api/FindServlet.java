package kz.bgm.web.api;

import kz.bgm.CatalogStorage;
import kz.bgm.items.Track;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class FindServlet extends HttpServlet {

    public static final String TRACK_LIST = "track-list";
    public static CatalogStorage catalogService;

    @Override
    public void init() throws ServletException {
        catalogService = new CatalogStorage(FindTrackServletJson.DB_PROPS);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String find = req.getParameter("find");
        String type = req.getParameter(FindTrackServletJson.FIND_TYPE);
        if (find != null && !"".equals(find) && type != null) {

            List<Track> foundTracks;
            if (type.equals("like")) {
                foundTracks = catalogService.getTracksLikeArtist(find);
            } else if (type.equals("equals")) {
                foundTracks = catalogService.getTracksByArtist(find);
            } else {
                foundTracks = catalogService.getTracksLikeAllNames(find);
            }

            req.getSession().setAttribute(TRACK_LIST, foundTracks);
            resp.sendRedirect("/find.jsp");
        } else {
            resp.sendRedirect("/find.jsp");
        }
    }

}
