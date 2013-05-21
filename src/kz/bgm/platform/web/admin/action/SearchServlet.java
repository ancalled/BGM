package kz.bgm.platform.web.admin.action;

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
        String strCatalogId = req.getParameter("catalog");
        String strFrom = req.getParameter("from");
        String strPageSize = req.getParameter("pageSize");

        long catalogId = 0;

        if (strCatalogId != null &&
                !"".equals(strCatalogId) &&
                !"all".equals(strCatalogId)) {
            catalogId = Long.parseLong(strCatalogId);
        }

//for pagination
//        int from = 0;
//        int pageSize = 50;
//        if (strFrom != null&&!"".equals(strFrom)) {
//            from = Integer.parseInt(strFrom);
//        }
//
//        if (strPageSize != null&&!"".equals(strPageSize)) {
//            pageSize = Integer.parseInt(strPageSize);
//        }


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
                case "composition":
                    found = catalogService.searchTracksByName(query);
                    break;
                case "composer":
                    found = catalogService.searchTracksByComposer(query);
                    break;
                default:
                    try {
                        List<Long> res = luceneSearch.search(query/*from,pageSize*/);

                        if ("all".equals(strCatalogId) || catalogId == 0) {
                            found = catalogService.getTracks(res);
                        } else {
                            found = catalogService.getTracks(res, catalogId);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    break;
            }

            HttpSession session = req.getSession();

            session.setAttribute("tracks", found);
            session.setAttribute("query", query);
            session.setAttribute("type", type);


            resp.sendRedirect("/admin/view/search-result");

        }
    }

}
