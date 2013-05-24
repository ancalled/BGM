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


    <div class="row">

        <legend>
            Поиск композиций
        </legend>


        <form action="../action/search" method="post">
            <div class="row">
                <div class="container span3">
                    <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu"
                        style="display: block; position: static; margin-bottom: 5px; *width: 180px;">
                        <li><a tabindex="-1" id="code" onclick="change_type(this)">Код композиции</a></li>
                        <li><a tabindex="-1" id="artist" onclick="change_type(this)">Артист</a></li>
                        <li><a tabindex="-1" id="composition" onclick="change_type(this)">Композиция</a></li>
                        <li class="divider"></li>
                        <li><a tabindex="-1" id="full" onclick="change_type(this)">Полный поиск</a></li>
                    </ul>
                </div>
                <div class="container span8">
                    <label for="query"></label><input type="text" name="q" id="query" class="input-block-level">
                </div>

            </div>
            <br>

            <div class="row">
                <input type="submit" value="Поиск" class="btn">

            </div>
            <br>


            <input type="hidden" name="type" value="full">
        </form>

        <p>
            По запросу '${query}' найдено ${fn:length(tracks)} композиций
        </p>

        <table class="table">
            <form id="chooser" action="/customer/action/add-to-basket" method="post">

                <thead>
                <tr>
                    <th>Код</th>
                    <th>Композиция</th>
                    <th>Исполнитель</th>
                    <th>Авторы</th>
                    <th>Мобильный контент</th>
                    <th>Публичка</th>
                    <th>Каталог</th>
                    <th><input type="submit" value="Доваить треки" class="btn"></th>
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
                        <td><input type="checkbox" name="check_${t.id}" onclick="choseTrack(this)" value="${t.id}"/>
                        </td>
                    </tr>
                </c:forEach>

                </tbody>


            </form>
        </table>


    </div>

</div>

<script>
    var elForm = document.getElementById("chooser");

    function choseTrack(track) {
        if (track.checked) {
            var input = document.createElement('input');
            input.type = 'hidden';
            input.name = track.track;
            input.id = track.track;
            input.value = track.value;
            elForm.appendChild(input);
        } else {
            var oldInput = document.getElementById(track.track);
            elForm.removeChild(oldInput);
        }
    }

    var typeEl = document.getElementById('type');

    function change_type(comp) {
        typeEl.setAttribute('value', comp.id);
    }
</script>
</body>
</html>