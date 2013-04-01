<%@ page import="kz.bgm.platform.items.ReportItem" %>
<%@ page import="kz.bgm.platform.web.ReportServlet" %>
<%@ page import="java.util.List" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>

<%
    @SuppressWarnings("unchecked")
    List<ReportItem> reportList =
            (List<ReportItem>) session.getAttribute(ReportServlet.REPORT);

    session.setAttribute(ReportServlet.REPORT, null);

%>


<head>
    <script src="js/jquery.js"></script>
    <script src="js/bootstrap.js"></script>
    <script src="js/bootstrap-fileupload.js"></script>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="css/bootstrap-fileupload.css" media="screen"/>
    <title>Обработка отчета</title>
</head>


<body>

<div class="container text-center">
    <div class="row text-left">
        <legend>
            Обработка отчета
        </legend>
    </div>

    <div class="row text-left">

        <form action="/reporter" method="post" enctype="multipart/form-data">
            <div class="fileupload fileupload-new" data-provides="fileupload">
                <div class="input-append">
                    <div class="uneditable-input span3">
                        <i class="icon-file fileupload-exists"></i>
                        <span class="fileupload-preview"></span></div>
                        <span class="btn btn-file">
                            <span class="fileupload-new">Выбрать отчет</span>
                            <span class="fileupload-exists">Изменить</span><input name="file" type="file"/>
                        </span>
                    <a href="#" class="btn fileupload-exists" data-dismiss="fileupload">Удалить</a>
                </div>
            </div>

            <input type="submit">
        </form>


    </div>

    <%
        if (reportList != null) {
    %>

    <table class="table">
        <thead>
        <tr>
            <th>Код</th>
            <th>Композиция</th>
            <th>Исполнитель</th>
            <th>Авторы</th>
            <th>Цена</th>
            <th>Количество</th>
            <th>Авт. Доход</th>
            <th>Авт. Ставка</th>
            <th>Публ. Доход</th>
            <th>Каталог</th>
        </tr>
        </thead>
        <tbody>
        <%

            for (ReportItem rt : reportList) {
        %>
        <tr>
            <td><%=rt.getCode()%>
            </td>
            <td><%=rt.getCompisition()%>
            </td>
            <td><%=rt.getArtist()%>
            </td>
            <td><%=rt.getComposer()%>
            </td>
            <td><%=rt.getPrice()%>
            </td>
            <td><%=rt.getQty()%>
            </td>
            <td><%=rt.getAuthorRevenue()%>
            </td>
            <td><%=rt.getAuthRate()%>
            </td>
            <td><%=rt.getPublisherAuthRevenue()%>
            </td>
            <td><%=rt.getPublisherAuthRevenue()%>
            </td>
            <td><%=rt.getCatalog()%>
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


</body>
</html>