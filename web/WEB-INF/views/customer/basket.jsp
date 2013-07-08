<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <script src="/js/jquery.js"></script>
    <script src="/js/bootstrap.min.js"></script>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-fileupload.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-datetimepicker.min.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-responsive.css" media="screen"/>
    <title>Каталог пользователя</title>
    <style>
        #basket {
            font-size: 10pt;
        }

        #right-align {
            margin-left: 95%;
        }
    </style>
</head>

<c:import url="navbar.jsp">
    <c:param name="basket" value="active"/>
</c:import>

<body>

<div class="container">

    <div class="span10">
        <legend>
            Доступные композиции
        </legend>

        <c:if test="${not empty tracks}">
            <div class="row">
                <form id="right-align">
                    <button class="btn btn-block" type="submit" style="width: 30px">
                        <i class="icon-print"></i></button>
                </form>
            </div>
            <table class="table" id="basket">
                <thead>
                <tr>
                    <th>Код</th>
                    <th>Композиция</th>
                    <th>Исполнитель</th>
                    <th>Авторы</th>
                    <c:choose>
                        <c:when test="${customer.customerType eq 'MOBILE_AGGREGATOR'}">
                            <th>Мобильный контент</th>
                        </c:when>
                        <c:when test="${customer.customerType eq 'PUBLIC_RIGHTS_SOCIETY'}">
                            <th>Публичка</th>
                        </c:when>
                    </c:choose>
                    <th>Каталог</th>
                    <th>Удалить</th>
                </tr>
                </thead>
                <tbody>

                <c:forEach var="t" items="${tracks}">
                    <tr>
                        <td>${t.code}</td>
                        <td>${t.name}</td>
                        <td>${t.artist}</td>
                        <td>${t.composer}</td>
                        <c:choose>
                            <c:when test="${customer.rightType eq 'AUTHOR'}">
                                <td>${t.mobileShare}</td>
                            </c:when>
                            <c:otherwise>
                                <td>${t.publicShare}</td>
                            </c:otherwise>
                        </c:choose>
                        <td>${t.catalog}</td>
                        <td>
                            <a href="#" class="remove-track" id="${t.id}" title="Удалить">
                                <i class="icon-remove"></i>
                            </a>
                        </td>
                    </tr>
                </c:forEach>

                </tbody>
            </table>
        </c:if>
    </div>
</div>


<form method="post" action="/customer/action/remove-user-track" id="track-remove-form">
    <input type="hidden" id="track-to-remove" name="track_id">
</form>

<div id="track-remove-modal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-header">
        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
        <h3 id="myModalLabel">Удаление трека</h3>
    </div>
    <div class="modal-body">
        <p>Вы действительно хотите удалить трек?</p>
    </div>
    <div class="modal-footer">
        <button class="btn" data-dismiss="modal">Нет</button>
        <button class="btn btn-primary" id="modal-remove-btn" aria-hidden="true">Да</button>
    </div>
</div>

<script>
    $(document).ready(function () {
        $('#modal-remove-btn').click(function () {
            $('#track-remove-form').submit();
        });

        $('a.remove-track').click(function () {
            var a = $(this);
            $('#track-to-remove').val(a.attr('id'));

            $('#track-remove-modal').modal('show');
        });
    });
</script>

</body>
</html>