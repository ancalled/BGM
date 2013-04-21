package kz.bgm.platform.web;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public class LoginServlet extends HttpServlet {


    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String user = req.getParameter("u");
        String pass = req.getParameter("p");

        if (user != null && pass != null) {
//        int userId = checkUserInBase(user, pass);
//        if (userId > 0) {

            HttpSession session = req.getSession();
            session.setAttribute("user", user);


//        }

        }
        resp.sendRedirect("/index.html");
    }

    private int checkUserInBase(String userName, String pass) {
        //return user id
        return 0;
    }
}
