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

<c:import url="navbar.jsp">
    <c:param name="customers" value="active"/>
</c:import>

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
                    <i class="icon-trash" id="${u.id}"></i>

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


<%--Modal--%>
<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">Удаление пользователя</h3>
    </div>
    <div class="modal-body">
        <p>Вы действительно хотите удалить пользователя ?</p>
    </div>
    <div class="modal-footer">
        <button class="btn" data-dismiss="modal">Нет</button>
        <button class="btn btn-primary" id="remove-btn" aria-hidden="true">Да</button>
    </div>
</div>
<%--====================--%>

<form action="/admin/action/delete-user" id="user-remove" method="post">
    <input type="hidden" name="user-id" id="user">
    <input type="hidden" name="cid" id="customer">
</form>

<script>
    var delButtons = document.getElementsByTagName('i');
    for (var i = 0; i < delButtons.length; ++i) {
        var but = delButtons[i];
        $(but).mouseenter(function () {
            $(this).css('opacity', '0.3');
        });
        $(but).mouseout(function () {
            $(this).css('opacity', '1');
        });
        $(but).click(function () {
            $('#user').val(this.id);
            $('#customer').val(${customer.id});
            $('#remove-btn').click(function () {
                submitRemover();
            });
            $('#myModal').modal('show');

        });
    }

    function submitRemover() {
        document.getElementById("user-remove").submit();
    }
</script>

</body>
</html>