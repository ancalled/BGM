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
        <a class="brand" href="/customer/">BGM Platform</a>
        <ul class="nav">
            <li><a href="/customer/">Главная</a></li>
            <li><a href="/customer/reports.html">Отчеты</a></li>
            <li class="active"><a href="/customer/search.html">Поиск</a></li>
            <li><a href="/customer/action/logout">Выход</a></li>
        </ul>
    </div>
</div>

<div class="container">


    <div class="row">

        <legend>
            Поиск композиций
        </legend>

        <form action="../action/search" method="post">
            <div class="row">
                <label for="query"></label><input type="text" name="q" id="query" class="input-block-level">
            </div>


            <div class="row">
                <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu"
                    style="display: block; position: static; margin-bottom: 5px; *width: 180px;">
                    <li><a tabindex="-1" id="code" onclick="change_type(this)">Код композиции</a></li>
                    <li><a tabindex="-1" id="artist" onclick="change_type(this)">Артист</a></li>
                    <li><a tabindex="-1" id="composition" onclick="change_type(this)">Композиция</a></li>
                    <li class="divider"></li>
                    <li><a tabindex="-1" id="full" onclick="change_type(this)">Полный поиск</a></li>
                </ul>
            </div>
            <br>
            <div class="row">
                <input type="submit" value="Поиск" class="btn">
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
                    <td>${t.code}</td>
                    <td>${t.name}</td>
                    <td>${t.artist}</td>
                    <td>${t.composer}</td>
                    <td>${t.mobileShare}</td>
                    <td>${t.publicShare}</td>
                    <td>${t.catalog}</td>
                </tr>
            </c:forEach>

            </tbody>
        </table>


    </div>

</div>
<script>

    var typeEl = document.getElementById('type');

    function change_type(comp) {
        typeEl.setAttribute('value', comp.id);
    }
</script>
</body>
</html>