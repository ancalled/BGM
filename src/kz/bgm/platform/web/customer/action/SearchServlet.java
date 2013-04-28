package kz.bgm.platform.web.customer.action;

import kz.bgm.platform.model.domain.Track;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import kz.bgm.platform.model.service.LuceneSearch;
import org.apache.log4j.Logger;
import org.apache.lucene.queryparser.classic.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;


public class SearchServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(SearchServlet.class);


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

        String query = req.getParameter("q");
        String type = req.getParameter("type");

        log.debug("Got query: " + query + ", search type: " + type);

        if (query != null && !"".equals(query.trim()) && type != null) {

            List<Track> found = null;
            switch (type) {
                case "like-artist":
                    found = catalogService.searchTrackByArtistLike(query);

                    break;
                case "artist":
                    found = catalogService.searchTracksByArtist(query);
                    break;
                case "code":
                    found = catalogService.searchTracksByCode(query);
                    break;
                case "song":
                    found = catalogService.searchTracksByName(query);
                    break;
                case "composer":
                    found = catalogService.searchTracksByComposer(query);
                    break;
                default:
                    try {
                        List<Long> res = luceneSearch.search(query);
                        found = catalogService.getTracks(res);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
            }

            HttpSession session = req.getSession(true);

            session.setAttribute("tracks", found);
            session.setAttribute("query", query);


            resp.sendRedirect("/csutomer/view/search-result");

        }
    }

}
