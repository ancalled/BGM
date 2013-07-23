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

    ul.not-active {
        color: #cccccc;
    }
</style>


<body>

<c:import url="navbar.jsp">
    <c:param name="reports" value="active"/>
</c:import>

<div class="container">

    <div class="row">
        <div class="span8">

            <section>
                <legend>
                    Отправленные отчеты
                </legend>

                <ul class="unstyled">
                    <c:forEach items="${reports}" var="r">
                        <li>
                            <ul class="inline ${r.accepted ? '' : 'not-active'}">
                                <li><fmt:formatDate pattern="yyyy MMMMM"
                                                    value="${r.startDate}"/></li>
                                <li>
                                    <c:choose>
                                        <c:when test="${r.type == 'MOBILE'}">
                                            Мобильный
                                        </c:when>
                                        <c:otherwise>
                                            Публичка
                                        </c:otherwise>
                                    </c:choose>
                                </li>
                                <li>
                                    <c:choose>
                                        <c:when test="${r.period == 'MONTH'}">
                                            Ежемесячный
                                        </c:when>
                                        <c:otherwise>
                                            Квартальный
                                        </c:otherwise>
                                    </c:choose>
                                </li>
                                <li>
                                    <a href="report?id=${r.id}">
                                            ${r.tracks} / ${r.detected}
                                    </a>
                                </li>
                            </ul>
                        </li>
                    </c:forEach>
                </ul>

            </section>


        </div>

    </div>

</div>



</body>
</html>