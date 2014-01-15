<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>

    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-fileupload.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/csv-preview.css" media="screen"/>


    <title>Создание каталога</title>
</head>
<body>

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

        <input type="hidden" name="platformId" value="${platform.id}">

            <label for="type">Тип прав</label>
            <select id="type" name="rights" class="input-block-level">
                <option value="AUTHOR">Авторские</option>
                <option value="RELATED">Смежные</option>
                <option value="AUTHOR_RELATED">Авторские и смежные</option>
            </select>

            <label for="name">Роялити </label>
            <input type="number" id="royal" name="royal" placeholder="0">

            <input type="submit" value="Создать каталог">

        <c:import url="footer.jsp"/>

        </form>
    </div>
</div>
</body>
</html>