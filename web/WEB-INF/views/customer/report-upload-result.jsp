<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>

<head>
    <script src="/js/jquery.js"></script>
    <script src="/js/bootstrap.js"></script>
    <script src="/js/bootstrap-fileupload.js"></script>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-fileupload.css" media="screen"/>
    <title>Обработка отчета</title>
</head>


<body>

<c:import url="navbar.jsp">
    <c:param name="reports" value="active"/>
</c:import>

<div class="container">
    <section>
        <h4>Отчет загружен</h4>

        <dl class="dl-horizontal">
            <dt>Номер</dt>
            <dd>${report.id}</dd>
            <dt>Дата отчета</dt>
            <dd>
                <fmt:formatDate pattern="yyyy-MM-dd" value="${report.startDate}" />
            </dd>
            <dt>Период отчета</dt>
            <dd>${report.period}</dd>
            <dt>Тип</dt>
            <dd>${report.type}</dd>
            <%--<dt>Компания</dt>--%>
            <%--<dd>${customer.name}</dd>--%>
            <dt>Треков</dt>
            <dd>${report.tracks}</dd>
            <dt>Определилось</dt>
            <dd>${report.detected}</dd>
        </dl>

    </section>

</div>


</body>
</html>