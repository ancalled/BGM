<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<div class="navbar">
    <div class="navbar-inner">
        <a class="brand" href="/admin/view/index">BGM Platform</a>
        <ul class="nav">
            <li class="${param.index}"><a href="/admin/view/index">Главная</a></li>
            <%--<li class="${param.catalogs}"><a href="/admin/view/catalogs">Каталоги</a></li>--%>
            <li class="${param.reports}"><a href="/admin/view/reports">Отчеты</a></li>
            <li class="${param.search}"><a href="/admin/view/search">Поиск</a></li>
            <li class="${param.customers}"><a href="/admin/view/customers">Пользователи</a></li>
            <li><a href="/admin/action/logout">Выход</a></li>
        </ul>
    </div>
</div>
