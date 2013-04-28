<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <script src="../../js/jquery.js"></script>
    <script src="../../js/bootstrap.js"></script>
    <script src="../../js/bootstrap-fileupload.js"></script>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="../../css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="../../css/bootstrap-fileupload.css" media="screen"/>
    <title>Клиенты</title>
</head>
<body>

<div class="navbar">
    <div class="navbar-inner">
        <a class="brand" href="/index.html">BGM Platform</a>
        <ul class="nav">
            <li><a href="/index.html">Главная</a></li>
            <li><a href="/reports.html">Отчеты</a></li>
            <li><a href="/search.html">Поиск</a></li>
            <li class="active"><a href="/view/customers">Клиенты</a></li>
            <li><a href="/logout">Выход</a></li>
        </ul>
    </div>
</div>


<div class="container">
    <table class="table">
        <thead>
        <tr>
            <th>Название</th>
            <th>Ставка</th>
            <th>Тип прав</th>
        </tr>
        </thead>
        <tbody>


        <c:forEach var="c" items="${customers}">
            <tr>
                <td>${c.name}
                </td>
                <td>${c.royalty}
                </td>
                <td>${c.rightType}
                </td>
            </tr>
        </c:forEach>

        </tbody>
    </table>


</div>


</body>
</html>