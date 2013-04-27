<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<html>
<head>
    <script src="/js/jquery.js"></script>
    <script src="/js/bootstrap.js"></script>
    <script src="/js/bootstrap-fileupload.js"></script>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-fileupload.css" media="screen"/>
    <title>Поиск</title>
</head>
<body>
<div class="navbar">
    <div class="navbar-inner">
        <a class="brand" href="/index.html">BGM Platform</a>
        <ul class="nav">
            <li><a href="/index.html">Главная</a></li>
            <li><a href="/reports.html">Отчеты</a></li>
            <li class="active"><a href="/search.html">Поиск</a></li>
        </ul>
    </div>
</div>

<div class="container">


    <div class="row">

        <legend>
            Поиск композиций
        </legend>

        <form action="/action/search" method="post">
            <div class="row">
                <label for="query"></label><input type="text" name="q" id="query" class="input-block-level">
            </div>
            <br>
            <div class="row">
                <input type="submit" value="Быстрый поиск" class="btn">
            </div>

            <input type="hidden" name="type" value="full">
        </form>

        <p>
            По запросу '${query}' найдено ${fn:length(tracks)} композиций
        </p>

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


    </div>

</div>

</body>
</html>