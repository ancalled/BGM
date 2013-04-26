<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

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

            <input type="hidden" name="type" value="full">
        </form>


        <c:if test="${tracks!=null}">
        <span class="label ">На запрос '${query}' найдено Результатов: ${size}</span>

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

            <c:forEach var="t" items="${tracks}">
            <tr>
                <td>${t.code}
                </td>
                <td><${t.name}
                </td>
                <td>${t.artist}
                </td>
                <td>${t.composer}
                </td>
                <td>${t.mobileShare}
                </td>
                <td>${t.publicShare}
                </td>
                <td>${t.catalog}
                </td>
            </tr>
            </c:forEach>

            </tbody>
        </table>

        </c:if>

    </div>

</div>

</body>
</html>