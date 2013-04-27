<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>


<head>
    <script src="../../web/js/jquery.js"></script>
    <script src="../../web/js/bootstrap.js"></script>
    <script src="../../web/js/bootstrap-fileupload.js"></script>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="../../web/css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="../../web/css/bootstrap-fileupload.css" media="screen"/>
    <title>Обработка отчета</title>
</head>


<body>

<div class="navbar">
    <div class="navbar-inner">
        <a class="brand" href="#">BGM Platform</a>
        <ul class="nav">
            <li><a href="\index.html">Главная</a></li>
            <li class="active"><a href="\report.jsp">Отчеты</a></li>
            <li><a href="\search.jsp">Поиск</a></li>
            <li><a href="">Админка</a></li>
        </ul>
    </div>
</div>


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


            <div class="row-fluid">
                <input class="btn" type="submit">
            </div>
        </form>


        <form action="/report-calculator" method="post">
            <div class="row-fluid">
                <input class="btn" type="submit" value="Camone!">
            </div>
        </form>

        <%--<%--%>
        <%--if (reportPath != null && reportPath.size() > 0) {--%>

        <%--for (String path : reportPath) {--%>
        <%--%>--%>
        <%--<form>--%>
        <%--<div class="row-fluid">--%>
        <%--<input type="button"--%>
        <%--class="btn"--%>
        <%--value="Загрузить отчет"--%>
        <%--onClick="window.location.href='<%=path%>'">--%>
        <%--</div>--%>
        <%--</form>--%>
        <%--<%--%>
        <%--}--%>
        <%--}--%>

        <%--%>--%>

        <%--</div>--%>

        <%--<%--%>
        <%--if (reportList != null) {--%>
        <%--%>--%>

        <%--<table class="table table-bordered">--%>
        <%--<thead>--%>
        <%--<tr>--%>
        <%--<th>Код</th>--%>
        <%--<th>Композиция</th>--%>
        <%--<th>Исполнитель</th>--%>
        <%--<th>Авторы</th>--%>
        <%--<th>Цена</th>--%>
        <%--<th>Количество</th>--%>
        <%--<th>Авт. Доход</th>--%>
        <%--<th>Авт. Ставка</th>--%>
        <%--<th>Публ. Доход</th>--%>
        <%--<th>Каталог</th>--%>
        <%--</tr>--%>
        <%--</thead>--%>
        <%--<tbody>--%>
        <%--<%--%>
        <%--for (ReportItem rt : reportList) {--%>
        <%--%>--%>
        <%--<tr>--%>
        <%--<td><%=rt.getCode()%>--%>
        <%--</td>--%>
        <%--<td><%=rt.getCompisition()%>--%>
        <%--</td>--%>
        <%--<td><%=rt.getArtist()%>--%>
        <%--</td>--%>
        <%--<td><%=rt.getComposer()%>--%>
        <%--</td>--%>
        <%--<td><%=rt.getPrice()%>--%>
        <%--</td>--%>
        <%--<td><%=rt.getQty()%>--%>
        <%--</td>--%>
        <%--<td><%=rt.getAuthorRevenue()%>--%>
        <%--</td>--%>
        <%--<td><%=rt.getAuthRate()%>--%>
        <%--</td>--%>
        <%--<td><%=rt.getPublisherAuthRevenue()%>--%>
        <%--</td>--%>
        <%--<td><%=rt.getCatalog()%>--%>
        <%--</td>--%>
        <%--</tr>--%>
        <%--<%--%>
        <%--}--%>
        <%--%>--%>

        <%--</tbody>--%>
        <%--</table>--%>

        <%--<%--%>
        <%--}--%>
        <%--%>--%>

    </div>
 </div>

</body>
</html>