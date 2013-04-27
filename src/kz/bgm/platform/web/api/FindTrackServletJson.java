package kz.bgm.platform.web.api;


import kz.bgm.platform.model.service.CatalogFactory;
import kz.bgm.platform.model.service.CatalogStorage;
import kz.bgm.platform.model.domain.Track;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class FindTrackServletJson extends HttpServlet {

    public static final String APP_DIR = System.getProperty("user.dir");

    public static final String FIND_TYPE = "type";



    @SuppressWarnings("unchecked")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {


        CatalogStorage catalogService = CatalogFactory.getStorage();
        String find = req.getParameter("find");

        if (find != null && !"".equals(find)) {

            List<Track> foundTracks =
                    catalogService.searchBySongName(find);
            JSONArray mass = new JSONArray();
            if (foundTracks == null) {
                resp.sendRedirect("/find.html");
                return;
            }
            for (Track t : foundTracks) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", t.getId());
                jsonObject.put("code", t.getCode());
                jsonObject.put("composition", t.getName());
                jsonObject.put("artist", t.getArtist());
                jsonObject.put("authors", t.getComposer());
                jsonObject.put("mobile_share", (double) t.getMobileShare());
                jsonObject.put("public_share", (double) t.getPublicShare());

                mass.add(jsonObject);
            }

            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.print(mass);
            out.flush();
        }

    }


}
