package kz.bgm.platform.web.customer.action;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class RemoveFromUserCatalogServlet extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String strTrackId = req.getParameter("track_id");
        long id = Long.parseLong(strTrackId);

        HttpSession session = req.getSession();
        //todo доделать удаление из user-catalog ("корзина")

    }
}
