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

<c:import url="navbar.jsp">
    <c:param name="customers" value="active"/>
</c:import>

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
                <td><a href="customer-detail?customer_id=${c.trackId}">${c.track} </a>
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
                    <a href="/admin/action/delete-customer?customer-trackId=${c.trackId}">
                        <i class="icon-trash"></i>
                    </a>
                </td>
            </tr>
            </a>
        </c:forEach>

        </tbody>
    </table>

    <form action="create-customer-form" method="get">
        <input type="hidden" name="cid" value="${customer.trackId}">
        <input class="btn" type="submit" value="Создать">

    </form>

</div>


</body>
</html>