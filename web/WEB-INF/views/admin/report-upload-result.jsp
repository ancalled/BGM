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

<div class="navbar">
    <div class="navbar-inner">
        <a class="brand" href="/admin/index.html">BGM Platform</a>
        <ul class="nav">
            <li><a href="/admin/index.html">Главная</a></li>
            <li class="active"><a href="/admin/reports.html">Отчеты</a></li>
            <li><a href="/admin/view/search-result">Поиск</a></li>
            <li><a href="/admin/view/customers">Клиенты</a></li>
            <li><a href="/admin/action/logout">Выход</a></li>
        </ul>
    </div>
</div>

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