<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>

    .active {
        color: #000000;
    }

    .no-reports {
        font-size: 9pt;
    }

    .not-active {
        color: #CCCCCC;
    }


    dl.months {
        margin: 40px 0 10px 0;
    }

    dl.months dt {
        font-weight: normal;
        font-size: 14pt;
        margin-left: 20px;
    }

    dl.months dd {
        margin: 10px 0 10px 80px;
    }

    dl.months dt.year {
        font-size: 15pt;
        margin: 10px 0 15px 0;
    }



</style>

<div class="span10">

    <section>
           <input class="btn btn-primary"
                  type="button"
                  value="Новый отчет"
                  id="downloadBtn">

    </section>

    <section>
        <dl class="months">
            <dt class="year"><fmt:formatDate pattern="yyyy" value="${now}"/></dt>
            <dd></dd>

            <c:forEach var="i" begin="0" end="${fn:length(months) - 1}" step="1">
                <c:set var="m" value="${months[i]}"/>
                <c:set var="mm" value="${months[i + 1]}"/>
                <c:set var="y"><fmt:formatDate pattern="yyyy" value="${m}"/></c:set>
                <c:set var="yy"><fmt:formatDate pattern="yyyy" value="${mm}"/></c:set>

                <dt><fmt:formatDate pattern="MMMMM" value="${m}"/></dt>
                <dd>
                    <c:set var="hasReports" value="no"/>

                    <c:forEach items="${reports}" var="r">
                        <c:if test="${r.startDate gt mm and r.startDate lt m}">
                            <c:set var="status" value="${r.accepted ? 'active' : 'not-active'}"/>
                            <ul class="inline ${status}">
                                <li><fmt:formatDate pattern="dd" value="${r.startDate}"/></li>

                                <li><a href="report?id=${r.id}">
                                ${not empty r.customer.name ? r.customer.name : 'Неизвестно' }
                                </a>
                                </li>
                                <li>
                                    <c:choose>
                                        <c:when test="${r.type.name == 'PUBLIC'}">
                                            Публичка
                                        </c:when>
                                        <c:otherwise>
                                            Мобильный
                                        </c:otherwise>
                                    </c:choose>
                                </li>
                                <li>
                                    <c:choose>
                                        <c:when test="${r.accepted ==true}">
                                            <i class="icon-ok"></i>
                                        </c:when>
                                        <c:otherwise>
                                            <i class="icon-remove"></i>
                                        </c:otherwise>
                                    </c:choose>
                                </li>
                            </ul>

                            <c:set var="hasReports" value="yes"/>
                        </c:if>
                    </c:forEach>

                    <c:if test="${hasReports == 'no'}">
                        <span class="no-reports">
                            Не было отчетов
                        </span>

                    </c:if>
                </dd>

                <c:if test="${y > yy}">
                    <dt class="year">${yy}</dt>
                    <dd></dd>
                </c:if>
            </c:forEach>

        </dl>

        <%--<dl class="dl-horizontal">--%>
            <%--<c:forEach items="${reports}" var="r">--%>
                <%--<c:set var="status" value="${r.accepted ? 'active' : 'not-active'}"/>--%>

                <%--<dt>--%>
                    <%--&lt;%&ndash;<a href="customer-detail?customer_id=${r.customerId}">&ndash;%&gt;--%>
                    <%--<a href="report?id=${r.id}">--%>
                            <%--${not empty r.customer.name ? r.customer.name : 'Неизвестно' }--%>
                    <%--</a>--%>
                <%--</dt>--%>
                <%--<dd>--%>
                    <%--<ul class="inline ${status}">--%>
                            <%--&lt;%&ndash;<li>${r.uploadDate}</li>&ndash;%&gt;--%>
                        <%--<li>${r.startDate}</li>--%>
                        <%--<li>--%>
                            <%--<c:choose>--%>
                                <%--<c:when test="${r.type.name == 'PUBLIC'}">--%>
                                    <%--Публичка--%>
                                <%--</c:when>--%>
                                <%--<c:otherwise>--%>
                                    <%--Мобильный--%>
                                <%--</c:otherwise>--%>
                            <%--</c:choose>--%>
                        <%--</li>--%>
                        <%--&lt;%&ndash;<li>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;<c:choose>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;<c:when test="${r.period.name == 'MONTH'}">&ndash;%&gt;--%>
                                    <%--&lt;%&ndash;Ежемесячный&ndash;%&gt;--%>
                                <%--&lt;%&ndash;</c:when>&ndash;%&gt;--%>
                                <%--&lt;%&ndash;<c:otherwise>&ndash;%&gt;--%>
                                    <%--&lt;%&ndash;Квартальный&ndash;%&gt;--%>
                                <%--&lt;%&ndash;</c:otherwise>&ndash;%&gt;--%>
                            <%--&lt;%&ndash;</c:choose>&ndash;%&gt;--%>
                        <%--&lt;%&ndash;</li>&ndash;%&gt;--%>
                        <%--<li>--%>
                            <%--<c:choose>--%>
                                <%--<c:when test="${r.accepted ==true}">--%>
                                    <%--<i class="icon-ok"></i>--%>
                                <%--</c:when>--%>
                                <%--<c:otherwise>--%>
                                    <%--<i class="icon-remove"></i>--%>
                                <%--</c:otherwise>--%>
                            <%--</c:choose>--%>
                        <%--</li>--%>
                    <%--</ul>--%>
                <%--</dd>--%>

            <%--</c:forEach>--%>
        <%--</dl>--%>


        <div id="upload-report-form">
            <c:forEach var="c" items="${customers}" varStatus="s">
                <input type="hidden" id="customer-${c.id}" value="${c.customerType}">
            </c:forEach>
            <form action="/admin/action/upload-report"
                  method="post" enctype="multipart/form-data">

                <label>
                    Пользователь <br>
                    <select name="customer-id" id="customer">

                        <c:forEach var="c" items="${customers}" varStatus="s">
                            <option class="${c.name}" value="${c.id}"
                                    id="${c.id}" ${s.index==0?"selected":""}>${c.name}</option>
                        </c:forEach>

                    </select>
                </label>


                <label>
                    Тип отчета <br>
                    <select id="repType" name="repType">

                        <option id="MOBILE_AGGREGATOR" value="MOBILE_AGGREGATOR">Мобильный</option>
                        <option value="PUBLIC_RIGHTS_SOCIETY" value="PUBLIC_RIGHTS_SOCIETY">Публичный</option>

                    </select>
                </label>

                <%--<input type="hidden" name="report-type" id="report-type"/>--%>

                <div class="fileupload fileupload-new" data-provides="fileupload">
                    <div class="input-append">
                        <div class="uneditable-input span3">
                            <i class="icon-file fileupload-exists"></i>
                            <span class="fileupload-preview"></span></div>
                                <span class="btn btn-fileName">
                                    <span class="fileupload-new">Выбрать отчет</span>
                                    <span class="fileupload-exists">Изменить</span><input name="file" type="file"/>
                                </span>
                        <a href="#" class="btn fileupload-exists" data-dismiss="fileupload">Удалить</a>
                    </div>
                </div>

                Дата отчета <br>

                <div id="date" class="input-append">
                    <input data-format="yyyy-MM-dd" id="dt" class="input-block-level" name="dt" type="text">
                              <span class="add-on">
                                 <i data-time-icon="icon-time" data-whenUpdated-icon="icon-calendar">
                                 </i>
                                  </span>
                </div>


                <label>
                    Период<br>
                    <select name="period">
                        <option value="0">Месячный</option>
                        <option value="1">Квартальный</option>
                    </select>
                </label>


                <div class="row-fluid">
                    <input class="btn" type="submit" id="submitReport" value="Отправить">
                </div>
            </form>


            <div>
            <span>
            Для загрузки отчета необходимо
            подготовить файл отчета в формате excel.
            </span>
            </div>
        </div>

    </section>


</div>


<script src="/js/bootstrap-fileupload.js"></script>
<script src="/js/bootstrap-datetimepicker.min.js"></script>
<script>

    $(document).ready(function () {
        $('#upload-report-form').hide();


        var erParam = getParam("er");
        if (erParam == "Wrong type") {
            alert("Вероятно указан неверный тип отчета");
        }
    });


    //    window.onload(function () {
    //         $('#submitReport').onclick(sendReport());
    //    });

    $('#type-report-change').change(function () {
        var v = $(this).val();
        if (v == 1) {
            $("#report-load-form").attr("action", "/admin/action/load-public-reports");
        }
        else if (v == 2) {
            $("#report-load-form").attr("action", "/admin/action/load-mobile-reports");
        }
    });


    $(function () {
        $('#date').datetimepicker({
            pickTime: true
        });


    });


    function sendReport() {
        $.ajax({
            url: "/admin/action/upload-report",
            dataType: 'json',
            method: 'post',
            async: 'true',

            data: {
                'customer-id': $('#customer').val(),
                'dt': $('#dt').val(),
                'repType': $('#repType').val()
            },
            error: function () {
                alert("Неудалось выгрузить каталог в файл" + $('#catName').val() + " .csv");
            },
            success: function (data) {
                $("#file-link").append("<a href='" + data.path + "'>" +
                        "<i class='icon-download-alt'></i>" + "Скачать " +
                        $('#catName').val() + ".csv (" + Math.round(data.size / 1024 / 1024) + " Мб)" +
                        "</a>");
                $('#loading-gif').css('visibility', 'hidden');
            }
        });
    }

    $('#customer').change(function () {
                var customerType = $('#customer-' + this.options[this.selectedIndex].id).val();
                if (customerType == 'MOBILE_AGGREGATOR') {
                    $('#repType').val('MOBILE_AGGREGATOR');
                } else if (customerType = 'PUBLIC_RIGHTS_SOCIETY') {
                    $('#repType').val('PUBLIC_RIGHTS_SOCIETY');
                }
            }
    );
    //
    //    $('#repType').change(function(){
    //        $('#report-type').val(this.options[this.selectedIndex].id);
    //    });


    var el = document.getElementById('date');

    //    el.on('changeDate', function (e) {
    //        alert(e.date.toString());
    //        console.log(e.localDate.toString());
    //    });


    //    $('#report-tab a:first').tab('show');


    function getParam(name) {
        name = name.replace(/[\[]/, "\\\[").replace(/[\]]/, "\\\]");
        var regex = new RegExp("[\\?&]" + name + "=([^&#]*)"),
                results = regex.exec(location.search);
        return results == null ? "" : decodeURIComponent(results[1].replace(/\+/g, " "));
    }


</script>

