<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%--<%--%>
<%--TrackBasket basket = (TrackBasket) request.getSession().getAttribute("basket");--%>
<%--request.setAttribute("basket", basket);--%>
<%--%>--%>


<div class="navbar">
    <div class="navbar-inner">
        <a class="brand" href="/customer/view/index">BGM Platform</a>
        <ul class="nav">
            <li class="${param.index}"><a href="/customer/view/index">Главная</a></li>
            <li class="${param.reports}"><a href="../../in_developing.html">Отчеты</a></li>
            <li class="${param.search}"><a href="/customer/view/search-result">Поиск</a></li>
            <li><a href="/customer/action/logout">Выход</a></li>
            <li class="${param.basket}"><a href="/customer/view/basket">(${fn:length(sessionScope.basket.tracks)})
                <i class="icon-shopping-cart"></i></a></li>
        </ul>
    </div>
</div>
