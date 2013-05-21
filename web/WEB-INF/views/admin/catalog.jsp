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
    <title>${catalog.name}</title>
</head>
<body>

<c:import url="navbar.jsp">
    <c:param name="index" value="active"/>
</c:import>

<div class="container">
    <div class="row">
        <div class="span6">


            <section>
                <legend>
                    Каталог ${catalog.name}
                </legend>


                <dl class="dl-horizontal">
                    <dt>Тип прав</dt>
                    <dd>
                        <c:choose>
                            <c:when test="${catalog.copyright eq 'AUTHOR'}">
                                Авторские
                            </c:when>
                            <c:otherwise>
                                Смежные
                            </c:otherwise>
                        </c:choose>
                    </dd>

                    <dt>Роялти</dt>
                    <dd>${catalog.royalty}%</dd>

                    <dt>Композиций</dt>
                    <dd>
                        <fmt:formatNumber type="number" maxFractionDigits="3" value="${catalog.tracks}"/>
                    </dd>

                    <dt>Артистов</dt>
                    <dd>
                        <fmt:formatNumber type="number" maxFractionDigits="3" value="${catalog.artists}"/>
                    </dd>
                </dl>

            </section>

            <section>

                <legend>
                    Обновления
                </legend>

                <form action="../action/update-catalog" method="post" enctype="multipart/form-data">
                    <div class="fileupload fileupload-new" data-provides="fileupload">
                        <div class="input-append">
                            <div class="uneditable-input span3">
                                <i class="icon-file fileupload-exists"></i>
                                <span class="fileupload-preview"></span>
                            </div>

                <span class="btn btn-file">
                    <span class="fileupload-new">Выбрать обновление</span>
                    <span class="fileupload-exists">Изменить</span>
                    <input name="file" type="file"/>
                </span>

                            <a href="#" class="btn fileupload-exists" data-dismiss="fileupload">Убрать</a>
                        </div>
                    </div>

                    <div class="row-fluid">
                        <input class="btn" type="submit" value="Загрузить">
                    </div>

                    <input type="hidden" name="catId" value="${catalog.id}">
                </form>

            </section>

        </div>
    </div>

</div>


</body>
</html>