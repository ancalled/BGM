<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>

    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-fileupload.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/csv-preview.css" media="screen"/>
    <script src="/js/jquery.js"></script>

    <title>Создание каталога</title>
</head>

<c:import url="navbar.jsp">
    <c:param name="index" value="active"/>
</c:import>

<div class="container">
    <div class="span4">

        <legend>
            Создание каталога
        </legend>

        <form action="/admin/action/create-catalog" method="post">

            <label for="name">Название каталога </label>
            <input type="text" id="name" name="name">

            <label for="type">Платформа</label>

            <select id="platform" name="platformId" class="input-block-level">
                <c:forEach items="${platforms}" var="p" varStatus="loop">
                    <option value="${p.id}" ${platformId==p.id ? "selected='selected'":""}>${p.name}</option>
                </c:forEach>
            </select>

            <%--<input type="hidden" name="platformId" value="" id="platform_id">--%>


            <label for="type">Тип прав</label>
            <select id="type" name="rights" class="input-block-level">
                <option value="AUTHOR">Авторские</option>
                <option value="RELATED">Смежные</option>
            </select>

            <label for="name">Роялити </label>
            <input type="number" id="royal" name="royal" placeholder="0">

            <input type="submit" value="Создать каталог">

            <c:import url="footer.jsp"/>

        </form>
    </div>
</div>

<%--<script>--%>

    <%--$('#platform').change(function () {--%>
        <%--var platformId = this.value;--%>
        <%--$('#platform_id').attr('value',platformId);--%>
        <%--alert(platformId);--%>
    <%--})--%>
<%--</script>--%>
</body>
</html>