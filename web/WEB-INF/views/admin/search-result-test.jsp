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


        <div class="row span9" style="background:rgba(223,223,223,0.31);border-radius: 5px;padding: 4px">
            <label for="query"></label><input type="text" name="q" id="query" class="input-block-level span8"
                                              style="margin-top: 10px">

            <input type="submit" value="Поиск" class="btn">


            <div class="container-fluid">
            <ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu"
            style="display: block; position: static; margin-bottom: 5px; *width: 180px;">
            <li><a tabindex="-1" id="code" onclick="change_type(this)">Код композиции</a></li>
            <li><a tabindex="-1" id="artist" onclick="change_type(this)">Артист</a></li>
            <li><a tabindex="-1" id="composition" onclick="change_type(this)">Композиция</a></li>
            <li class="divider"></li>
            <li><a tabindex="-1" id="full" onclick="change_type(this)">Полный поиск</a></li>
            </ul>
            </div>

            <ul class="nav nav-pills">
                <li class="dropdown">
                    <a class="dropdown-toggle" id="catalog" role="button" data-toggle="dropdown" href="#">Каталог<b
                            class="caret"></b></a>
                    <ul id="menu1" class="dropdown-menu" role="menu" aria-labelledby="catalog">
                        <c:forEach var="c" items="${catalogs}">

                            <li role="presentation"><a role="menuitem" tabindex="-1" id="${c.id}"
                                                       onclick="setCatalog(this)" href="">${c.name}</a></li>

                        </c:forEach>
                    </ul>
                </li>

            </ul>
            <script>
                var catalogComp = document.getElementById("catalog");

              function setCatalog(cat) {
                    catalogComp.textContent = cat.textContent;
                }
            </script>

            <%--<c:if test="${not empty catalogs}">--%>
            <%--<label>--%>
            <%--<select name="catalog", style="margin-left: 20px">--%>
            <%--<option value="all">Все</option>--%>
<%----%>
            <%--<c:forEach var="c" items="${catalogs}">--%>
<%----%>

            <%--<option value="${c.id}">${c.name}</option>--%>
<%----%>
            <%--</c:forEach>--%>
            <%--</select>--%>
            <%--</label>--%>


            <%--</c:if>--%>


        </div>


        <input type="hidden" id="type" name="type" value="full">
    </form>

</div>
<div class="container" style="margin-top: 25px">
    <c:if test="${not empty tracks}">

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

    </c:if>

</div>
<script>


    var typeEl = document.getElementById('type');

    function change_type(comp) {
        typeEl.setAttribute('value', comp.id);
    }


    var searchForm = document.getElementById("searcher");
    var from_page_input = document.getElementById("from-p");
    var till_page_input = document.getElementById("till-p");
    var search_input = document.getElementById("query");

    $(".collapse").collapse()

    function nextPage(from) {
        from_page_input.value = from;
        till_page_input.value = '${pageSize}';
        search_input.value = '${query}';
        searchForm.submit();
    }
</script>
</body>
</html>