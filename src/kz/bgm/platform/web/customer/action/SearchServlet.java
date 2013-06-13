package kz.bgm.platform.web.customer.action;

import kz.bgm.platform.model.domain.SearchType;
import kz.bgm.platform.model.domain.Track;
import kz.bgm.platform.model.domain.User;
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
        String fieldStr = req.getParameter("field");

        SearchType searchType;
        if (fieldStr != null) {
            searchType = SearchType.valueOf(fieldStr);
        } else {
            searchType = SearchType.ALL;
        }

        HttpSession ses = req.getSession();
        User user = (User) ses.getAttribute("user");



        List<Long> available = catalogService.getAvailableCatalogs(user.getCustomerId());
        List<Long> requested = getCatalogsId(req);
        List<Long> catalogs = new ArrayList<>();
        if (requested.isEmpty()) {
            catalogs.addAll(available);
        } else {
            for (Long id : requested) {
                if (available.contains(id)) {
                    catalogs.add(id);
                }
            }
        }

        log.debug("Got query: " + query + ", search type: " + searchType);
        List<Track> found = Collections.emptyList();
        try {
            if (query.contains(";")) {
                found = separatedFieldsSearch(query, catalogs);

            } else {
                found = search(query, searchType, catalogs);
            }

        } catch (ParseException e) {
            e.printStackTrace();
        }
        //todo убрать этот session
        HttpSession session = req.getSession();

        session.setAttribute("tracks", found);
        session.setAttribute("query", query);
        session.setAttribute("searchType", searchType);

        resp.sendRedirect(buildResponseUrl(req));
    }


    private List<Track> search(String query, SearchType searchType, List<Long> catalogs)
            throws IOException, ParseException {
        List<Track> found;
        switch (searchType) {
            case ALL:
                List<Long> tracks = luceneSearch.search(query);
                found = catalogService.getTracks(tracks, catalogs);

                break;

//            case "";
            default:
//                found = catalogService.searchTracks(searchType, query, catalogs);
                found = null;
                break;
        }
        return found;
    }

    private List<Track> separatedFieldsSearch(String query, List<Long> catalogs)
            throws IOException, ParseException {
        String[] fields = query.split(";");
        List<Long> tracks = new ArrayList<>();

        if (fields.length == 2) {
            tracks = luceneSearch.searchByAuthor(fields[0], fields[1], 100, 3);

        } else if (fields.length >= 3) {
            tracks = LuceneSearch.parseSearchResult(
                    luceneSearch.search(fields[2], fields[1], fields[0], 100, 3));
        }

        return catalogService.getTracks(tracks, catalogs);
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
