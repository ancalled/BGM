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
    <title>Клиенты</title>
</head>
<body>

<div class="navbar">
    <div class="navbar-inner">
        <a class="brand" href="/admin/index.html">BGM Platform</a>
        <ul class="nav">
            <li><a href="/admin/index.html">Главная</a></li>
            <li><a href="/admin/reports.html">Отчеты</a></li>
            <li><a href="/admin/search.html">Поиск</a></li>
            <li class="active"><a href="/admin/view/customers">Клиенты</a></li>
            <li><a href="/admin/action/logout">Выход</a></li>
        </ul>
    </div>
</div>


<div class="container">
    <div class="row text-left">
        <legend>
            Компании
        </legend>
    </div>

    <table class="table table-hover">
        <thead>
        <tr>
            <th>Название</th>
            <th>Ставка</th>
            <th>Договор</th>
            <th>Тип прав</th>
        </tr>
        </thead>
        <tbody>


        <c:forEach var="c" items="${customers}">

            <tr>
                <td><a href="customer-detail?customer_id=${c.id}">${c.name} </a>
                </td>
                <td>${c.royalty}
                </td>
                <td>${c.contract}
                </td>
                <td>
                    <c:choose>
                        <c:when test="${c.rightType eq 'copyright'}">
                            авторские
                        </c:when>
                        <c:otherwise>
                            смежные
                        </c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <a href="/admin/action/delete-customer?customer-id=${c.id}">
                        <i class="icon-trash"></i>
                    </a>
                </td>
            </tr>
            </a>
        </c:forEach>

        </tbody>
    </table>

    <form action="create-customer-form" method="get">
        <input type="hidden" name="cid" value="${customer.id}">
        <input class="btn" type="submit" value="Создать">

    </form>

</div>


</body>
</html>