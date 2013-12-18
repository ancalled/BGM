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
    <style>
        table td.decimal {
            text-align: right;
            padding-right: 30px;
        }
    </style>
</head>
<body>

<c:import url="navbar.jsp">
    <c:param name="customers" value="active"/>
</c:import>

<div class="container">
    <div class="row text-left">
        <legend>
            Компании &mdash; пользователи авторских и смежных прав
        </legend>
    </div>

    <table class="table">
        <thead>
        <tr>
            <th>Организация</th>
            <th>Вид деятельности</th>
            <th>Тип прав</th>

            <th>Доля по авторским правам</th>
            <th>Доля по смежным правам</th>
            <%--<th>Ставка</th>--%>
            <th>Убрать</th>
        </tr>
        </thead>
        <tbody>


        <c:forEach var="c" items="${customers}">

            <tr>
                <td><a href="customer-detail?customer_id=${c.id}">${c.name}</a></td>
                <td>
                    <c:choose>
                        <c:when test="${c.customerType eq 'MOBILE_AGGREGATOR'}">
                            Мобильный агрегатор
                        </c:when>
                        <c:when test="${c.customerType eq 'PUBLIC_RIGHTS_SOCIETY'}">
                            Организация по коллективному управлению
                        </c:when>
                        <c:otherwise>Другое</c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <c:choose>
                        <c:when test="${c.rightType eq 'AUTHOR'}">
                            Авторские
                        </c:when>
                        <c:when test="${c.rightType eq 'RELATED'}">
                            Смежные
                        </c:when>
                        <c:when test="${c.rightType eq 'AUTHOR_RELATED'}">
                            Авторские и смежные
                        </c:when>
                        <c:otherwise>&mdash;</c:otherwise>
                    </c:choose>
                </td>
                <td class="decimal">
                    <c:choose>
                        <c:when test="${c.rightType eq 'RELATED'}">&mdash;</c:when>
                        <c:otherwise>${c.authorRoyalty}%</c:otherwise>
                    </c:choose>
                </td>
                <td class="decimal">
                    <c:choose>
                        <c:when test="${c.rightType eq 'AUTHOR'}">&mdash;</c:when>
                        <c:otherwise> ${c.relatedRoyalty}%</c:otherwise>
                    </c:choose>
                </td>
                <td>
                    <i class="icon-trash" id="${c.id}"></i>
                        <%--<a href="/admin/action/delete-customer?customer-id=${c.id}">--%>
                        <%--<i class="icon-trash"></i>--%>
                        <%--</a>--%>
                </td>
            </tr>
            </a>

        </c:forEach>

        </tbody>
    </table>

    <hr/>

    <form action="create-customer-form" method="get">
        <input type="hidden" name="cid" value="${customer.id}">
        <input class="btn" type="submit" value="Добавить компанию">

    </form>

</div>


<%--Modal--%>
<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">Удаление клиента</h3>
    </div>
    <div class="modal-body">
        <p>Вы действительно хотите удалить компанию ?</p>
    </div>
    <div class="modal-footer">
        <button class="btn" data-dismiss="modal">Нет</button>
        <button class="btn btn-primary" id="remove-btn" aria-hidden="true">Да</button>
    </div>
</div>
<%--====================--%>

<form action="/admin/action/delete-customer" method="post" id="customer-remove">

    <input type="hidden" id="customer" name="customer-id">

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
            $('#customer').val(this.id);
            $('#remove-btn').click(function () {
               submitRemover();
            });
            $('#myModal').modal('show');

        });
    }


    function submitRemover() {
        document.getElementById("customer-remove").submit();
    }

</script>

<c:import url="footer.jsp"/>

</body>
</html>