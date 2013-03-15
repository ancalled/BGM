<%@ page import="kz.bgm.items.Track" %>
<%@ page import="kz.bgm.web.api.FindServlet" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<%
    @SuppressWarnings("unchecked")
    List<Track> trackList = (List<Track>) session.getAttribute(FindServlet.TRACK_LIST);
%>

<html>
<head>
    <script src="http://code.jquery.com/jquery.js"></script>
    <script src="js/bootstrap.js"></script>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="css/bootstrap.css" media="screen"/>
    <title>Find song</title>
</head>
<body style="margin-top: 50">


<div class="container">

    <div class="row">

        <form class="" action="/finder" method="post" style="margin-bottom:40; margin-left:100 ">

            <input placeholder="Search" type="text" name="find" id="find">

            <label class="radio" style="margin-top: 20">

                <input type="radio" name="type" class="icon-check" value="like">
                Поиск по имени ипонителя
            </label>

            <label class="radio" style="margin-top: 20">
                <input type="radio" name="type" class="checkbox" value="full" checked="checked">
                Полный поиск
            </label>

            <input type="submit" style="margin-top: 20" value="Найти" class="btn">
        </form>


        <%
            if (trackList != null) {
        %>
        <table class="table">
            <thead>
            <tr>
                <td>Id</td>
                <td>Code</td>
                <td>Composition</td>
                <td>Artist</td>
                <td>Authors</td>
                <td>Controlled Metch</td>
                <td>Collect Metch</td>
                <td>Publisher</td>
                <td>Comment</td>
            </tr>
            </thead>
            <tbody>

            <%

                for (Track t : trackList) {
            %>
            <tr>
                <td><%=t.getId()%>
                </td>
                <td><%=t.getCode()%>
                </td>
                <td><%=t.getComposition()%>
                </td>
                <td><%=t.getArtist()%>
                </td>
                <td><%=t.getAuthors()%>
                </td>
                <td><%=t.getCollect_metch()%>
                </td>
                <td><%=t.getControlled_metch()%>
                </td>
                <td><%=t.getPublisher()%>r</td>
                <td><%=t.getComment()%>
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