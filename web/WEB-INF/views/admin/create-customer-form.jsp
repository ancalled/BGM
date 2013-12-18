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
    <title>Новая компания</title>
</head>
<body>

<c:import url="navbar.jsp">
    <c:param name="customers" value="active"/>
</c:import>

<div class="container" style="height: 80%">
    <div class="row text-left">
        <legend>
            Новая компания
        </legend>
    </div>
    <div class="span4">

        <form action="/admin/action/create-customer" method="post"  class="form-horizontal">

            <div class="control-group">
                <label class="control-label" for="name">Наименование</label>

                <div class="controls">
                    <input type="text" id="name" name="name">
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="rights">Вид деятельности</label>

                <div class="controls">
                    <select id="type" name="type" class="input-block-level">
                        <option value="MOBILE_AGGREGATOR">Мобильный агрегатор</option>
                        <option value="PUBLIC_RIGHTS_SOCIETY">Организация по коллективному управлению</option>
                        <option value="OTHER">Другое</option>
                    </select>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="rights">Тип прав</label>

                <div class="controls">
                    <select id="rights" name="rights" class="input-block-level">
                        <option value="AUTHOR">Авторские</option>
                        <option value="RELATED">Публичка</option>
                        <option value="AUTHOR_RELATED">Авторские и смежные</option>
                    </select>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="share-author">Доля по авторским правам</label>

                <div class="controls">
                    <input type="number" id="share-author" name="share" placeholder="0">
                    <%--<span class="add-on">%</span>--%>
                </div>
            </div>

            <div class="control-group">
                <label class="control-label" for="share-related">Доля по смежным правам</label>

                <div class="controls">
                    <input type="number" id="share-related" name="share" placeholder="0">
                    <%--<span class="add-on">%</span>--%>
                </div>
            </div>


            <div class="control-group">
                <div class="controls">

                    <button type="submit" class="btn">Создать</button>
                </div>
            </div>
        </form>
    </div>
    <style type="text/css">
input#share{
  height: 27
    }

</style>
</div>

<c:import url="footer.jsp"/>

</body>
</html>