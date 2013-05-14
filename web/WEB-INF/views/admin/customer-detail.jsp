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
<body style="padding-top: 30">


<div class="container">

    <div class="row text-left">
    <legend>
            Информация по компании ${customer.name}
        </legend>
    </div>

</div>

<br>
<br>

<div class="container">
    <table class="table">
        <thead>
        <tr>
            <th>#</th>
            <th>Имя</th>
            <th>Пароль</th>
            <th>Полное имя</th>
            <th>Почта</th>
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
                <td>${u.fullName}
                </td>
                <td>${u.email}
                </td>
                <td>
                    <a href="/admin/action/delete-user?user-id=${u.id}&cid=${customer.id}">
                        <i class="icon-trash"></i>
                    </a>
                </td>
            </tr>

        </c:forEach>

        </tbody>
    </table>
    <div class="row span2">
        <form action="create-user-form" method="get">
            <input type="hidden" name="cid" value="${customer.id}">
            <input class="btn" type="submit" value="Создать">

        </form>
    </div>
</div>


<script>

</script>

</body>
</html>