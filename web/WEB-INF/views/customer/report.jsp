<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html xmlns="http://www.w3.org/1999/html">

<head>
    <script src="/js/jquery.js"></script>
    <script src="/js/bootstrap.js"></script>
    <script src="/js/bootstrap-fileupload.js"></script>
    <script src="/js/bootstrap-datetimepicker.min.js"></script>
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
                <fmt:formatDate pattern="yyyy MMMMM" value="${report.startDate}" />
            </dd>
            <dt>Период отчета</dt>
            <dd>${report.period}</dd>
            <%--<dt>Тип</dt>--%>
            <%--<dd>${report.type}</dd>--%>
            <%--<dt>Компания</dt>--%>
            <%--<dd>${customer.name}</dd>--%>
            <dt>Треков</dt>
            <dd>${report.tracks}</dd>
            <dt>Определилось</dt>
            <dd>${report.detected}</dd>
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
                <th>#</th>
                <th>Код</th>
                <th>Композиция</th>
                <th>Исполнитель</th>
                <th>Определилось</th>
                <%--<th>Авторы</th>--%>
                <%--<th>Тип контента</th>--%>
                <th>Цена</th>
                <th>Количество</th>
                <%--<th>Определилось</th>--%>
            </tr>
            </thead>
            <tbody>

            <c:forEach items="${items}" var="i" varStatus="loop">
                <tr class="${i.detected ? '' : 'not-found'}">
                    <td class="invariant">${loop.index + start}</td>
                    <td class="invariant">${i.number}</td>
                    <td>${i.track}</td>
                    <td>${i.artist}</td>
                    <td>
                        <c:if test="${i.detected}">
                            <span>${i.foundTrack.name}</span>
                            <span>${i.foundTrack.artist}</span>
                        </c:if>
                    </td>

                <%--<td>${i.authors}</td>--%>
                    <%--<td>${i.contentType}</td>--%>
                    <td>${i.price}</td>
                    <td>${i.qty}</td>
                    <%--<td>${i.detected ? "да" : "нет"}</td>--%>
                </tr>
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

</div>


</body>
</html>