package kz.bgm.platform.web.action;

import kz.bgm.platform.model.domain.Track;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import kz.bgm.platform.web.api.FindTrackServletJson;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class SearchTrackServlet extends HttpServlet {

    public static final String TRACK_LIST = "tracks";
    public static final String QUERY = "query";
    public static final String SIZE = "size";
    private CatalogStorage catalogService;
    private static final Logger log = Logger.getLogger(SearchTrackServlet.class);

    @Override
    public void init() throws ServletException {
        catalogService = CatalogFactory.getStorage();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {


        String query = req.getParameter("find");
        String type = req.getParameter(FindTrackServletJson.FIND_TYPE);

        log.info("Got query " + query + " find by " + type);

        if (query != null && !"".equals(query) && type != null) {

            List<Track> found;
            if (type.equals("like-artist")) {
                found = catalogService.searchByArtistLike(query);

            } else if (type.equals("artist")) {
                found = catalogService.searchByArtist(query);
            } else if (type.equals("code")) {
                found = catalogService.searchByCode(query);
            } else if (type.equals("song")) {
                found = catalogService.searchBySongName(query);
            } else if (type.equals("composer")) {
                found = catalogService.searchByComposer(query);
            } else {
                found = catalogService.search(query, true);
            }

            req.setAttribute(TRACK_LIST, found);
            req.setAttribute(SIZE,found.size());
            req.setAttribute(QUERY, query);

            req.getRequestDispatcher("/search.jsp?q=" + query).forward(req, resp);

        }
    }

}
