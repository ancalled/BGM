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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;


public class SearchServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(SearchServlet.class);
    public static final String ARTIST = "artist";
    public static final String CODE = "code";
    public static final String NAME = "name";
    public static final String COMPOSER = "composer";

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
        String field = req.getParameter("field");

        String strFrom = req.getParameter("from");
        String strPageSize = req.getParameter("pageSize");

        List<Long> catalogIdList = getCatalogsId(req);

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


        log.debug("Got query: " + query + ", search type: " + field);

        List<Track> found = null;


        if (field.equals("all")) {

            try {
                List<Long> trackIdList = luceneSearch.search(query/*from,pageSize*/);
                found = catalogService.getTracks(trackIdList, catalogIdList);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            found = catalogService.searchTracks(field, query, catalogIdList);
        }

        HttpSession session = req.getSession();

        session.setAttribute("tracks", found);
        session.setAttribute("query", query);
        session.setAttribute("type", field);


        resp.sendRedirect("/admin/view/search-result");

    }


    private List<Long> getCatalogsId(HttpServletRequest req) {
        Enumeration<String> paramNames = req.getParameterNames();
        List<Long> idList = new ArrayList<Long>();

        while (paramNames.hasMoreElements()) {
            String param = paramNames.nextElement();

            if (param.contains("catalog")) {
                String strCatId = req.getParameter(param);
                Long id = Long.parseLong(strCatId);
                if (id == -1) {
                    return null;
                }
                idList.add(id);
            }
        }

        return idList;

    }


}
