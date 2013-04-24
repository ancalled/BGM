<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>
<head>
    <script src="js/jquery.js"></script>
    <script src="js/bootstrap.js"></script>
    <script src="js/bootstrap-fileupload.js"></script>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="css/bootstrap-fileupload.css" media="screen"/>
    <title>Результат загрузки отчета</title>
</head>
<body>

<%
    String result = request.getParameter("mess");

    if (result != null) {
%>

<div class="navbar">
    <div class="navbar-inner">
        <a class="brand" href="/index.html">BGM Platform</a>
        <ul class="nav">
            <li><a href="/index.html">Главная</a></li>
            <li><a href="/report.html">Отчеты</a></li>
            <li><a href="/search.jsp">Поиск</a></li>
            <li><a href="">Админка</a></li>
        </ul>
    </div>
</div>

<H3 class="span8">Рeзультат загрузки отчета : <%=result%>
</H3>

<%
} else {
%>
<H2>Error</H2>
<%
    }
%>


</body>
</html>