<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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

        .invariant {
            color: #1a6383;
        }

        ul.bottom-aligned li {
            vertical-align: top;
        }

        .apply-btn {
            margin: 20px 0 0 0;
        }

        div.apply-pane {
            padding: 10px 0 5px 0;
            background-color: #edf9f9;
            width: 60%;
            margin: 5px 0 15px 0;
        }

        .disabled {
            pointer-events: none;
            cursor: default;
        }

        ul.warnings {
            height: 200px;
            overflow: scroll;
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

                <div class="apply-pane">

                    <dl class="unstyled dl-horizontal">
                        <dt>Файл</dt>
                        <dd>${update.file}</dd>
                        <dt>Всего треков</dt>
                        <dd>${update.tracks}</dd>
                        <dt>Новых / измененных</dt>
                        <dd>${update.tracks - update.crossing} / ${update.crossing}</dd>
                        <dt></dt>
                        <dd>
                            <c:choose>
                                <c:when test="${update.applied}">
                                    <strong>Применен</strong>
                                </c:when>
                                <c:otherwise>
                                    <%--<c:if test="${update.status == 'OK'}">--%>
                                        <form action="../action/appy-catalog-update" method="post">
                                            <input class="btn btn-small btn-primary apply-btn" type="submit"
                                                   value="Применить!">
                                        </form>
                                    <%--</c:if>--%>
                                </c:otherwise>
                            </c:choose>

                        </dd>
                    </dl>

                </div>

            </section>

            <c:if test="${!update.applied}">
                <section>
                    <c:if test="${update.status == 'HAS_WARNINGS'}">
                        <h4>Ошибки (${fn:length(update.warnings)}):</h4>

                        <ul class="unstyled warnings">
                            <c:forEach items="${update.warnings}" var="w">
                                <li>Строка <strong>${w.row}</strong> поле <strong>${w.column}</strong>: ${w.message}</li>
                            </c:forEach>
                        </ul>

                    </c:if>
                    <h4>Изменения ${from + 1} &ndash; ${from + pageSize}</h4>

                    <div class="pagination pagination-centered">
                        <ul>
                            <c:choose>
                                <c:when test="${from >= pageSize}">
                                    <li><a href="catalog-update?from=${from - pageSize}">&laquo;</a></li>
                                </c:when>
                                <c:otherwise>
                                    <li class="disabled"><a href="#">&laquo;</a></li>
                                </c:otherwise>
                            </c:choose>

                            <c:forEach var="i" begin="1" end="${(update.crossing / pageSize) + 1}" step="1"
                                       varStatus="status">
                                <li class="${from == (i - 1) * pageSize ? 'active' : ''}">
                                    <a href="catalog-update?from=${(i - 1) * pageSize}">${i}</a>
                                </li>
                            </c:forEach>

                            <c:choose>
                                <c:when test="${from + pageSize < update.crossing}">
                                    <li><a href="catalog-update?from=${from + pageSize}">&raquo;</a></li>
                                </c:when>
                                <c:otherwise>
                                    <li class="disabled"><a href="#">&raquo;</a></li>
                                </c:otherwise>
                            </c:choose>
                        </ul>
                    </div>

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
                        </tr>
                        </thead>
                        <tbody>

                        <c:forEach items="${diffs}" var="d">
                            <tr>
                                <td class="invariant">${d.number + 1}</td>
                                <td class="invariant">${d.code}</td>
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
                            <c:choose>
                                <c:when test="${from >= pageSize}">
                                    <li><a href="catalog-update?from=${from - pageSize}">&laquo;</a></li>
                                </c:when>
                                <c:otherwise>
                                    <li class="disabled"><a href="#">&laquo;</a></li>
                                </c:otherwise>
                            </c:choose>

                            <c:forEach var="i" begin="1" end="${(update.crossing / pageSize) + 1}" step="1"
                                       varStatus="status">
                                <li class="${from == (i - 1) * pageSize ? 'active' : ''}">
                                    <a href="catalog-update?from=${(i - 1) * pageSize}">${i}</a>
                                </li>
                            </c:forEach>

                            <c:choose>
                                <c:when test="${from + pageSize < update.crossing}">
                                    <li><a href="catalog-update?from=${from + pageSize}">&raquo;</a></li>
                                </c:when>
                                <c:otherwise>
                                    <li class="disabled"><a href="#">&raquo;</a></li>
                                </c:otherwise>
                            </c:choose>
                        </ul>
                    </div>


                    </p>

                </section>

            </c:if>

            <hr/>

        </div>
    </div>

</div>


</body>
</html>