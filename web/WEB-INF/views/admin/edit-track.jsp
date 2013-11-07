<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <script src="/js/jquery.js"></script>
    <script src="/js/bootstrap.js"></script>
    <script src="/js/bootstrap-fileupload.js"></script>
    <script src="/js/plugin-parser.js"></script>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-fileupload.css" media="screen"/>
    <title>Корректировка композиции</title>


</head>
<body>

<c:import url="navbar.jsp">
    <c:param name="search" value="active"/>
</c:import>


<div class="container">


    <div class="row text-left">
        <legend>
            Редактирование композиции
        </legend>
    </div>
    <div class="span4">

        <form action="/admin/action/update-track" method="post">
            <input type="hidden" name="track-id" value="${track.id}">
            <label>
                Код
                <input type="text" name="code" class="input-block-level" value="${track.code}" required="true">
            </label>
            <label>
                Имя
                <input type="text" name="name" class="input-block-level" value="${track.name}" required="true">
            </label>
            <label>
                Артист
                <input type="text" name="artist" class="input-block-level" value="${track.artist}" required="true">
            </label>
            <label>
                Автор
                <input type="text" name="composer" class="input-block-level" value="${track.composer}" required="true">
            </label>
            <label>
                Мобильный контент
                <input type="text" name="mobile-share" class="input-block-level" value="${track.mobileShare}"
                       required="true">
            </label>
            <label>
                Публичка
                <input type="text" name="public-share" class="input-block-level" value="${track.publicShare}"
                       required="true">
            </label>
            <label>
                Платформа
                <select id="platforms">
                    <c:forEach var="pl" items="${platforms}" varStatus="s">
                        <option value="${pl.name}">${pl.name}</option>
                    </c:forEach>

                </select>
            </label>

            <label>
                Каталог <br>
                <select name="catalog" id="catalogs">
                    <c:forEach var="pl" items="${platforms}" varStatus="s">

                        <c:forEach var="cat" items="${pl.catalogs}" varStatus="s">
                            <option class="${pl.name}" id="${cat.id}">${cat.name}</option>
                        </c:forEach>

                    </c:forEach>

                </select>
            </label>
            <input type=hidden id="cat-id" name="cat-id">
            <br>
            <button class="btn" type="submit">Изменить</button>
        </form>
    </div>

</div>

<script>
    var catalogSelect = document.getElementById("catalogs");
    var catalogs = catalogSelect.getElementsByTagName('*');
    var catIdEL = document.getElementById("cat-id");

    catalogSelect.onchange = function () {
        $('#cat-id').val(this.options[this.selectedIndex].id);
    }

    for (var i = 0; i < catalogs.length; i++) {
        if (catalogs[i].textContent == getParameterByName('catalog')) {
            catalogs[i].selected = true;
            $('#cat-id').val(catalogs[i].id);
            break;
        }
    }


    var platformSelect = document.getElementById("platforms");
    platformSelect.selectedIndex = 1;

    platformSelect.onchange = function () {
        var platformName = this.value;

        var catalogs = catalogSelect.getElementsByTagName('*');
        var firstSelected = false;

        for (var i = 0; i < catalogs.length; i++) {
            if (catalogs[i].className.toLowerCase() != platformName.toLowerCase()) {
                catalogs[i].style.display = 'none';
            } else {
                catalogs[i].style.display = 'block';

                if (firstSelected == false) {
                    catalogSelect.selectedIndex = i;
                    firstSelected = true;
                }
            }
        }


    }

    function getParameterByName(name) {
        name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
        var regexS = "[\\?&]" + name + "=([^&#]*)";
        var regex = new RegExp(regexS);
        var results = regex.exec(window.location.search);
        if (results == null)
            return "";
        else
            return decodeURIComponent(results[1].replace(/\+/g, " "));
    }
</script>

</body>
</html>