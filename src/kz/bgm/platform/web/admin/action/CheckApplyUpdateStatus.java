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

import static kz.bgm.platform.web.admin.action.ApplyUpdateServlet.ApplyStatus;
import static kz.bgm.platform.web.admin.action.ApplyUpdateServlet.ApplyUpdateTask;


public class CheckApplyUpdateStatus extends HttpServlet {

    private static final Logger log = Logger.getLogger(CheckApplyUpdateStatus.class);


    @Override
    @SuppressWarnings("unchecked")
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {

        resp.setContentType("application/json");
        PrintWriter out = resp.getWriter();
        JSONObject jsonObj = new JSONObject();

        String updateId = req.getParameter("uid");

        TaskRunner taskRunner = TaskRunner.getInstance();
        ApplyUpdateTask task = (ApplyUpdateTask) taskRunner.getTask("apply-update-" + updateId);

        if (task != null) {
            ApplyStatus status = task.getStatus();
            log.info("Got check-apply request: updateId: " + updateId + ", status: " + status);
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
