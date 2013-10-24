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

        span.catalog {
            border-radius: 3;
            padding: 0 4px 0 4px;
        }

        .search-result  table.table {
            font-size: 10pt;
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

        tr.same-track td {
            border-top: none;
            /*padding: 5px 0 0 0;*/
            line-height: 15px;
        }

        td.score {
            color: #8f6e5f;
        }

        label.separated {
            margin-top: 15px;
        }

        .light {
            opacity: 0.4;
        }

        .dark {
            opacity: 1;
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

            <form id="searcher" action="/admin/action/search" method="post" class="form-search">

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

                                <label for="field-artist" class="radio separated">
                                    <input type="radio" name="field" value="artist" id="field-artist">
                                    по артисту
                                </label>

                                <label for="field-composer" class="radio">
                                    <input type="radio" name="field" value="composer" id="field-composer">
                                    по композитору
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


                            </fieldset>
                        </div>


                        <div class="span6">
                            <h4>Фильтр по каталогам</h4>

                            <div class="row">
                                <c:forEach var="p" items="${platforms}">
                                    <div class="span2">
                                        <div class="catalog-title">${p.name}</div>
                                        <fieldset>
                                            <c:forEach var="c" items="${p.catalogs}" varStatus="loop">

                                                <label for="checkbox-${loop.index}" class="checkbox">
                                                    <input type="checkbox" value="${c.id}"
                                                           name="catalog-${c.id}"
                                                           id="checkbox-${loop.index}">
                                                        ${c.name}
                                                </label>
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


                <table class="table">
                    <thead>
                    <tr>
                            <%--<th>#</th>--%>
                        <th></th>
                        <th>Код</th>
                        <th>Композиция</th>
                        <th>Исполнитель</th>
                        <th>Авторы</th>
                        <th>Мобильный контент</th>
                        <th>Публичка</th>
                        <th>Каталог</th>
                        <th></th>
                    </tr>
                    </thead>
                    <tbody>

                    <c:set var="lastComposer" value="none"/>
                    <c:set var="lastName" value="none"/>

                    <jsp:useBean id="colorRandom" class="kz.bgm.platform.utils.ColorRandom" scope="application"/>
                    <jsp:useBean id="colors" class="java.util.HashMap" scope="request"/>
                    <c:forEach var="p" items="${platforms}">

                        <c:forEach var="catalog" items="${p.catalogs}">
                            <c:set target="${colors}" property="${catalog.name}" value="${colorRandom.colorName}"/>
                        </c:forEach>
                    </c:forEach>

                    <c:forEach var="r" items="${tracks}" varStatus="s">
                        <tr class="${fn:toLowerCase(lastComposer)eq fn:toLowerCase(r.track.composer) &&
                        fn:toLowerCase(lastName)eq fn:toLowerCase(r.track.name)? 'same-track' : ''}">
                            <td class="score"><fmt:formatNumber type="number" pattern="##.##"
                                                                value="${r.score}"/></td>
                            <td>${r.track.code}</td>
                            <td>${r.track.name}</td>
                            <td>${r.track.artist}</td>
                            <td>${r.track.composer}</td>
                            <td>${r.track.mobileShare}</td>
                            <td> ${r.track.publicShare}</td>
                            <td>
                                <span class="catalog" style="background: ${colors[r.track.catalog]}">
                                        ${r.track.catalog}
                                </span>
                            </td>
                            <td class="light" id=${r.track.id}><i class="icon-wrench"></i></td>
                        </tr>

                        <c:set var="lastComposer" value="${r.track.composer}"/>
                        <c:set var="lastName" value="${r.track.name}"/>
                    </c:forEach>

                    </tbody>
                </table>

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

    var lightElements = document.getElementsByClassName("light");

    for (var i = 0; i < lightElements.length; i++) {

        lightElements[i].onmouseover = function () {
            this.className = 'dark';

        };

        lightElements[i].onmouseout = function () {
            this.className = 'light';
        };

        lightElements[i].onmousedown = function () {
            window.location.replace("/admin/view/edit-track?id=" + this.id);
        }
    }


    var submitParam = getParameterByName('submit');
    if (submitParam == 'true') {
        document.getElementById('searcher').submit();
    }

</script>
</body>
</html>