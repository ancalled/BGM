<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<html>
<head>
    <script src="/js/jquery.js"></script>
    <%--<script src="/js/bootstrap.js"></script>--%>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.css" media="screen"/>
    <title>Поиск</title>
    <style>

        div.search-params {
            background: #edf9f9;
            margin-left: 0;
            padding: 5px 0 10px 0;
        }

        .search-params label {
            display: block;
        }

        .search-result  table.table {
            font-size: 10pt;
        }

        span.catalog {
            border-radius: 3;
            padding: 0 4px 0 4px;
        }

        div.catalog-title {
            font-weight: bold;
            margin: 5px 0 8px 0;
            color: #8f6e5f;
        }

        div.toggler {
            margin: 10px 0 5px 0
        }

        .toggler a {
            text-decoration: none;
            color: #008ace;
            border-bottom: 1px dotted #008ace;
            margin: 0 2px 0 2px;
            cursor: pointer;
        }

        .input-block-level {
            width: 540px;
        }

        td.centered {
            text-align: center;
        }

        td.score {
            color: #8f6e5f;
        }

        label.separated {
            margin-top: 15px;
        }

        tr.same-track td {
            border-top: none;
            /*padding: 5px 0 0 0;*/
            line-height: 15px;
        }

    </style>
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

    <div class="span10">

        <form id="searcher" action="/customer/action/search" method="post" class="form-search">

            <input type="hidden" name="from" id="from-p">
            <input type="hidden" name="pageSize" id="till-p">
            <input type="hidden" name="extend" id="extend-search" value="">

            <input type="text" name="q" id="query" class="input-block-level">
            <input type="submit" value="Поиск" class="btn">

            <div class="toggler">
                <a class="accordion-toggle"
                   data-toggle="collapse"
                   data-parent="#accordion2"
                   id="extra"
                   href="#">
                    Дополнительно
                </a>
            </div>

            <div id="collapseOne" class="search-params collapse in">
                <div class="container search-params">

                    <div class="span4">
                        <h4>Поиск</h4>
                        <fieldset>
                            <label for="field-all" class="radio">
                                <input type="radio" name="field" value="all" id="field-all">
                                по всем полям
                            </label>

                            <label for="field-name" class="radio">
                                <input type="radio" name="field" value="track" id="field-name">
                                по названию трека
                            </label>

                            <label for="field-code" class="radio">
                                <input type="radio" name="field" value="code" id="field-code">
                                по коду
                            </label>

                            <label for="field-artist-track" class="radio separated">
                                <input type="radio" name="field" value="artist_track" id="field-artist-track">
                                по артисту и треку (через «;»)
                            </label>

                            <label for="field-composer-track" class="radio">
                                <input type="radio" name="field" value="composer_track" id="field-composer-track">
                                по композитору и треку (через «;»)
                            </label>

                            <%--<label for="field-artist" class="radio separated">--%>
                            <%--<input type="radio" name="field" value="artist" id="field-artist">--%>
                            <%--все треки артиста--%>
                            <%--</label>--%>

                            <%--<label for="field-composer" class="radio">--%>
                            <%--<input type="radio" name="field" value="composer" id="field-composer">--%>
                            <%--все треки композитора--%>
                            <%--</label>--%>
                        </fieldset>
                    </div>

                    <jsp:useBean id="colorMap" class="java.util.HashMap"/>
                    <div class="span6">
                        <h4>Фильтр по каталогам</h4>

                        <div class="row">
                            <c:forEach var="p" items="${platforms}">
                                <div class="span2">
                                    <div class="catalog-title">${p.name}</div>

                                    <fieldset>
                                        <c:forEach var="c" items="${p.catalogs}" varStatus="loop">
                                            <c:set target="${colorMap}" property="${c.name}" value="${c.color}"/>
                                            <c:if test="${c.rightType eq customer.rightType or customer.rightType eq 'AUTHOR_RELATED'}">
                                                <label for="checkbox-${loop.index}" class="checkbox">
                                                    <input type="checkbox" value="${c.id}"
                                                           name="catalog-${c.id}"
                                                           id="checkbox-${loop.index}">
                                                        ${c.name}
                                                </label>
                                            </c:if>
                                        </c:forEach>
                                    </fieldset>
                                </div>
                            </c:forEach>
                        </div>
                    </div>

                </div>

            </div>
        </form>
    </div>

</div>


<div class="row">
    <div class="span10 search-result">

        <c:if test="${not empty query}">

            <legend>
                Результат поиска (${fn:length(tracks) gt 99 ? 'больше 100' : fn:length(tracks)} треков)
            </legend>


            <form id="chooser" action="/customer/action/add-to-basket" method="post">
                <table class="table">
                    <thead>
                    <tr>
                            <%--<th>#</th>--%>
                        <th></th>
                        <th>Код</th>
                        <th>Композиция</th>
                        <th>Исполнитель</th>
                        <th>Авторы</th>
                        <c:choose>
                            <c:when test="${customer.customerType eq 'MOBILE_AGGREGATOR'}">
                                <th>Мобильный контент</th>
                            </c:when>
                            <c:when test="${customer.customerType eq 'PUBLIC_RIGHTS_SOCIETY'}">
                                <th>Публичка</th>
                            </c:when>
                        </c:choose>

                        <th>Каталог</th>
                        <th>
                            <button class="btn btn-block" type="submit" style="width: 30px"><i
                                    class="icon-shopping-cart"></i></button>

                        </th>
                    </tr>
                    </thead>
                    <tbody>

                    <c:set var="lastComposer" value="none"/>
                    <c:set var="lastName" value="none"/>
                    <c:set var="lastArtist" value="none"/>
                    <c:set var="lastCode" value="none"/>

                    <c:forEach var="r" items="${tracks}" varStatus="s">
                        <tr class="${fn:toLowerCase(lastComposer)eq fn:toLowerCase(r.track.composer) &&
                        fn:toLowerCase(lastName)eq fn:toLowerCase(r.track.name)||
                        fn:toLowerCase(lastArtist)eq fn:toLowerCase(r.track.artist&&
                        fn:toLowerCase(lastCode)eq fn:toLowerCase(r.track.code))? 'same-track' : ''}">

                                <%--<td>${s.index + 1}</td>--%>
                            <td class="score"><fmt:formatNumber type="number" pattern="##.##"
                                                                value="${r.score}"/></td>
                            <td>${r.track.code}</td>
                            <td>${r.track.name}</td>
                            <td>${r.track.artist}</td>
                            <td>${r.track.composer}</td>

                            <c:choose>
                                <c:when test="${customer.customerType eq 'MOBILE_AGGREGATOR'}">
                                    <td>${r.track.mobileShare}</td>
                                </c:when>
                                <c:when test="${customer.customerType eq 'PUBLIC_RIGHTS_SOCIETY'}">
                                    <td> ${r.track.publicShare}</td>
                                </c:when>
                            </c:choose>

                            <td>
                                <span class="catalog"  style="background: <c:out value="${colorMap[r.track.catalog]}"/>">
                                        ${r.track.catalog}
                                </span>
                            </td>
                            <c:set var="contains" value="false"/>
                            <c:forEach var="uc" items="${customer_tracks}">
                                <c:if test="${uc eq r.track.id}">
                                    <td class="centered"><i class="icon-ok"></i></td>
                                    <c:set var="contains" value="true"/>
                                </c:if>
                            </c:forEach>

                            <c:choose>
                                <c:when test="${not contains}">
                                    <td class="centered">
                                        <input type="checkbox" name="check_${r.track.id}" value="${r.track.id}"/>
                                    </td>
                                </c:when>
                                <c:otherwise>

                                </c:otherwise>
                            </c:choose>

                        </tr>
                        <c:set var="lastComposer" value="${r.track.composer}"/>
                        <c:set var="lastName" value="${r.track.name}"/>
                        <c:set var="lastArtist" value="${r.track.artist}"/>
                        <c:set var="lastCode" value="${r.track.code}"/>

                    </c:forEach>

                    </tbody>
                </table>
            </form>


        </c:if>
    </div>
</div>

<hr/>
</div>


<script>


    var typeEl = document.getElementById('type');
    //    var searchForm = document.getElementById("searcher");
    var from_page_input = document.getElementById("from-p");
    var till_page_input = document.getElementById("till-p");
    var collapse = $('#collapseOne');
    var extend = $('#extend-search');

    var search_input = document.getElementById("query");
    var query = getParameterByName('q');
    search_input.value = query;

    updateParams();
    extendedSearchUpdate();

    $('#extra').click(function () {
        if (collapse.is(":visible")) {
            collapse.hide();
            extend.val('false');
        } else {
            collapse.show();
            extend.val('true');
        }

    });


    function extendedSearchUpdate() {
        var isExtended = getParameterByName('extend');
        var collapse = $('#collapseOne');
        if (isExtended != null) {
            if (isExtended == 'true') {
                collapse.show();
                extend.val('true');
            } else {
                collapse.hide();
                extend.val('false');
            }
        }
    }

    <%--function nextPage(from) {--%>
    <%--from_page_input.value = from;--%>
    <%--till_page_input.value = '${pageSize}';--%>
    <%--search_input.value = "${query}";--%>
    <%--searchForm.submit();--%>
    <%--}--%>

    function updateParams() {
        $('[type=checkbox]').each(function () {
            var param = getParameterByName((this).name);
            if (param == this.value) {
                $(this).prop('checked', true);
            }
        });

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