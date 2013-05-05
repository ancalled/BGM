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
    <title>Компания ${customer.name}</title>
</head>
<body>


<div class="container">
    <ul>
        <li>
            ${details.address}
        </li>
        <li>
            ${details.rnn}
        </li>
        <li>
            ${details.boss}
        </li>
    </ul>
</div>


<div class="container">
    <table class="table">
        <thead>
        <tr>
            <th>#</th>
            <th>Имя</th>
            <th>Пароль</th>
        </tr>
        </thead>
        <tbody>


        <c:forEach var="u" items="${users}" varStatus="status">
            <tr>
                <td>${status.index}
                </td>
                <td>${u.login}
                </td>
                <td>${u.pass}
                </td>
            </tr>
        </c:forEach>

        </tbody>
    </table>

    <div class="row">
        <form action="/admin/create-user-form.html" method="get">
            <input type="hidden" name="cid"  value="${customer.id}">
            <input class="btn"  type="submit" value="Создать">

        </form>
    </div>
</div>


</body>
</html>