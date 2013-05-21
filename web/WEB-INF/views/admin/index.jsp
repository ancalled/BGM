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
        <legend>
            Каталоги
        </legend>

        <c:if test="${not empty cat_stat}">

            <table class="table">
                <thead>
                <tr>
                    <th>Каталог</th>
                    <th>Ставка</th>
                    <th>Права</th>
                    <th>Количество исполнителей</th>
                    <th>Количество треков</th>
                </tr>
                </thead>
                <tbody>
                <c:forEach items="${cat_stat}" var="c">
                    <tr>
                        <td>${c.catalog}</td>
                        <td>${c.royalty}</td>
                        <td><c:choose>
                            <c:when test="${c.rights eq 'AUTHOR'}">
                                Авторские
                            </c:when>
                            <c:when test="${c.rights eq 'RELATED'}">
                                Смежные
                            </c:when>
                            <c:otherwise>
                                ${c.rights}
                            </c:otherwise>
                        </c:choose></td>
                        <td>
                            <fmt:formatNumber type="number"
                                              maxFractionDigits="3" value="${c.artistCount}"/>
                        </td>
                        <td>
                            <fmt:formatNumber type="number"
                                              maxFractionDigits="3" value="${c.trackCount}"/>
                        </td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

        </c:if>
    </div>
    <div class="row">
        <legend>
            Загруженные клиентские отчеты
        </legend>

        <c:if test="${not empty reports}">

            <table class="table">
                <thead>
                <tr>
                    <th>Клиент</th>
                    <th>Дата подачи</th>
                    <th>Отчет за</th>
                    <th>Тип</th>
                    <th>Период</th>
                    <th>Расчитан</th>
                </tr>
                </thead>
                <tbody>

                <c:forEach items="${reports}" var="r">
                    <tr>
                        <td>${r.customer}</td>
                        <td>${r.sendDate}</td>
                        <td>${r.reportDate}</td>
                        <td>
                            <c:choose>
                                <c:when test="${r.reportType ==1}">
                                    Публичка
                                </c:when>
                                <c:otherwise>
                                    Мобильный
                                </c:otherwise>
                            </c:choose>
                        </td>
                        <td><c:choose>
                            <c:when test="${r.reportPeriod ==1}">
                                Квартальный
                            </c:when>
                            <c:otherwise>
                                Месячный
                            </c:otherwise>
                        </c:choose>
                        </td>
                        <td>
                            <c:choose>
                                <c:when test="${r.calculated ==true}">
                                    <i class="icon-ok"></i>
                                </c:when>
                                <c:otherwise>
                                    <i class="icon-remove"></i>
                                </c:otherwise>
                            </c:choose>
                        </td>
                    </tr>
                </c:forEach>

                </tbody>
            </table>

        </c:if>

    </div>

</div>

</div>


</body>
</html>