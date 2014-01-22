package kz.bgm.platform.web.admin.action;

import kz.bgm.platform.model.domain.Track;
import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;


public class GetRandomTracksServlet extends HttpServlet {

    private CatalogStorage storage;


    @Override
    public void init() throws ServletException {
        storage = CatalogFactory.getStorage();
    }


    @Override
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setCharacterEncoding("UTF-8");
        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject jsonObj = new JSONObject();


        String numStr = req.getParameter("n");
        int num = numStr != null ? Integer.parseInt(numStr) : 10;

        String catIdStr = req.getParameter("p");

        Collection<Track> tracks;
        if (catIdStr != null) {
            long catId = Long.parseLong(catIdStr);
            tracks = storage.getRandomTracks(catId, num);
        } else {
            tracks = storage.getRandomTracks(num);
        }

        JSONArray tracksArray = new JSONArray();
        for (Track t: tracks) {
            JSONObject trackObj = new JSONObject();
            trackObj.put("id", t.getId());
            trackObj.put("name", t.getName());
            trackObj.put("artist", t.getArtist());
            trackObj.put("catalog", t.getCatalog());

            tracksArray.add(trackObj);
        }

        jsonObj.put("tracks", tracksArray);

        jsonObj.writeJSONString(out);
        out.close();

    }
}
