<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <script src="../../web/js/jquery.js"></script>
    <script src="../../web/js/bootstrap.js"></script>
    <script src="../../web/js/bootstrap-fileupload.js"></script>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="../../web/css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="../../web/css/bootstrap-fileupload.css" media="screen"/>
    <title>Страница авторизации</title>
</head>
<body style="padding-left: 140px">

<div class="row">
    <div class="container span4">

        <form method="post" action="/authorize" class="form-signin">
            <h2 class="form-signin-heading">Авторизация</h2>
            <input type="text" name="u" class="input-block-level" placeholder="Пользователь">
            <input type="password" name="p" class="input-block-level" placeholder="Пароль">
            <label class="checkbox">
                <input type="checkbox" value="remember-me"> Запомнить меня
            </label>
            <button class="btn btn-large btn-primary" type="submit">Ок</button>
        </form>
    </div>
</div>

<div class="container span4">
    <c:if test="${mess=='001'}">
        <div class="alert alert-block">
            <button type="button" class="close" data-dismiss="alert">&times;</button>
            <h4>Внимание</h4>
            Вы не были авторизованы, авторизуйтесь
        </div>

        <h2></h2>
    </c:if>
</div>
</body>
</html>