package kz.bgm.platform.web.action;

import kz.bgm.platform.model.domain.Track;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import kz.bgm.platform.model.service.LuceneSearch;
import kz.bgm.platform.web.api.FindTrackServletJson;
import org.apache.log4j.Logger;
import org.apache.lucene.queryparser.classic.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public class SearchTrackServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(SearchTrackServlet.class);


    public static final String TRACK_LIST = "tracks";
    public static final String QUERY = "query";
    public static final String SIZE = "size";

    private CatalogStorage catalogService;
    private LuceneSearch luceneSearch;

    @Override
    public void init() throws ServletException {
        catalogService = CatalogFactory.getStorage();
        luceneSearch = LuceneSearch.getInstance();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {


        String query = req.getParameter("find");
        String type = req.getParameter(FindTrackServletJson.FIND_TYPE);

        log.info("Got query " + query + " find by " + type);

        if (query != null && !"".equals(query) && type != null) {

            List<Track> found = null;
            if (type.equals("like-artist")) {
                found = catalogService.searchTrackByArtistLike(query);

            } else if (type.equals("artist")) {
                found = catalogService.searchTracksByArtist(query);
            } else if (type.equals("code")) {
                found = catalogService.searchTracksByCode(query);
            } else if (type.equals("song")) {
                found = catalogService.searchTracksByName(query);
            } else if (type.equals("composer")) {
                found = catalogService.searchTracksByComposer(query);
            } else {

                try {
                    List<Long> res = luceneSearch.search(query);
                    found = catalogService.getTracks(res);

                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

            req.setAttribute(TRACK_LIST, found);
            req.setAttribute(SIZE, found != null ? found.size() : 0);
            req.setAttribute(QUERY, query);

            req.getRequestDispatcher("/search.jsp?q=" + query).forward(req, resp);

        }
    }

}
