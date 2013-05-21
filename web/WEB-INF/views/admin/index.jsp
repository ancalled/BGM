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


<body>

<c:import url="navbar.jsp">
    <c:param name="index" value="active"/>
</c:import>

<div class="container">

    <div class="row">
        <div class="span8">
            <section>
                <legend>
                    Каталоги
                </legend>

                <c:forEach var="p" items="${platforms}">

                    <h4>${p.name}</h4>

                    <dl class="dl-horizontal">
                        <c:forEach var="c" items="${p.catalogs}">
                            <dt>
                                <a href="catalog?catId=${c.id}">
                                        ${c.name}
                                </a>
                            </dt>
                            <dd>
                                <ul class="inline">
                                    <li>
                                        <c:choose>
                                            <c:when test="${c.copyright eq 'AUTHOR'}">
                                                Авторские
                                            </c:when>
                                            <c:otherwise>
                                                Смежные
                                            </c:otherwise>
                                        </c:choose>
                                    </li>
                                    <li>
                            <span>
                                 <i class="icon-music"></i>
                            </span>
                            <span>
                                 <fmt:formatNumber type="number" maxFractionDigits="3" value="${c.tracks}"/>
                            </span>
                                    </li>
                                    <li>
                            <span>
                                 <i class="icon-user"></i>
                            </span>
                            <span>
                                 <fmt:formatNumber type="number" maxFractionDigits="3" value="${c.artists}"/>
                            </span>
                                    </li>
                                </ul>

                            </dd>
                        </c:forEach>
                    </dl>
                </c:forEach>

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

    </div>

</div>

</div>


</body>
</html>