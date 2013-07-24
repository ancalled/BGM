<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html xmlns="http://www.w3.org/1999/html">

<head>
    <script src="/js/jquery.js"></script>
    <script src="/js/bootstrap.js"></script>
    <%--<script src="/js/bootstrap-fileupload.js"></script>--%>
    <%--<script src="/js/bootstrap-datetimepicker.min.js"></script>--%>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-fileupload.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-datetimepicker.min.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-responsive.css" media="screen"/>
    <title>Главная</title>
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

        tr.not-found {
            background: #fbfee1;
        }

        tr.same-track td {
            border-top: none;
            /*padding: 5px 0 0 0;*/
            line-height: 15px;
        }

        td.number {
            text-align: right;
            padding-right: 20px;
        }

        td.centered {
            text-align: center;
        }

        .nonwrapped {
            white-space: nowrap;
        }

        .author {
            padding: 0 4px 0 4px;
            background: #ffe9c9;
        }

        .related {
            padding: 0 4px 0 4px;
            background: #cdfef9;
        }

        .author_related {
            padding: 0 4px 0 4px;
            background: #e6e6e6;
        }
    </style>
</head>

<body>

<%--<c:import url="navbar.jsp">--%>
<%--<c:param name="reports" value="active"/>--%>
<%--</c:import>--%>

<div class="navbar">
    <div class="navbar-inner">
        <a class="brand" href="/customer/view/index">BGM Platform</a>
        <ul class="nav">
            <li class=""><a href="/customer/view/index">Главная</a></li>
            <li class=""><a href="/customer/view/send-report">Отправить отчет</a></li>
            <li class="active"><a href="/customer/view/reports">Отчеты</a></li>
            <li class=""><a href="/customer/view/search">Поиск</a></li>
            <li class=""><a href="/customer/view/basket">Корзина</a></li>
            <li><a href="/customer/action/logout">Выход</a></li>
        </ul>
    </div>
</div>

<div class="container">
    <section>
        <h4>Отчет загружен</h4>

        <dl class="dl-horizontal">
            <dt>Номер</dt>
            <dd>${report.id}</dd>
            <dt>Дата отчета</dt>
            <dd>
                <fmt:formatDate pattern="yyyy MMMMM" value="${report.startDate}"/>
            </dd>
            <dt>Период отчета</dt>
            <dd>${report.period}</dd>
            <%--<dt>Тип</dt>--%>
            <%--<dd>${report.type}</dd>--%>
            <%--<dt>Компания</dt>--%>
            <%--<dd>${customer.name}</dd>--%>
            <dt>Треков</dt>
            <dd>${report.tracks} / ${report.detected}</dd>
            <c:if test="${report.accepted}">
                <dt></dt>
                <dd><strong>Подтвержден</strong></dd>
            </c:if>

        </dl>

    </section>

    <section>

        <div class="pagination pagination-centered">
            <ul>
                <c:choose>
                    <c:when test="${from >= size}">
                        <li><a href="report?id=${report.id}&from=${from - size}">&laquo;</a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="disabled"><a href="#">&laquo;</a></li>
                    </c:otherwise>
                </c:choose>

                <c:forEach var="i" begin="1" end="${(report.detected / size) + 1}" step="1"
                           varStatus="status">
                    <li class="${from == (i - 1) * size ? 'active' : ''}">
                        <a href="report?id=${report.id}&from=${(i - 1) * size}">${i}</a>
                    </li>
                </c:forEach>

                <c:choose>
                    <c:when test="${from + size < update.crossing}">
                        <li><a href="report?id=${report.id}&from=${from + size}">&raquo;</a></li>
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
                <th>Код</th>
                <th>Композиция</th>
                <th>Исполнитель</th>
                <th>Цена</th>
                <th class="nonwrapped">Кол-во</th>
                <th>Определилось</th>
                <th class="nonwrapped">Каталог</th>
                <th>Доля</th>
            </tr>
            </thead>
            <tbody>

            <c:set var="lastNum" value="0"/>
            <c:forEach items="${items}" var="i" varStatus="loop">
                <tr class="${i.detected ? '' : 'not-found'} ${lastNum == i.number ? 'same-track' : ''}">
                        <%--<td class="invariant">${loop.index + start}</td>--%>
                    <td class="invariant number">
                        <c:if test="${lastNum != i.number}">
                            ${i.number}
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${lastNum != i.number}">
                            ${i.track}
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${lastNum != i.number}">
                            ${i.artist}
                        </c:if>
                    </td>
                    <td class="number">
                        <c:if test="${lastNum != i.number}">
                            ${i.price}
                        </c:if>
                    </td>
                    <td class="number">
                        <c:if test="${lastNum != i.number}">
                            ${i.qty}
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${i.detected}">
                            <span>${i.foundTrack.artist} &mdash; </span>
                            <span>${i.foundTrack.name}</span>
                            <span class="nonwrapped" style="padding-left: 10px; font-style: italic">#${i.foundTrack.code}</span>
                        </c:if>
                    </td>
                        <%--<td class="centered">--%>
                    <td>
                        <c:if test="${i.detected}">

                            <span class="nonwrapped ${fn:toLowerCase(i.foundTrack.foundCatalog.rightType)}">
                                    ${i.foundTrack.catalog}
                            </span>
                            <%--<br/>--%>
                            <%--<span class="nonwrapped">${i.foundTrack.code}</span>--%>

                        </c:if>
                    </td>
                    <td class="number">
                        <c:if test="${i.detected}">
                            ${i.foundTrack.mobileShare}%
                        </c:if>
                    </td>
                    <td>
                        <c:if test="${i.detected}">
                            <a href="#_" class="remove-track" id="${i.id}" title="Этот трек определился неверно, убрать">
                                <i class="icon-remove"></i>
                            </a>
                        </c:if>
                    </td>
                </tr>

                <c:set var="lastNum" value="${i.number}"/>
            </c:forEach>


            </tbody>
        </table>


        <div class="pagination pagination-centered">
            <ul>
                <c:choose>
                    <c:when test="${from >= size}">
                        <li><a href="report?id=${report.id}&from=${from - size}">&laquo;</a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="disabled"><a href="#">&laquo;</a></li>
                    </c:otherwise>
                </c:choose>

                <c:forEach var="i" begin="1" end="${(report.detected / size) + 1}" step="1"
                           varStatus="status">
                    <li class="${from == (i - 1) * size ? 'active' : ''}">
                        <a href="report?id=${report.id}&from=${(i - 1) * size}">${i}</a>
                    </li>
                </c:forEach>

                <c:choose>
                    <c:when test="${from + size < update.crossing}">
                        <li><a href="report?id=${report.id}&from=${from + size}">&raquo;</a></li>
                    </c:when>
                    <c:otherwise>
                        <li class="disabled"><a href="#">&raquo;</a></li>
                    </c:otherwise>
                </c:choose>
            </ul>
        </div>
    </section>

    <div id="track-remove-modal" class="modal hide fade" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
         aria-hidden="true">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>
            <h3 id="myModalLabel">Удаление трека</h3>
        </div>
        <div class="modal-body">
            <p>Удалить строку?</p>
        </div>
        <div class="modal-footer">
            <button class="btn" data-dismiss="modal">Нет</button>
            <button class="btn btn-primary" id="modal-remove-btn" aria-hidden="true">Да</button>
        </div>
    </div>

</div>



<script>
    $(document).ready(function () {

        $('a.remove-track').click(function () {
            var a = $(this);
            var itemId = a.attr('id');

            $.post('/customer/api/remove-from-report',
                    {report_id: "${report.id}",
                        item_id: itemId
                    }, function (data) {
                        console.log(data);
                        if (data.status == 'ok') {
                            $('#' + itemId).parent().parent().remove()
                        }
                    });
        });

    });

</script>


</body>
</html>