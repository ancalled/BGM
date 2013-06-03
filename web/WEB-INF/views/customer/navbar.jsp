<%@ page import="kz.bgm.platform.model.domain.User" %>
<%@ page import="kz.bgm.platform.model.service.CatalogFactory" %>
<%@ page import="kz.bgm.platform.model.service.CatalogStorage" %>
<%@ page import="java.util.List" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
    CatalogStorage storage = CatalogFactory.getStorage();

    User user = (User) session.getAttribute("user");

    List<Long> tracksIdList = storage.getUserTracksId(user.getId());
%>


<div class="navbar">
    <div class="navbar-inner">
        <a class="brand" href="/customer/view/index">BGM Platform</a>
        <ul class="nav">
            <li class="${param.index}"><a href="/customer/view/index">Главная</a></li>
            <li class="${param.reports}"><a href="../../in_developing.html">Отчеты</a></li>
            <li class="${param.search}"><a href="/customer/view/search-result">Поиск</a></li>
            <li><a href="/customer/action/logout">Выход</a></li>
            <li class="${param.user-catalog}"><a
                    href="/customer/view/user-catalog">(<%=tracksIdList.size()%>)
                <i class="icon-shopping-cart"></i></a></li>
        </ul>
    </div>
</div>
