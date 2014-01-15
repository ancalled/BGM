package kz.bgm.platform.web.admin.action;

import kz.bgm.platform.BgmServer;
import kz.bgm.platform.model.domain.CatalogUpdate;
import kz.bgm.platform.model.domain.Track;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import kz.bgm.platform.model.service.LuceneSearch;
import kz.bgm.platform.utils.LuceneIndexRebuildUtil;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

public class ApplyUpdateServlet extends HttpServlet {


    public static final String VIEWS_HOME_URL = "../view";
    public static final String RESULT_URL = VIEWS_HOME_URL + "/catalog-update";


    private static final Logger log = Logger.getLogger(ApplyUpdateServlet.class);

    private CatalogStorage storage;
    private LuceneSearch luceneSearch;

    @Override
    public void init() throws ServletException {
        storage = CatalogFactory.getStorage();
        luceneSearch = LuceneSearch.getInstance();
    }


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String idStr = req.getParameter("id");
        if (idStr != null) {
            long id = Long.parseLong(idStr);

            log.info("Applying catalog updates, id: " + id);

            storage.applyCatalogUpdate(id);

            log.info("Get all new tracks for reindex");

            List<Track> updatedTracks = storage.getUpdates(id);

            log.info("Found " + updatedTracks.size() + " indexes. Rebuilding index for this tracks");

            LuceneIndexRebuildUtil.rebuildIndex(updatedTracks);

            log.info("Done. Reinitializing searcher");
            //reinit searcher after index update
            LuceneSearch.getInstance().initSearcher(BgmServer.INDEX_DIR);

//            luceneSearch.index(tracks, LuceneIndexRebuildUtil.INDEX_DIR);

            log.info("Applied.");

        }

        resp.sendRedirect(RESULT_URL);
    }


}


