package kz.bgm.platform.web;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class DispatcherServlet extends HttpServlet {


    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        String pth = req.getPathInfo();

        Action action = null;
        if ("/report-upload-result".equals(pth)) {
            action = new Action() {
                @Override
                public String execute(HttpServletRequest req, HttpServletResponse resp) {


                    return "report-upload-result";
                }
            };
        }


        if (action != null) {
            String view = action.execute(req, resp);
            req.getRequestDispatcher("/WEB-INF/" + view + ".jsp").forward(req, resp);

        } else {
            resp.sendRedirect("/404.html");
        }


    }


    public static interface Action {


        String execute(HttpServletRequest req, HttpServletResponse resp);

    }
}
