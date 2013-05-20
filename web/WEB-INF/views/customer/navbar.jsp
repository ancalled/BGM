<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="navbar">
    <div class="navbar-inner">
        <a class="brand" href="/customer/view/index">BGM Platform</a>
        <ul class="nav">
            <li class="${param.index}"><a href="/customer/view/index">Главная</a></li>
            <li  class="${param.reports}"><a href="/customer/view/report-upload-result">Отчеты</a></li>
            <li  class="${param.search}"><a href="/customer/view/search-result">Поиск</a></li>
            <li><a href="/customer/action/logout">Выход</a></li>
        </ul>
    </div>
</div>
