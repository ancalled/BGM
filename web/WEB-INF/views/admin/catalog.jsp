<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>

    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-fileupload.css" media="screen"/>
    <script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>

    <style>


        #catalog-download-progress {
            margin: 0px 40px 0px 0px;
        }

        #catalog-download-file-url {
            margin: 20px 20px 15px 0px;
        }


        .report-loaded {
            color: #CCCCCC;
        }

        .report-accepted {
            color: #0d370e;
        }

        table.reports {
            margin-top: 30px;
        }

        table.reports td {
            vertical-align: top;
        }

        table.reports dt {
            width: 110px;
        }

        table.reports dd {
            margin-left: 120px;
        }

    </style>

    <title>${catalog.name}</title>
</head>
<body>

<c:import url="navbar.jsp">
    <c:param name="index" value="active"/>
</c:import>

<div class="container">
    <div class="row">
        <section>
            <legend>Каталог ${catalog.name}</legend>

            <dl class="dl-horizontal">
                <dt>Тип прав</dt>
                <dd>
                    <c:choose>
                        <c:when test="${catalog.rightType eq 'AUTHOR'}">
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
                <dd><fmt:formatNumber type="number" maxFractionDigits="3"
                                      value="${catalog.tracks}"/></dd>

                <dt>Артистов</dt>
                <dd><fmt:formatNumber type="number" maxFractionDigits="3" value="${catalog.artists}"/></dd>
                <%--<dt class="fileDt">Файл каталога</dt>--%>
                <dt></dt>
                <dd>
                    <div id="catalog-download-file-url"></div>
                    <input class="btn btn-primary"
                           type="button"

                           value="Выгрузить каталог в csv"
                           id="downloadBtn">

                    <img id="catalog-download-progress" src="/img/loading.gif" style="visibility: hidden">
                </dd>


            </dl>

        </section>


    </div>

    <div class="row">

       <c:import url="catalog-update-upload.jsp"/>

        <section>

                <c:if test="${fn:length(updates) gt 0}">
            <h4>Предыдущие обновления</h4>

            <table class="reports">
                <c:forEach items="${updates}" var="u">
                    <c:set var="reportType" value=""/>
                    <c:choose>
                        <c:when test="${u.applied}">
                            <c:set var="reportType" value="report-accepted"/>
                        </c:when>
                        <c:otherwise>
                            <c:set var="reportType" value="report-loaded"/>
                        </c:otherwise>
                    </c:choose>
                    <tr class="${reportType}">
                        <td>
                            <a href="/admin/view/catalog-update?id=${u.id}">
                                <fmt:formatDate pattern="yyyy-MM-dd HH:mm:ss" value="${u.whenUpdated}"/>
                            </a>
                        </td>
                        <td>
                            <dl class="unstyled dl-horizontal">
                                <dt>Файл</dt>
                                <dd><a href="/admin/action/download-catalog-update?id=${u.id}">${u.fileName}</a></dd>

                                <dt>Состояние</dt>
                                <dd>
                                    <c:choose>
                                        <c:when test="${u.applied}">
                                            Применено
                                        </c:when>
                                        <c:otherwise>
                                            Ожидает подтверждения
                                        </c:otherwise>
                                    </c:choose>
                                </dd>

                                <dt>Всего треков</dt>
                                <dd><fmt:formatNumber type="number" maxFractionDigits="3" value="${u.tracks}"/></dd>

                                <dt>Новых</dt>
                                <dd><fmt:formatNumber type="number" maxFractionDigits="3" value="${u.newTracks}"/></dd>

                                <dt>Измененных</dt>
                                <dd><fmt:formatNumber type="number" maxFractionDigits="3"
                                                      value="${u.changedTracks}"/></dd>
                            </dl>
                        </td>
                    </tr>


                </c:forEach>
            </table>
            </c:if>
            </section>



    </div>
</div>



<script type="text/javascript">


$(document).ready(function () {

      $('#downloadBtn').click(function() {
          $('#downloadBtn').remove();
          $('#catalog-download-progress').css('visibility', 'visible');

          $.ajax({
              url: "/admin/action/download-catalog",
              dataType: 'json',
              method: 'post',
              async: 'true',

              data: {
                  'cid': $('#catId').val(),
                  'ft': ';',
//                'eb': '\\\'',
                  'lt': '\\n'
              },
              error: function () {
                  alert("Неудалось выгрузить каталог в файл" + $('#catName').val() + " .csv");
              },
              success: function (data) {
                  $("#catalog-download-file-url").append("<a href='" + data.path + "'>" +
                          "<i class='icon-download-alt'></i>" + "Скачать " +
                          $('#catName').val() + ".csv (" + Math.round(data.size / 1024 / 1024) + " Мб)" +
                          "</a>");
                  $('#catalog-download-progress').css('visibility', 'hidden');
              }
          });

      })


});
</script>


<c:import url="footer.jsp"/>

</body>
</html>