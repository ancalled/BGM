<%@ page import="kz.bgm.platform.items.Track" %>
<%@ page import="kz.bgm.platform.web.FindServlet" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    @SuppressWarnings("unchecked")
    List<Track> trackList = (List<Track>) session.getAttribute(FindServlet.TRACK_LIST);

    session.setAttribute(FindServlet.TRACK_LIST, null);

    String query = (String) session.getAttribute(FindServlet.QUERY);

%>

<html>
<head>
    <script src="js/jquery.js"></script>
    <script src="js/bootstrap.js"></script>
    <script src="js/bootstrap-dropdown.js"></script>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="css/bootstrap.css" media="screen"/>
    <title>Поиск</title>
</head>
<body>
<div class="navbar">
    <div class="navbar-inner">
        <a class="brand" href="/index.html">BGM Platform</a>
        <ul class="nav">
            <li><a href="/index.html">Главная</a></li>
            <li><a href="/report.html">Отчеты</a></li>
            <li class="active"><a href="/search.jsp">Поиск</a></li>
            <li><a href="">Админка</a></li>
        </ul>
    </div>
</div>

<div class="container">


    <div class="row">

        <legend>
            Поиск композиций
        </legend>

        <form action="/finder" method="post">
            <div class="row">
                <label for="find"></label><input type="text" name="find" id="find" class="input-block-level">
            </div>

            <br>


            <div class="row">
                <input type="submit" value="Быстрый поиск" class="btn">
            </div>

            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu">
                <li><a tabindex="-1" href="#">Action</a></li>
                <li><a tabindex="-1" href="#">Another action</a></li>
                <li><a tabindex="-1" href="#">Something else here</a></li>
                <li class="divider"></li>
                <li><a tabindex="-1" href="#">Separated link</a></li>
            </ul>



            <input type="hidden" name="type" value="full">
        </form>
         <%--//todo make collapse here--%>
        <%--<div class="accordion" id="accordion2">--%>
          <%--<div class="accordion-group">--%>
            <%--<div class="accordion-heading">--%>
              <%--<a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" href="#collapseOne">--%>
                <%--Расширенный поиск--%>
              <%--</a>--%>
            <%--</div>--%>
            <%--<div id="collapseOne" class="accordion-body collapse in">--%>
              <%--<div class="accordion-inner">--%>
                <%--Anim pariatur cliche...--%>
              <%--</div>--%>
            <%--</div>--%>
          <%--</div>--%>
        <%--</div>--%>


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