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
    <form id="searcher" action="/customer/action/search" method="post">
        <input type="hidden" name="from" id="from-p">
        <input type="hidden" name="pageSize" id="till-p">

        <%--<div class="hero-unit" style="padding: 10px">--%>
        <div class="row" style="margin-bottom: 0px">
            <div class="span8" style="margin-right: 4px">

                <label for="query"></label><input type="text" name="q" id="query" class="input-block-level"
                                                  style="margin-top: 0px">
            </div>
                <input type="submit" value="Поиск" class="btn" style="margin-top: 4px">


        </div>
        <div class="row" style="margin-top: -5px">
            <div class="accordion-heading" style="margin-left: 5px">
                <a class="accordion-toggle" data-toggle="collapse" data-parent="#accordion2" id="extra"
                   href="#collapseOne" style=" text-decoration: none">
                    Расширенный
                </a>
            </div>
            <div id="collapseOne" class="accordion-body collapse in span9" style="margin-left: 5px">
                <div class="accordion-inner">
                    <div class="container" style="background: #edf9f9; margin-left: 0">

                        <div class=" span3 pull-left">
                            <h4>Ищщем по</h4>
                            <input type="radio" style="margin-right: 10px;margin-top: -2px" value="all" id="all-field"
                                   name="field">всем полям<br>
                            <input type="radio" style="margin-right: 10px;margin-top: -2px" value="code" name="field">коду <br>
                            <input type="radio" style="margin-right: 10px;margin-top: -2px" value="artist" name="field">артисту <br>
                            <input type="radio" style="margin-right: 10px;margin-top: -2px" value="composer" name="field">автору <br>
                            <input type="radio" style="margin-right: 10px;margin-top: -2px" value="name" name="field">композиции <br>
                        </div>

                        <%--<h4>Платформа</h4>--%>
                        <%--<li>--%>
                        <%--<input id="all-cat" type="checkbox" onchange="deselectForEachCatalog()"--%>
                        <%--value="-1"--%>
                        <%--name="catalog"--%>
                        <%--style="margin-right: 10px;font-size: 11pt">--%>
                        <%--Все--%>
                        <%--</li>--%>

                        <div class="span4">
                        <c:forEach var="p" items="${platforms}">
                            <div class=" pull-left" >

                                <h4>${p.name}</h4>

                                <div style="height:145px;width:140px;overflow:auto;padding: 5px">
                                    <ul class="nav nav-list bs-docs-sidenav">

                                        <c:forEach var="c" items="${p.catalogs}">
                                            <li style="font-size: 11pt">
                                                <input type="checkbox" value="${c.id}"
                                                       name="catalog${c.id}" style="margin-right: 10px;font-size: 11pt;margin-top: -2px">
                                                    ${c.name}
                                            </li>

                                        </c:forEach>

                                    </ul>
                                </div>
                            </div>
                        </c:forEach>
                        </div>

                    </div>

                </div>
            </div>
        </div>


    </form>

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

    <c:if test="${not empty query}">
    <span class="label">

            По запросу '${query}' найдено ${fn:length(tracks)} композиций

    </span>

    <form id="chooser" action="/customer/action/add-to-basket" method="post">
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
                <th><input type="submit" value="В корзину" class="btn"></th>
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
                    <c:set var="found_in_box" value="false"/>
                    <c:forEach var="uc" items="${user_track_ids}">
                        <c:if test="${uc==t.id}">
                            <td><i class="icon-shopping-cart"></i></td>
                            <c:set var="found_in_box" value="true"/>
                        </c:if>
                    </c:forEach>

                    <c:choose>
                        <c:when test="${found_in_box!=true}">
                            <td>
                                <input type="checkbox" name="check_${t.id}" onclick="addTrackToBasket(this)"
                                       value="${t.id}"/>
                            </td>
                        </c:when>
                        <c:otherwise>

                        </c:otherwise>
                    </c:choose>

                </tr>
            </c:forEach>

            </tbody>
        </table>
    </form>
</c:if>
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

    var typeEl = document.getElementById('type');

    function change_type(comp) {
        typeEl.setAttribute('value', comp.id);
    }

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

//    function deselectForEachCatalog() {
//        var isAny = false;
//
//        $('[type=checkbox]').each(function () {
//            if ($(this).prop('checked')){
//                isAny=true;
//            }
//        });
//
//        if(isAny==false){
//
//        }
//    }

    $('#extra').click(function () {
        $('#collapseOne').show();
    })
    $('#collapseOne').hide();
    $('#collapseOne').collapse();

    function changeCatalog() {
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


    var elForm = document.getElementById("chooser")

    function addTrackToBasket(checkbox) {
        if (checkbox.checked) {
            var input = document.createElement('input');
            input.type = 'hidden';
            input.name = checkbox.name;
            input.id = checkbox.value;
            input.value = checkbox.value;
            elForm.appendChild(input);
        } else {
            var oldInput = document.getElementById(checkbox.value);
            elForm.removeChild(oldInput);
        }
    }


</script>
</body>
</html>