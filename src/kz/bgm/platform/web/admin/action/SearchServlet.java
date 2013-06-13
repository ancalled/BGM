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
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.*;


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

        req.setCharacterEncoding("UTF-8");
        resp.setCharacterEncoding("UTF-8");

        String query = req.getParameter("q");
        String field = req.getParameter("field");
        String extended = req.getParameter("extend");
        List<Long> catalogIdList = getCatalogsId(req);


//for pagination
//        String strFrom = req.getParameter("from");
//        String strPageSize = req.getParameter("pageSize");
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
        List<Track> found = Collections.emptyList();
        try {
            if (query.contains(";")) {

                found = complexSearch(query, catalogIdList);

            } else {

                found = qutickSearch(query, field, catalogIdList);

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //todo убрать этот session
        HttpSession session = req.getSession();

        session.setAttribute("tracks", found);
        session.setAttribute("query", query);
        session.setAttribute("type", field);

        resp.sendRedirect(buildResponseUrl(req));

    }

    private List<Track> qutickSearch(String query, String field, List<Long> catalogs) throws IOException, ParseException {
        List<Track> found;
        switch (field) {
            case "all":
                List<Long> trackIdList = luceneSearch.search(query/*from,pageSize*/);

                if (!catalogs.isEmpty()) {
                    found = catalogService.getTracks(trackIdList, catalogs);
                } else {
                    found = catalogService.getTracks(trackIdList);
                }

                break;
            default:
                found = catalogService.searchTracks(field, query, catalogs);
                break;
        }
        return found;
    }

    private List<Track> complexSearch(String query, List<Long> catalogs) throws IOException, ParseException {
        List<Track> found;
        String[] fields = query.split(";");

        List<Long> tracks = new ArrayList<>();

        if (fields.length == 2) {
            tracks = luceneSearch.searchByAuthor(fields[0], fields[1], 100, 3);
        } else if (fields.length >= 3) {
            tracks = LuceneSearch.parseSearchResult(
                    luceneSearch.search(fields[2], fields[1], fields[0], 100, 3));
        }

        if (!catalogs.isEmpty()) {
            found = catalogService.getTracks(tracks, catalogs);
        } else {
            found = catalogService.getTracks(tracks);
        }
        return found;
    }

    private String buildResponseUrl(HttpServletRequest req) {

        StringBuilder buf = new StringBuilder();
        buf.append("/customer/view/search");

        Map<String, String[]> params = req.getParameterMap();
        if (!params.isEmpty()) {
            buf.append("?");
            int i = 0;
            for (String key : params.keySet()) {

                buf.append(key);
                buf.append("=");
                String value = params.get(key)[0];
                if ("q".equals(key)) {
                    try {
                        buf.append(URLEncoder.encode(value, "UTF-8"));
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                } else {
                    buf.append(value);
                }
                if (i++ < params.size() - 1) {
                    buf.append("&");
                }
            }

        }
        return buf.toString();
    }


    private List<Long> getCatalogsId(HttpServletRequest req) {
        Enumeration<String> paramNames = req.getParameterNames();
        List<Long> idList = new ArrayList<>();

        while (paramNames.hasMoreElements()) {
            String param = paramNames.nextElement();

            if (param.contains("catalog")) {
                String strCatId = req.getParameter(param);
                Long id = Long.parseLong(strCatId);
                if (id == -1) {
                    return idList;
                }
                idList.add(id);
            }
        }

        return idList;

    }


}
