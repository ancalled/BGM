<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>




    div.months-by-quarters {
        margin-top: 20px;
        overflow: hidden;
    }

    div.months-by-quarters div {
        display: block;
    }

    div.months-by-quarters div.incoming-report {
        width: 25%;
        float: left;
    }

    div.months-by-quarters div.clear {
        clear: both;
    }

    div.months-by-quarters div.year {
        font-size: 13pt;
        font-weight: bold;
        margin: 5px 0 3px 0;
    }

    div.incoming-report div.margins {
        margin: 0 10px 10px 0;
    }

    div.incoming-report div.content {
        height: 150px;
        background: #cceef1;
    }

    div.outgoing-report-report div.content {
        height: 150px;
    }

    div.incoming-report div.future {
        background: #eaeef1;
    }

    .content .header {
        padding-top: 5px;
        font-size: 14pt;
        text-align: center;
    }


    .content .no-reports {
        padding-top: 10px;
        width: 100%;
        text-align: center;
        /*color: #8f8f8f;*/
    }

    ul.reports {
        /*list-style: */
        padding: 10px 5px 5px 0;
        font-size: 10pt;
    }

    li.active a {
        /*color: #000000;*/
    }

    li.not-active a {
        color: #8f8f8f;
        /*text-decoration: line-through;*/
    }

</style>

<div class="span12">

    <section>
           <input class="btn btn-primary"
                  type="button"
                  value="Новый отчет"
                  id="downloadBtn">

    </section>

    <section>
        <div class="months-by-quarters">


            <div class="year"><fmt:formatDate pattern="yyyy" value="${now}"/></div>

                <c:forEach var="i" begin="0" end="${fn:length(months) - 1}" step="1">
                    <c:set var="m" value="${months[i]}"/>
                    <c:set var="mm" value="${months[i + 1]}"/>
                    <c:set var="y"><fmt:formatDate pattern="yyyy" value="${m}"/></c:set>
                    <c:set var="yy"><fmt:formatDate pattern="yyyy" value="${mm}"/></c:set>
                    <c:set var="mn"><fmt:formatDate pattern="MM" value="${m}"/></c:set>

                    <div class="incoming-report">
                        <div class="margins">
                            <div class="content ${m > now ? 'future' : ''}">
                                <div class="header"><fmt:formatDate pattern="MMMMM" value="${m}"/></div>

                        <c:set var="hasReports" value="no"/>

                        <ul class="reports">

                        <c:forEach items="${reports}" var="r">
                            <c:if test="${r.startDate gt mm and r.startDate lt m}">
                                <c:set var="status" value="${r.accepted ? 'active' : 'not-active'}"/>

                                    <li class="${r.accepted ? 'active' : 'not-active'}">
                                        <a href="report?id=${r.id}">
                                            ${not empty r.customer.name ? r.customer.name : 'Неизвестно' }
                                        </a>
                                    </li>

                                <c:set var="hasReports" value="yes"/>
                            </c:if>
                        </c:forEach>
                        </ul>

                        <c:if test="${hasReports == 'no'}">
                        <div class="no-reports">
                            Не было отчетов
                        </div>

                        </c:if>

                            </div>
                        </div>
                    </div>

                    <c:if test="${(mn - 1) % 3 == 0}">

                        <div class="outgoing-report">
                             <div class="content">
                                 <%--3 000 023 тг--%>
                             </div>
                        </div>

                        <div class="clear"></div>
                    </c:if>

                    <c:if test="${y > yy}">
                        <div class="year">${yy}</div>
                    </c:if>
                </c:forEach>



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

