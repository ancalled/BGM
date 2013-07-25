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
    <title>Главная</title>
</head>

<style>
    span.small {
        font-size: 10px;
    }

    li.report a {
        text-decoration: underline;
    }

    li.sent {
        color: #114713;
    }

    li.sent a {
        color: #114713;
    }

    li.edit {
        color: #8f8e8e;
    }

    li.edit a {
        color: #8f8e8e;
    }
</style>


<body>

<c:import url="navbar.jsp">
    <c:param name="index" value="active"/>
</c:import>

<div class="container">

    <div class="row">
        <div class="span8">
            <h4>${customer.name}</h4>
            <ul class="unstyled">
                <li>
                    Тип организации:
                    <strong>
                        <c:choose>
                            <c:when test="${customer.customerType eq 'MOBILE_AGGREGATOR'}">
                                мобильный агрегатор
                            </c:when>
                            <c:when test="${customer.customerType eq 'PUBLIC_RIGHTS_SOCIETY'}">
                                организация по коллективному управлению
                            </c:when>
                            <c:otherwise>
                                не задан
                            </c:otherwise>
                        </c:choose>
                    </strong>
                </li>
                <li>
                    Тип прав:
                    <strong>
                        <c:choose>
                            <c:when test="${customer.rightType eq 'AUTHOR'}">
                                авторские
                            </c:when>
                            <c:when test="${customer.rightType eq 'RELATED'}">
                                смежные
                            </c:when>
                            <c:when test="${customer.rightType eq 'AUTHOR_RELATED'}">
                                авторские и смежные
                            </c:when>
                            <c:otherwise>
                                отсутствует
                            </c:otherwise>
                        </c:choose>
                    </strong>
                </li>

            </ul>
        </div>
    </div>


    <div class="row">
        <div class="span8">
            <section>
                <legend>
                    Доступные каталоги
                </legend>



                <c:forEach var="p" items="${platforms}">

                    <h4>${p.name}</h4>

                    <dl class="dl-horizontal">
                        <c:forEach var="c" items="${p.catalogs}">
                            <c:if test="${c.rightType eq customer.rightType or customer.rightType eq 'AUTHOR_RELATED'}">
                                <dt>${c.name}</dt>
                                <dd>
                                    <ul class="inline">
                                        <li>
                            <span class="small">
                                 <fmt:formatNumber type="number" maxFractionDigits="3" value="${c.tracks}"/>
                            </span>
                            <span class="small">
                                 <i class="icon-music"></i>
                            </span>

                                        </li>
                                        <li>
                                          <span class="small">
                                 <fmt:formatNumber type="number" maxFractionDigits="3" value="${c.artists}"/>
                            </span>
                            <span class="small">
                                 <i class="icon-user"></i>
                            </span>

                                        </li>
                                    </ul>

                                </dd>
                            </c:if>
                        </c:forEach>
                    </dl>
                </c:forEach>

            </section>


            <section>
                <legend>
                    Отчеты
                </legend>

                <ul class="unstyled">
                    <c:forEach items="${reports}" var="r">
                        <li>
                            <ul class="inline">
                                <li class="report ${r.accepted ? 'sent' : 'edit'}">
                                    <a href="report?id=${r.id}">
                                        <fmt:formatDate pattern="MMMMM yyyy" value="${r.startDate}"/>
                                    </a>
                                    <span>
                                        <c:choose>
                                            <c:when test="${r.accepted}">
                                                (отправлен)
                                            </c:when>
                                            <c:otherwise>
                                                (редактируется)
                                            </c:otherwise>
                                        </c:choose>
                                    </span>
                                </li>
                            </ul>
                        </li>
                    </c:forEach>
                </ul>

            </section>


        </div>

    </div>

</div>

</div>


</body>
</html>