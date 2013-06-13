<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <script src="/js/jquery.js"></script>
    <script src="/js/bootstrap.js"></script>
    <script src="/js/bootstrap-fileupload.js"></script>
    <script src="/js/bootstrap-datetimepicker.min.js"></script>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-fileupload.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-datetimepicker.min.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-responsive.css" media="screen"/>
    <title>Каталог пользователя</title>
</head>

<c:import url="navbar.jsp">
    <c:param name="basket" value="active"/>
</c:import>

<body>

<div class="container span8">

        <legend>
            Список композиций
        </legend>
    <c:if test="${not empty tracks}">

        <table class="table">
            <thead>
            <tr>
                <th>Код</th>
                <th>Композиция</th>
                <th>Исполнитель</th>
                <th>Авторы</th>
                <th>
                    <c:choose>
                        <c:when test="${customer.rightType eq 'AUTHOR'}">
                            Мобильный контент
                        </c:when>
                        <c:otherwise>
                            Публичка
                        </c:otherwise>
                    </c:choose>
                </th>
                <th>Каталог</th>
            </tr>
            </thead>
            <tbody>

            <c:forEach var="t" items="${tracks}">
                <tr>
                    <td>${t.code}</td>
                    <td>${t.name}</td>
                    <td>${t.artist}</td>
                    <td>${t.composer}</td>
                    <td>
                        <c:choose>
                            <c:when test="${customer.rightType eq 'AUTHOR'}">
                                ${t.mobileShare}
                            </c:when>
                            <c:otherwise>
                                ${t.publicShare}
                            </c:otherwise>
                        </c:choose>
                    </td>
                    <td>${t.catalog}</td>
                    <td><i id="${t.id}" class="icon-remove-circle"></i></td>
                </tr>
            </c:forEach>

            </tbody>
        </table>
    </c:if>
</div>


<form method="post" action="/customer/action/remove-user-track" id="track-remove">

    <input type="hidden" id="track_rem" name="track_id">

</form>

<div id="myModal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">Удаление трека</h3>
    </div>
    <div class="modal-body">
        <p>Вы действительно хотите удалить трек ?</p>
    </div>
    <div class="modal-footer">
        <button class="btn" data-dismiss="modal">Нет</button>
        <button class="btn btn-primary" id="remove-btn" aria-hidden="true">Да</button>
    </div>
</div>

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
               $('#track_rem').val(this.id);
               $('#remove-btn').click(function () {

                  submitRemover();
               });
               $('#myModal').modal('show');

        });
    }

    function submitRemover() {
        document.getElementById("track-remove").submit();
    }

</script>

</body>
</html>