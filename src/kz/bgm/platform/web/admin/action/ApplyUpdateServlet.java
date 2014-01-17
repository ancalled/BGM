package kz.bgm.platform.web.admin.action;

import kz.bgm.platform.BgmServer;
import kz.bgm.platform.model.domain.Track;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import kz.bgm.platform.model.service.LuceneSearch;
import kz.bgm.platform.utils.LuceneIndexRebuildUtil;
import kz.bgm.platform.utils.TaskRunner;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ApplyUpdateServlet extends HttpServlet {


    public static final String VIEWS_HOME_URL = "../view";
    public static final String RESULT_URL = VIEWS_HOME_URL + "/catalog-update";


    private static final Logger log = Logger.getLogger(ApplyUpdateServlet.class);

    private CatalogStorage storage;

    @Override
    public void init() throws ServletException {
        storage = CatalogFactory.getStorage();
    }


    @Override
    @SuppressWarnings("unchecked")
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject jsonObj = new JSONObject();

        String idStr = req.getParameter("uid");
        if (idStr == null) {
            jsonObj.put("status", "id-is-not-set");
            jsonObj.writeJSONString(out);
            return;
        }

        long id = Long.parseLong(idStr);

        TaskRunner.getInstance().submit("apply-update-" + id,
                new ApplyUpdateTask(id, storage));


        jsonObj.put("status", "ok");
        jsonObj.writeJSONString(out);

//        resp.sendRedirect(RESULT_URL);
    }


    public static enum ApplyStatus {
        APPLY_CATALOG_STEP1,
        APPLY_CATALOG_STEP2,
        APPLY_CATALOG_STEP3,
        INDEX_REBUILD,
        FINISHED,
    }


    public static class ApplyUpdateTask extends TaskRunner.Task<ApplyStatus> {

        private final long id;
        private final CatalogStorage storage;

        public ApplyUpdateTask(long id, CatalogStorage storage) {
            this.id = id;
            this.storage = storage;
        }

        @Override
        public Object call() throws Exception {

            log.info("Applying catalog updates, id: " + id);
            changeStatus(ApplyStatus.APPLY_CATALOG_STEP1);
            storage.applyCatalogUpdateStep1(id);

            changeStatus(ApplyStatus.APPLY_CATALOG_STEP2);
            storage.applyCatalogUpdateStep2(id);

            changeStatus(ApplyStatus.APPLY_CATALOG_STEP3);
            storage.applyCatalogUpdateStep3(id);

            log.info("Get all new tracks for reindex");
            changeStatus(ApplyStatus.APPLY_CATALOG_STEP3);
            List<Track> updatedTracks = storage.getAllTracksOfCatalogUpdate(id);

            log.info("Found " + updatedTracks.size() + " indexes. Rebuilding index for this tracks");
            LuceneIndexRebuildUtil.rebuildIndex(updatedTracks);

            log.info("Done. Reinitializing searcher");
            //reinit searcher after index update
            LuceneSearch.getInstance().initSearcher(BgmServer.INDEX_DIR);
//            luceneSearch.index(tracks, LuceneIndexRebuildUtil.INDEX_DIR);

            changeStatus(ApplyStatus.FINISHED);

            log.info("Applied.");

            return null;
        }
    }

}


