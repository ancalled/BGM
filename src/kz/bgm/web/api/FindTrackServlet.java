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

public class FindTrackServlet extends HttpServlet {

    public static final String APP_DIR = System.getProperty("user.dir");
    public static final String WEB_PROPS = APP_DIR + "/jetty.properties";

    public static final String DB_PROPS = APP_DIR + "/db.properties";
    public static final String TRACKS = "tracks";
    public static final String FIND_TYPE = "type";

    @SuppressWarnings("unchecked")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String find = req.getParameter("find");

        if (find != null && !"".equals(find)) {

            CatalogStorage cs = new CatalogStorage(DB_PROPS);
            List<Track> foundTracks =
                    cs.getTrackBySongName(find);
            JSONArray mass = new JSONArray();

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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String find = req.getParameter("find");
        String type = req.getParameter(FIND_TYPE);
        if (find != null && !"".equals(find)) {


            CatalogStorage cs = new CatalogStorage(DB_PROPS);
            List<Track> foundTracks;
            if (type.equals("like")) {
                foundTracks = cs.getTracksLikeArtist(find);
            } else {
                foundTracks = cs.getTracksByArtist(find);
            }


            req.getSession().setAttribute(TRACKS, foundTracks);

            PrintWriter writer = resp.getWriter();


            writer.print("<html>");
            writer.print("<head>");
            writer.print("<title>Result</title>");
            writer.print("</head>");

            writer.print("<body>");

            writer.print("Found " + foundTracks.size() + " songs");

            writer.print("<br>");
            writer.print("<br>");

            writer.print("<b>" + "ID" + "</b> ");
            writer.print("<b>" + "CODE" + "</b> ");
            writer.print("<b>" + "Composition" + "</b> ");
            writer.print("<b>" + "Publisher" + "</b> ");
            writer.print("<br>");
            writer.print("<br>");

            for (Track t : foundTracks) {
                writer.print("<b>" + t.getId() + "</b> ");
                writer.print("<b>" + t.getCode() + "</b> ");
                writer.print("<b>" + t.getComposition() + "</b> ");
                writer.print("<b>" + t.getPublisher() + "</b> ");
                writer.print("<br>");
            }

            writer.print("<br>");
            writer.print("<br>");

            writer.print("</body>");
            writer.print("</html>");


            writer.flush();
            writer.close();
        }
    }


}
