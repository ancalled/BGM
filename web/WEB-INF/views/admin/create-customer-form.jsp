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
    <title>Новый пользователь</title>
</head>
<body>

<c:import url="navbar.jsp">
    <c:param name="customers" value="active"/>
</c:import>

<div class="container">
    <div class="row text-left">
        <legend>
            Новый пользователь
        </legend>
    </div>
    <div class="span4">

        <form action="/admin/action/create-customer" method="post"  class="form-horizontal">
            <div class="control-group">
                <label class="control-label" for="name">Наименование</label>

                <div class="controls">
                    <input type="text" id="name" name="name" placeholder="ТОО 'Рога и копыта'">
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="rights">Тип прав</label>

                <div class="controls">
                    <select id="rights" name="rights" class="input-block-level">
                        <option value="AUTHOR">Авторские</option>
                        <option value="RELATED">Публичка</option>
                        <option value="AUTHOR_RELATED">Все</option>
                    </select>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="share">Ставка</label>

                <div class="controls">
                    <input type="number" id="share" name="share" placeholder="20">
                    <span class="add-on">%</span>
                </div>
            </div>


            <div class="control-group">
                <div class="controls">

                    <button type="submit" class="btn">Создать</button>
                </div>
            </div>
        </form>
    </div>



</div>
</body>
</html>