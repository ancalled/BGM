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
    <style>
        #search-panel  table.table {
            font-size: 10pt;
        }

        /*a.toggler {*/
            /*text-decoration: none;*/
            /*border-bottom: 1px dotted;*/
        /*}*/
    </style>
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
        <input type="hidden" name="extend" id="extend-search" value=" ">

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
                <a class="accordion-toggle toggler"
                   data-toggle="collapse"
                   data-parent="#accordion2"
                   id="extra"
                   href="#collapseOne">Дополнительно</a>
            </div>
            <div id="collapseOne" class="accordion-body collapse in span9" style="margin-left: 5px">
                <div class="accordion-inner">
                    <div class="container" style="background: #edf9f9; margin-left: 0">

                        <div class=" span3 pull-left">
                            <h4>Поиск по</h4>
                            <input type="radio" style="margin-right: 10px;margin-top: -2px" value="all" id="all-field"
                                   name="field">всем полям<br>
                            <input type="radio" style="margin-right: 10px;margin-top: -2px" value="code" name="field">коду
                            <br>
                            <input type="radio" style="margin-right: 10px;margin-top: -2px" value="artist" name="field">артисту
                            <br>
                            <input type="radio" style="margin-right: 10px;margin-top: -2px" value="composer"
                                   name="field">автору <br>
                            <input type="radio" style="margin-right: 10px;margin-top: -2px" value="name" name="field">композиции
                            <br>
                        </div>

                        <div class="span4">
                            <c:forEach var="p" items="${platforms}">
                                <div class=" pull-left">

                                    <h4>${p.name}</h4>

                                    <div style="height:145px;width:140px;overflow:auto;padding: 5px">
                                        <ul class="nav nav-list bs-docs-sidenav">

                                            <c:forEach var="c" items="${p.catalogs}">
                                                <c:if test="${c.rightType eq customer.rightType or customer.rightType eq 'AUTHOR_RELATED'}">
                                                    <li style="font-size: 11pt">
                                                        <input type="checkbox" value="${c.id}"
                                                               name="catalog${c.id}"
                                                               style="margin-right: 10px;font-size: 11pt;margin-top: -2px">
                                                            ${c.name}
                                                    </li>

                                                </c:if>
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

    <div id="search-panel">
        <c:if test="${not empty query}">
            <div class="label">По запросу '${query}' найдено ${fn:length(tracks)} композиций</div>

            <form id="chooser" action="/customer/action/add-to-basket" method="post">
                <table class="table">
                    <thead>
                    <tr>
                        <th>#</th>
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
                        <th><input type="submit" value="В корзину" class="btn"></th>
                    </tr>
                    </thead>
                    <tbody>

                    <c:forEach var="t" items="${tracks}" varStatus="s">
                        <tr>
                            <td>${s.index + 1}</td>
                            <td>${t.code}</td>
                            <td>${t.name}</td>
                            <td>${t.artist}</td>
                            <td>${t.composer}</td>

                            <c:choose>
                                <c:when test="${customer.customerType eq 'MOBILE_AGGREGATOR'}">
                                    <td>${t.mobileShare}</td>
                                </c:when>
                                <c:when test="${customer.customerType eq 'PUBLIC_RIGHTS_SOCIETY'}">
                                    <td> ${t.publicShare}</td>
                                </c:when>
                            </c:choose>

                            <td>${t.catalog}</td>
                            <c:set var="contains" value="false"/>
                            <c:forEach var="uc" items="${customer_tracks}">
                                <c:if test="${uc eq t.id}">
                                    <td><i class="icon-shopping-cart"></i></td>
                                    <c:set var="contains" value="true"/>
                                </c:if>
                            </c:forEach>

                            <c:choose>
                                <c:when test="${not contains}">
                                    <td>
                                        <input type="checkbox" name="check_${t.id}" value="${t.id}"/>
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

    </div>



</div>


<script>



    var typeEl = document.getElementById('type');
    var searchForm = document.getElementById("searcher");
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
        collapse.show();
    });

    collapse.on('shown', function () {
        extend.val('true');
    });

    collapse.on('hidden', function () {
        extend.val('false');
    });


    function extendedSearchUpdate() {
        var isExtended = getParameterByName('extended');
        var collapse = $('#collapseOne');
        if (isExtended != null) {
            if (isExtended == 'true') {
                collapse.show();
                collapse.collapse('show');
                extend.val('true');
            } else {
                collapse.hide();
                collapse.collapse('hide');
                extend.val('false');
            }
        }
    }

    function nextPage(from) {
        from_page_input.value = from;
        till_page_input.value = '${pageSize}';
        search_input.value = "${query}";
        searchForm.submit();
    }

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