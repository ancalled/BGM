package kz.bgm.platform.web.admin.action;


import kz.bgm.platform.model.domain.Track;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EditTrackServlet extends HttpServlet {

    private static final Logger log = Logger.getLogger(EditTrackServlet.class);
    private CatalogStorage storage;

    @Override
    public void init() throws ServletException {
        super.init();
        storage = CatalogFactory.getStorage();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        log.info("EditTrackServlet got request");

        String idStr = req.getParameter("track-id");
        String catIdStr = req.getParameter("cat-id");
        String code = req.getParameter("code");
        String name = req.getParameter("name");
        String artist = req.getParameter("artist");
        String composer = req.getParameter("composer");
        String shareMobileStr = req.getParameter("mobile-share");
        String sharePublicStr = req.getParameter("public-share");

        if (hasEmpty(idStr, catIdStr, code, name, artist,
                composer, shareMobileStr, sharePublicStr)) {
            resp.sendRedirect("/admin/view/edit-track?err=Not all data");
            log.warn("Some parameter is missing");
            return;
        }

        long id = Long.parseLong(idStr);
        int catId = Integer.parseInt(catIdStr);
        float shareMobile = Float.parseFloat(shareMobileStr);
        float sharePublic = Float.parseFloat(sharePublicStr);

        Track track = new Track();
        track.setId(id);
        track.setCatalogId(catId);
        track.setCode(code);
        track.setName(name);
        track.setArtist(artist);
        track.setComposer(composer);
        track.setMobileShare(shareMobile);
        track.setPublicShare(sharePublic);

        int rows = storage.updateTrack(track);

        if (rows == 0) {
            resp.sendRedirect("/admin/view/edit-track?err=Track was not edit");
        }
        else{
            resp.sendRedirect("/admin/view/search?pageSize=&field=code&from=&extend=true&submit=true&q="+code);
        }
    }

    private boolean hasEmpty(String... params) {
        for (String p : params) {
            if ("".equals(p) || p == null) {
                return true;
            }
        }

        return false;
    }

}
