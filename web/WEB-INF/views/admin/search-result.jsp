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
            <div class="container-fluid" style="border-bottom-style: solid;border-color: #ececec;border-bottom-width: 1px;padding: 10px">
                <div class="row-fluid">

                    <div class="span6">
                        <label for="query"></label><input type="text" name="q" id="query" class="input-block-level"
                                                          style="margin-top: 0px">

                        <input type="submit" value="Поиск" class="btn">
                    </div>


                    <div class="span3">
                        <h4>Поле</h4>
                        <input type="radio" style="margin-right: 10px" value="all" id="all-field" name="field">Полный
                        поиск<br>
                        <input type="radio" style="margin-right: 10px" value="code" name="field">Код <br>
                        <input type="radio" style="margin-right: 10px" value="artist" name="field">Артист <br>
                        <input type="radio" style="margin-right: 10px" value="composer" name="field">Автор <br>
                        <input type="radio" style="margin-right: 10px" value="name" name="field">Композиция <br>
                    </div>

                    <div class="span3 pull-left">
                        <h4>Каталог</h4>
                        <c:if test="${not empty catalogs}">
                            <ul class="nav nav-list bs-docs-sidenav">
                                <li>
                                    <input id="all-cat" type="checkbox" onchange="deselectForEachCatalog()" value="-1"
                                           name="catalog"
                                           style="margin-right: 10px;font-size: 11pt">
                                    Все
                                </li>
                                <c:forEach var="c" items="${catalogs}">
                                    <li style="font-size: 11pt">
                                        <input type="checkbox" value="${c.id}" onchange="deselectAllCatalogs()"
                                               name="catalog${c.id}" style="margin-right: 10px;font-size: 11pt">
                                            ${c.name}
                                    </li>
                                </c:forEach>
                            </ul>
                        </c:if>
                    </div>

                </div>

            </div>
        <%--</div>--%>

        <input type="hidden" id="type" name="type" value="full">
    </form>


    <script>

        var typeEl = document.getElementById('type');

        function change_type(comp) {
            typeEl.setAttribute('value', comp.id);
        }
    </script>

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
    var searchForm = document.getElementById("searcher");
    var from_page_input = document.getElementById("from-p");
    var till_page_input = document.getElementById("till-p");
    var search_input = document.getElementById("query");

    $('#all-cat').prop('checked', true);
    $('#all-field').prop('checked', true);

    function nextPage(from) {
        from_page_input.value = from;
        till_page_input.value = '${pageSize}';
        search_input.value = '${query}';
        searchForm.submit();
    }

    function deselectForEachCatalog() {
        var count =0;

        $('[type=checkbox]').each(function () {
            if ($(this).prop('checked')&&(this).id!=="all-cat") {
                $(this).prop('checked', false);
                count++;
            }

        });
           if(count==0){
               $('#all-cat').prop('checked', true);
           }
    }


    function deselectAllCatalogs() {
        $('#all-cat').prop('checked', false);
    }

</script>
</body>
</html>