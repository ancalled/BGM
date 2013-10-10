package kz.bgm.platform.web.admin.action;


import org.json.simple.JSONObject;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

public class DBActivityServlet extends HttpServlet {

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");

        JSONObject json = new JSONObject();
        json.put("status", "fuck");

        PrintWriter pw = resp.getWriter();
        pw.print(json.toString());
        pw.close();
//        SELECT * FROM INFORMATION_SCHEMA.PROCESSLIST   -!!!
    }


}
