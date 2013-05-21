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
    <title>Обновление каталога</title>
    <style>
        table.smallcaps {
            font-size: 10pt;
        }
        li.deleted {
            text-decoration: line-through;
            color: #8f6e5f;
        }

        ul.bottom-aligned li {
            vertical-align: top;
        }

        .apply-btn {
            margin: 20px 0 5px 0;
        }


    </style>
</head>
<body>

<c:import url="navbar.jsp">
    <c:param name="index" value="active"/>
</c:import>

<div class="container">
    <div class="row">
        <div class="span12">

            <section>
                <legend>Обновления по каталогу ${catalog.name}</legend>

                <p>

                <form action="../action/appy-catalog-update">
                    <dl class="unstyled dl-horizontal">
                        <dt>Файл</dt>
                        <dd>${result.file}</dd>
                        <dt>Треков</dt>
                        <dd>${result.tracks}</dd>
                        <dt></dt>
                        <dd>
                            <input class="btn btn-small btn-primary apply-btn" type="submit" value="Применить!">
                        </dd>
                    </dl>
                </form>

                </p>

                <hr/>

                <p>

                <c:choose>
                    <c:when test="${result.status == 'HAS_ERRORS'}">
                        <h4>Ошибка</h4>

                        <ul>
                            <c:forEach items="${result.errors}" var="e">
                                <li>${e}</li>
                            </c:forEach>
                        </ul>

                    </c:when>
                    <c:otherwise>
                        <h4>Изменения ${from + 1} &ndash; ${from + pageSize}</h4>

                        <table class="table smallcaps">
                            <thead>
                            <tr>
                                <th>#</th>
                                <th>Код</th>
                                <th>Композиция</th>
                                <th>Исполнитель</th>
                                <th>Авторы</th>
                                <th>Мобильный контент</th>
                                <th>Публичка</th>
                                <th>Каталог</th>
                            </tr>
                            </thead>
                            <tbody>

                            <c:forEach items="${diffs}" var="d">
                                <tr>
                                    <td>${d.number}</td>
                                    <td>${d.code}</td>
                                    <td>
                                        <c:if test="${d.oldTrack.name != d.newTrack.name}">
                                            <ul class="unstyled">
                                                <li class="deleted">${d.oldTrack.name}</li>
                                                <li>${d.newTrack.name}</li>
                                            </ul>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${d.oldTrack.artist != d.newTrack.artist}">
                                            <ul class="unstyled">
                                                <li class="deleted">${d.oldTrack.artist}</li>
                                                <li>${d.newTrack.artist}</li>
                                            </ul>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${d.oldTrack.composer != d.newTrack.composer}">
                                            <ul class="unstyled">
                                                <li class="deleted">${d.oldTrack.composer}</li>
                                                <li>${d.newTrack.composer}</li>
                                            </ul>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${d.oldTrack.mobileShare != d.newTrack.mobileShare}">
                                            <ul class="unstyled">
                                                <li class="deleted">${d.oldTrack.mobileShare}</li>
                                                <li>${d.newTrack.mobileShare}</li>
                                            </ul>
                                        </c:if>
                                    </td>
                                    <td>
                                        <c:if test="${d.oldTrack.publicShare != d.newTrack.publicShare}">
                                            <ul class="unstyled">
                                                <li class="deleted">${d.oldTrack.publicShare}</li>
                                                <li>${d.newTrack.publicShare}</li>
                                            </ul>
                                        </c:if>
                                    </td>
                                </tr>
                            </c:forEach>

                            </tbody>
                        </table>

                        <div class="pagination pagination-centered">
                            <ul>
                                <c:if test="${from >= pageSize}">
                                    <li><a href="catalog-update-result?from=${from - pageSize}">&laquo;</a></li>
                                </c:if>

                                <c:forEach var="i" begin="1" end="${result.tracks / pageSize}" step="${pageSize}" varStatus ="status">
                                    <c:set var="pageClass" value="${from == (i - 1) * pageSize ? 'active' : ''}"/>
                                    <li style="${pageClass}">
                                        <a href="catalog-update-result?from=${(i - 1) * pageSize}">${i}</a>
                                    </li>
                                </c:forEach>

                                <c:if test="${from + pageSize < result.tracks}">
                                    <li><a href="catalog-update-result?from=${from + pageSize}">&raquo;</a></li>
                                </c:if>
                            </ul>
                        </div>

                    </c:otherwise>
                </c:choose>

                </p>

            </section>


            <hr/>

        </div>
    </div>

</div>


</body>
</html>