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
    <form id="searcher" action="/admin/action/search" method="post">
        <input type="hidden" name="from" id="from-p">
        <input type="hidden" name="pageSize" id="till-p">

        <%--<div class="hero-unit" style="padding: 10px">--%>
        <div class="container-fluid"
             style="border-bottom-style: solid;border-color: #ececec;border-bottom-width: 1px;padding: 10px">
            <div class="row-fluid">

                <div class="span6">
                    <label for="query"></label><input type="text" name="q" id="query" class="input-block-level"
                                                      style="margin-top: 0px">

                    <input type="submit" value="Поиск" class="btn">
                </div>


                <div class="span3" align="right">
                    <h4>Поле</h4>
                    Полный поиск <input type="radio" style="margin-left: 10px" value="all" id="all-field" name="field">
                    <br>
                    Код<input type="radio" style="margin-left: 10px" value="code" name="field"> <br>
                    Артист<input type="radio" style="margin-left: 10px" value="artist" name="field"> <br>
                    Автор<input type="radio" style="margin-left: 10px" value="composer" name="field"> <br>
                    Композиция<input type="radio" style="margin-left: 10px" value="name" name="field"> <br>
                </div>

                <div class="span3 pull-left" style="padding-left: 15px">
                    <h4>Платформа</h4>
                    <ul class="nav nav-list bs-docs-sidenav">
                        <div style="height:220px;width:170px;border:1px solid #ececec;overflow:auto;padding: 5px">
                            <li>
                                <input id="all-cat" type="checkbox" onchange="deselectForEachCatalog()" value="-1"
                                       name="catalog"
                                       style="margin-right: 10px;font-size: 11pt">
                                Все
                            </li>

                            <c:forEach var="p" items="${platforms}">

                                <h4>${p.name}</h4>


                                <c:forEach var="c" items="${p.catalogs}">

                                    <li style="font-size: 11pt">
                                        <input type="checkbox" value="${c.id}" onchange="deselectAllCatalogs()"
                                               name="catalog${c.id}" style="margin-right: 10px;font-size: 11pt">
                                            ${c.name}
                                    </li>
                                </c:forEach>

                            </c:forEach>
                        </div>
                    </ul>
                </div>

            </div>

        </div>
        <%--</div>--%>

    </form>


    <script>

        var typeEl = document.getElementById('type');

        function change_type(comp) {
            typeEl.setAttribute('value', comp.id);
        }
    </script>

    <%--<div class="pagination pagination-centered">--%>
    <%--<ul>--%>
    <%--<c:choose>--%>
    <%--<c:when test="${from >= pageSize}">--%>
    <%--<li><a href="catalog-update?from=${from - pageSize}">&laquo;</a></li>--%>
    <%--</c:when>--%>
    <%--<c:otherwise>--%>
    <%--<li class="disabled"><a href="#">&laquo;</a></li>--%>
    <%--</c:otherwise>--%>
    <%--</c:choose>--%>

    <%--<c:forEach var="i" begin="1" end="${(fn:length(tracks) / pageSize) + 1}" step="1"--%>
    <%--varStatus="status">--%>
    <%--<li class="${from == (i - 1) * pageSize ? 'active' : ''}">--%>
    <%--<a onclick="nextPage(${(i - 1) * pageSize})">${i}</a>--%>
    <%--</li>--%>
    <%--</c:forEach>--%>

    <%--<c:choose>--%>
    <%--<c:when test="${from + pageSize < update.crossing}">--%>
    <%--<li><a onclick="nextPage(${from + pageSize})">&raquo;</a></li>--%>
    <%--</c:when>--%>
    <%--<c:otherwise>--%>
    <%--<li class="disabled"><a href="#">&raquo;</a></li>--%>
    <%--</c:otherwise>--%>
    <%--</c:choose>--%>
    <%--</ul>--%>
    <%--</div>--%>


    <span class="label">
        По запросу '${query}' найдено ${fn:length(tracks)} композиций
    </span>

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

    <%--<div class="pagination pagination-centered">--%>
    <%--<ul>--%>
    <%--<c:choose>--%>
    <%--<c:when test="${from >= pageSize}">--%>
    <%--<li><a href="catalog-update?from=${from - pageSize}">&laquo;</a></li>--%>
    <%--</c:when>--%>
    <%--<c:otherwise>--%>
    <%--<li class="disabled"><a href="#">&laquo;</a></li>--%>
    <%--</c:otherwise>--%>
    <%--</c:choose>--%>

    <%--<c:forEach var="i" begin="1" end="${(fn:length(tracks) / pageSize) + 1}" step="1"--%>
    <%--varStatus="status">--%>
    <%--<li class="${from == (i - 1) * pageSize ? 'active' : ''}">--%>
    <%--<a href="search-result?from=${(i - 1) * pageSize}">${i}</a>--%>
    <%--</li>--%>
    <%--</c:forEach>--%>

    <%--<c:choose>--%>
    <%--<c:when test="${from + pageSize < update.crossing}">--%>
    <%--<li><a href="search-result?from=${from + pageSize}">&raquo;</a></li>--%>
    <%--</c:when>--%>
    <%--<c:otherwise>--%>
    <%--<li class="disabled"><a href="#">&raquo;</a></li>--%>
    <%--</c:otherwise>--%>
    <%--</c:choose>--%>
    <%--</ul>--%>
    <%--</div>--%>


</div>
<script>
    var searchForm = document.getElementById("searcher");
    var from_page_input = document.getElementById("from-p");
    var till_page_input = document.getElementById("till-p");
    var search_input = document.getElementById("query");

    function updateParams() {
        var isAny = false;

        $('[type=checkbox]').each(function () {
            var param = getParameterByName((this).name);
            if (param == this.value) {
                $(this).prop('checked', true);
                isAny = true;
            }
        });

        if (isAny == false) {
            $('#all-cat').prop('checked', true);
        }

        var field = getParameterByName('field');
        var filedEmpty = true;
        $('[type=radio]').each(function () {
            if (this.value == field) {
                filedEmpty = false;
                $(this).prop('checked', true);
            }
        });

        if (filedEmpty == true) {
            $('#all-field').prop('checked', true);
        }

        var query = getParameterByName('q');
        search_input.value = query;

    }


    function nextPage(from) {
        from_page_input.value = from;
        till_page_input.value = '${pageSize}';
        search_input.value = "${query}";
        searchForm.submit();
    }

    function deselectForEachCatalog() {
        var count = 0;

        $('[type=checkbox]').each(function () {
            if ($(this).prop('checked') && (this).id !== "all-cat") {
                $(this).prop('checked', false);
                count++;
            }

        });
        if (count == 0) {
            $('#all-cat').prop('checked', true);
        }
    }


    function deselectAllCatalogs() {
        $('#all-cat').prop('checked', false);
    }

    updateParams();


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