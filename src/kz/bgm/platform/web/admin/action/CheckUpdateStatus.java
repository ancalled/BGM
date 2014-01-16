package kz.bgm.platform.web.admin.action;

import kz.bgm.platform.utils.TaskRunner;
import org.apache.log4j.Logger;
import org.json.simple.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

import static kz.bgm.platform.web.admin.action.UpdateCatalogServlet.UpdateStatus;
import static kz.bgm.platform.web.admin.action.UpdateCatalogServlet.UpdateTask;


public class CheckUpdateStatus extends HttpServlet {

    private static final Logger log = Logger.getLogger(CheckUpdateStatus.class);


    @Override
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject jsonObj = new JSONObject();

        String updateId = req.getParameter("uid");

        TaskRunner taskRunner = TaskRunner.getInstance();
        UpdateTask task = (UpdateTask) taskRunner.getTask("update-task-" + updateId);

        if (task != null) {
            UpdateStatus status = task.getStatus();
            log.info("Got check-update request: updateId: " + updateId + ", status: " + status);
            jsonObj.put("status", "found");
            jsonObj.put("taskStatus", status.toString());
            jsonObj.writeJSONString(out);

        } else {
            log.info("Got check-update request: updateId: " + updateId + ", task not found!");
            jsonObj.put("status", "task-not-found");
            jsonObj.writeJSONString(out);
        }

        out.close();
    }
}
