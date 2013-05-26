<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
    <script src="/js/jquery.js"></script>
    <script src="/js/bootstrap.js"></script>
    <script src="/js/bootstrap-fileupload.js"></script>
    <script src="/js/plugin-parser.js"></script>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-fileupload.css" media="screen"/>
    <title>Создание Организации</title>
</head>
<body>

<c:import url="navbar.jsp">
    <c:param name="customers" value="active"/>
</c:import>

<div class="container">
    <div class="row text-left">
        <legend>
            Создание Организации
        </legend>
    </div>
    <div class="span4">

        <form action="/admin/action/create-customer" method="post">
            <label>
                Наименование
                <input type="text" name="name" class="input-block-level" required="true">
            </label>
            <label>
               Тип прав
                <input type="text" name="right" class="input-block-level" required="true">
            </label>
            <label>
               Ставка
                <input type="text" name="share" class="input-block-level" required="true">
            </label>
            <label>
                Параметры договра
                <input type="text" name="contract" class="input-block-level" required="true">
            </label>

            <button class="btn" type="submit">Создать</button>
        </form>
    </div>



</div>
</body>
</html>