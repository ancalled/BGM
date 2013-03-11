package kz.bgm.web.api;

import kz.bgm.CatalogStorage;
import kz.bgm.items.Track;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;


public class FindServlet extends HttpServlet {

    public static CatalogStorage catalogService;

    @Override
    public void init() throws ServletException {
        catalogService = new CatalogStorage(FindTrackServlet.DB_PROPS);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String find = req.getParameter("find");
        String type = req.getParameter(FindTrackServlet.FIND_TYPE);
        if (find != null && !"".equals(find)) {


            List<Track> foundTracks;
            if (type.equals("like")) {
                foundTracks = catalogService.getTracksLikeArtist(find);
            } else {
                foundTracks = catalogService.getTracksByArtist(find);
            }


            req.getSession().setAttribute(FindTrackServlet.TRACKS, foundTracks);

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
