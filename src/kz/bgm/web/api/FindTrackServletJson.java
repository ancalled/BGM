package kz.bgm.web.api;


import kz.bgm.CatalogStorage;
import kz.bgm.items.Track;
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
    public static final String WEB_PROPS = APP_DIR + "/jetty.properties";

    public static final String DB_PROPS = APP_DIR + "/db.properties";
    public static final String TRACKS = "tracks";
    public static final String FIND_TYPE = "type";
    public static CatalogStorage catalogService;


    @Override

    public void init() throws ServletException {
        catalogService = new CatalogStorage(DB_PROPS);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String find = req.getParameter("find");

        if (find != null && !"".equals(find)) {
                                                       //todo эксперементировать пока над этим сервлетом
            List<Track> foundTracks =
                    catalogService.getTrackBySongName(find);
            JSONArray mass = new JSONArray();
            if (foundTracks == null) {
                resp.sendRedirect("/find.html");//todo добавить ошибку
                return;
            }
            for (Track t : foundTracks) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("Id", t.getId());
                jsonObject.put("Code", t.getCode());
                jsonObject.put("Composition", t.getComposition());
                jsonObject.put("Artist", t.getArtist());
                jsonObject.put("Authors", t.getMusicAuthors());
                jsonObject.put("Controlled_Metch", (double) t.getControlled_metch());
                jsonObject.put("Collect_Metch", (double) t.getCollect_metch());
                jsonObject.put("Publisher", t.getPublisher());
                jsonObject.put("Comment", t.getComment());

                mass.add(jsonObject);
            }

            resp.setContentType("application/json");
            PrintWriter out = resp.getWriter();
            out.print(mass);
            out.flush();
        }

    }


}
