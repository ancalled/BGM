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
<c:import url="navbar.jsp">
    <c:param name="search" value="active"/>
</c:import>

<div class="container">


    <div class="row text-left">
        <legend>
            Поиск композиций
        </legend>
    </div>
    <form action="/admin/action/search" method="post">

        <label for="query"></label><input type="text" name="q" id="query" class="input-block-level">

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

        <input type="hidden" id="type" name="type" value="full">
    </form>


    <script>

        var typeEl = document.getElementById('type');

        function change_type(comp) {
            typeEl.setAttribute('value', comp.id);
        }
    </script>

    <c:if test="${not empty tracks}">
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
    </c:if>

</div>

</body>
</html>