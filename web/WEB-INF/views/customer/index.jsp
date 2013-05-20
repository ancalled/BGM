<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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
        <div class="span5">
            <legend>
                Загруженные клиентские отчеты
            </legend>

            <c:if test="${not empty reports}">

                <table class="table">
                    <thead>
                    <tr>
                        <th>Дата подачи</th>
                        <th>Отчет за</th>
                        <th>Тип</th>
                        <th>Период</th>
                    </tr>
                    </thead>
                    <tbody>


                    <c:forEach items="${reports}" var="r">
                        <tr>
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