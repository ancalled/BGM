<%@ page import="kz.bgm.platform.items.Track" %>
<%@ page import="kz.bgm.platform.web.FindServlet" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    @SuppressWarnings("unchecked")
    List<Track> trackList = (List<Track>) session.getAttribute(FindServlet.TRACK_LIST);
    String query = (String) session.getAttribute(FindServlet.QUERY);

%>

<html>
<head>
    <script src="js/jquery.js"></script>
    <script src="js/bootstrap.js"></script>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="css/bootstrap.css" media="screen"/>
    <title>Поиск</title>
</head>
<body>

<div class="container">


    <div class="row">

        <legend>
            Поиск композиций
        </legend>

        <form action="/finder" method="post">
            <div class="row">
                <label for="find"></label><input type="text" name="find" id="find" class="input-large">
            </div>

            <div class="row">
                <input type="submit" value="Быстрый поиск" class="btn">
            </div>


            <%--<label class="radio" style="margin-top: 20">--%>

            <%--<input type="radio" name="type" class="icon-check" value="like">--%>
            <%--Поиск по имени ипонителя--%>
            <%--</label>--%>

            <%--<label class="radio" style="margin-top: 20">--%>
            <%--<input type="radio" name="type" class="checkbox" value="full" checked="checked">--%>
            <%--Полный поиск--%>
            <%--</label>--%>
            <input type="hidden" name="type" value="full">
        </form>


        <%
            if (trackList != null) {
        %>
        <span class="label ">На запрос '<%=query%>' найдено Результатов: <%=trackList.size()%></span>

        <table class="table">
            <thead>
            <tr>
                <th>Код</th>
                <th>Композиция</th>
                <th>Исполнитель</th>
                <th>Авторы</th>
                <th>Мобильный контент</th>
                <th>Публичка</th>
                <th>Каталог</th>
            </tr>
            </thead>
            <tbody>

            <%

                for (Track t : trackList) {
            %>
            <tr>
                <td><%=t.getCode()%>
                </td>
                <td><%=t.getName()%>
                </td>
                <td><%=t.getArtist()%>
                </td>
                <td><%=t.getComposer()%>
                </td>
                <td><%=t.getMobileShare()%>
                </td>
                <td><%=t.getPublicShare()%>
                </td>
                <%
                    if (t.getCatalog() == null) {
                        t.setCatalog("");
                    }
                %>
                <td><%=t.getCatalog()%>
                </td>
            </tr>
            <%
                }
            %>

            </tbody>
        </table>

        <%
            }
        %>

    </div>

</div>

</body>
</html>