<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html xmlns="http://www.w3.org/1999/html">

<head>
    <script src="/js/jquery.js"></script>
    <script src="/js/bootstrap.js"></script>
    <script src="/js/bootstrap-fileupload.js"></script>
    <script src="/js/bootstrap-datetimepicker.min.js"></script>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-fileupload.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-datetimepicker.min.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-responsive.css" media="screen"/>

    <link href="//netdna.bootstrapcdn.com/twitter-bootstrap/2.3.1/css/bootstrap-combined.no-icons.min.css" rel="stylesheet">
    <link href="//netdna.bootstrapcdn.com/font-awesome/3.1.1/css/font-awesome.css" rel="stylesheet">
    <title>Главная</title>
</head>

<style>
    span.small {
        font-size: 10px;
    }

    #random-tracks {
        margin-top: 100px;
        padding: 10px 20px 10px 30px;
        background-color: #edf9f9;
    }

    #random-tracks li {
        padding-bottom: 7px;
    }

    #total-tracks {
        margin: 0 0 30px 185px;
        text-decoration: underline;
    }

    #total-tracks strong {
        font-size: 13pt;
    }



    .add-button a {
        text-decoration: none;
    }

    .add-button a:link {
        color: #cccccc;
    }

    .add-button a:visited {
        color: #cccccc;
    }

    .add-button a:hover {
        color: #747474;
    }

    .add-platform {
        margin-left: 20px;
    }

    .add-catalog {
        margin-left: 10px;
    }

    .enemy-platform {
        color: #4a0b00;
    }

</style>


<body>

<c:import url="navbar.jsp">
    <c:param name="index" value="active"/>
</c:import>

<div class="container">

    <div class="row">
        <div class="span7">
            <section>
                <legend>
                    Каталоги
                    <span class="add-button add-platform">
                        <a href="#add-platform"><i class="icon-plus-sign icon-large"></i> </a>
                    </span>
                </legend>


                <c:forEach var="p" items="${platforms}">

                    <h4>
                    <c:choose>
                        <c:when test="${p.rights == true}">
                            ${p.name}
                        </c:when>
                        <c:otherwise>
                            <span class="enemy-platform">
                                    ${p.name}
                            </span>
                        </c:otherwise>
                    </c:choose>



                    <span class="add-button add-catalog">
                        <a href="add-catalog?platformId=${p.id}"><i class="icon-plus-sign"></i> </a>
                    </span>
                    </h4>

                    <dl class="dl-horizontal">
                        <c:forEach var="c" items="${p.catalogs}">
                            <dt>
                                <a href="catalog?catId=${c.id}">${c.name}</a>
                            </dt>
                            <dd>
                                <ul class="inline">
                                    <li>
                                        <c:choose>
                                            <c:when test="${c.rightType eq 'AUTHOR'}">Авторские</c:when>
                                            <c:otherwise>Смежные</c:otherwise>
                                        </c:choose>
                                    </li>
                                    <li>
                            <span class="small">
                                 <i class="icon-music"></i>
                            </span>
                            <span class="small">
                                 <fmt:formatNumber type="number" maxFractionDigits="3" value="${c.tracks}"/>
                            </span>
                                    </li>
                                    <li>
                            <span class="small">
                                 <i class="icon-user"></i>
                            </span>
                            <span class="small">
                                 <fmt:formatNumber type="number" maxFractionDigits="3" value="${c.artists}"/>
                            </span>
                                    </li>
                                </ul>

                            </dd>
                        </c:forEach>

                    </dl>


                </c:forEach>

                <div id="total-tracks">
                    Всего треков:
                    <strong>
                        <fmt:formatNumber type="number" maxFractionDigits="3" value="${totalTracks}"/>
                    </strong>

                </div>

            </section>


            <section>
                <legend>
                    Входящие отчеты
                </legend>

                <dl class="dl-horizontal">
                    <c:forEach items="${reports}" var="r">
                        <dt>
                            <a href="customer-detail?customer_id=${r.customerId}">
                                    ${r.customer}
                            </a>
                        </dt>
                        <dd>
                            <ul class="inline">
                                    <%--<li>${r.sendDate}</li>--%>
                                <li>${r.reportDate}</li>
                                <li>
                                    <c:choose>
                                        <c:when test="${r.reportType ==1}">
                                            Публичка
                                        </c:when>
                                        <c:otherwise>
                                            Мобильный
                                        </c:otherwise>
                                    </c:choose>
                                </li>
                                <li>
                                    <c:choose>
                                        <c:when test="${r.reportPeriod ==1}">
                                            Квартальный
                                        </c:when>
                                        <c:otherwise>
                                            Ежемесячный
                                        </c:otherwise>
                                    </c:choose>
                                </li>
                                <li>
                                    <c:choose>
                                        <c:when test="${r.calculated ==true}">
                                            <i class="icon-ok"></i>
                                        </c:when>
                                        <c:otherwise>
                                            <i class="icon-remove"></i>
                                        </c:otherwise>
                                    </c:choose>
                                </li>
                            </ul>
                        </dd>

                    </c:forEach>
                </dl>

            </section>


            <section>
                <legend>
                    Исходящие отчеты
                </legend>
            </section>
        </div>

        <div class="span4">
            <div id="random-tracks">
                <h4>Случайные треки:</h4>

                <ul class="unstyled">
                    <c:forEach var="t" items="${randomTracks}">
                        <li>
                            <a href="#">
                                    ${t.name}<c:if test="${not empty t.artist}">: ${t.artist}</c:if>
                            </a>
                        </li>
                    </c:forEach>
                </ul>
            </div>
        </div>

    </div>

</div>

<c:import url="footer.jsp"/>


</body>
</html>