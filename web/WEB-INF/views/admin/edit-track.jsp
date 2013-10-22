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
    <title>Корректировка композиции</title>


</head>
<body>

<c:import url="navbar.jsp">
    <c:param name="search" value="active"/>
</c:import>


<div class="container">


    <div class="row text-left">
        <legend>
            Корректировка трека
        </legend>
    </div>
    <div class="span4">
        <%--  private long id = 0L;
          private int catalogId = 0;
          private String catalog = "";
          private String code = "";
          private String artist = "";
          private String name = "";
          private String composer = "";
          private float publicShare = 0F;
          private float mobileShare = 0F;
          private Catalog foundCatalog;--%>

        <form action="admin/action/edit-track" method="post" enctype="">
            <label>
                Код
                <input type="text" name="code" class="input-block-level" required="true">
            </label>
            <label>
                Имя
                <input type="text" name="name" class="input-block-level" required="true">
            </label>
            <label>
                Артист
                <input type="text" name="artist" class="input-block-level" required="true">
            </label>
            <label>
                Автор
                <input type="text" name="composer" class="input-block-level" required="true">
            </label>
            <label>
                Мобильный контент
                <input type="text" name="mobile-share" class="input-block-level" required="true">
            </label>
            <label>
                Публичка
                <input type="text" name="public-share" class="input-block-level" required="true">
            </label>
            <label>
                Каталог
                <select name="catalog" id="Select1" size="4" multiple="multiple">

                    <option value="1">item 1</option>

                    <option value="2">item 2</option>

                    <option value="3">item 3</option>


                </select>
            </label>

            <input type="hidden" name="track-id">
             <br>
            <button class="btn" type="submit">Изменить</button>
        </form>
    </div>


</div>
</body>
</html>