<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="span4">

    <legend>
        Расчет и загрузка квартального отчета
    </legend>

    <label for="platform">Платформа</label>
    <select name="platform" id="platform" class="input-block-level">
        <c:forEach var="p" items="${platforms}" varStatus="s">
            <option class="${p.name}" value="${p.name}" ${s.index==0?"selected":""}>${p.name}</option>
        </c:forEach>
    </select>

    <label for="type">Тип отчета</label>
    <select name="ензу" id="type" class="input-block-level">
        <option value="public">Публичный</option>
        <option value="mobile">Мобильный</option>
    </select>

    <label for="date-from">C</label>

    <div id="date-from" class="input-append">
        <input data-format="yyyy-MM-dd" class="input-block-level" id=from name="dt" type="text">
                              <span class="add-on">
                                 <i data-time-icon="icon-time" data-whenUpdated-icon="icon-calendar">
                                 </i>
                                  </span>
    </div>

    <label for="date-to">По</label>

    <div id="date-to" class="input-append">
        <input data-format="yyyy-MM-dd" class="input-block-level" name="dt" id="to" type="text">
                              <span class="add-on">
                                 <i data-time-icon="icon-time" data-whenUpdated-icon="icon-calendar">
                                 </i>
                                  </span>
    </div>

    <br>

    <input type="button" onclick="calculateReports()" id="submitBut" value="Рассчитать">
    <br>
    <br>
    <br>

    <img id="loading-gif" src="../../../img/loading.gif" style="visibility: hidden">

    <div class="rowLayout">
        <div class="descLayout">
            <div class="pad">
                <b id="no-data" style="visibility: hidden"> Нет данных</b>

                <div id="table" style="visibility: hidden"></div>

            </div>
        </div>
    </div>
</div>


<script src="/js/bootstrap-fileupload.js"></script>
<script src="/js/bootstrap-datetimepicker.min.js"></script>
<script src="/js/jquery.handsontable.full.js"></script>

<script>
    var table = $('#table');

    table.handsontable({
        width: 900,
        readOnly: true,
        height: 500,
        font: "Arial",
        colHeaders: ["Код",
            "Трек",
            "Исполнитель",
            "Композитор",
            "Тип контента",
            "Цена",
            "Кол-во",
            "Объем",
            "Моб.доля",
            "Публ.доля",
            "Доля пользователя",
            "Доля каталога",
            "Вознаграждение",
            "Каталог",
            "права"
        ],
//        colWidths: [65, 47, 47, 47, 47, 47, 47, 47, 47, 47],
        rowHeaders: true,
//        fixedRowsTop: 2,
//        fixedColumnsLeft: 2,
        contextMenu: true
    });


    function calculateReports() {
        $('#loading-gif').css('visibility', 'visible');
        $('#no-data').css('visibility', 'hidden');
        table.css('visibility', 'hidden');

        $.ajax({
            url: "/admin/action/calculate-report",
            dataType: 'json',
            method: 'post',
            async: 'true',

            data: {
                'platform': $('#platform').val(),
                'from': $('#from').val(),
                'to': $('#to').val(),
                'type': $('#type').val()
            },
            error: function () {

            },
            success: function (resp) {
                $('#loading-gif').css('visibility', 'hidden');
                if (resp.report_items.length == 0) {
                    $('#no-data').css('visibility', 'visible');
                    return;
                }
                table.css('visibility', 'visible');

                var rows = [];
                for (var i = 0; i < resp.report_items.length; i++) {

                    var row = [];

                    row.push(resp.report_items[i].code);
                    row.push(resp.report_items[i].name);
                    row.push(resp.report_items[i].artist);
                    row.push(resp.report_items[i].composer);
                    row.push(resp.report_items[i].content_type);
                    row.push(resp.report_items[i].price);
                    row.push(resp.report_items[i].qty);
                    row.push(resp.report_items[i].vol);
                    row.push(resp.report_items[i].share_mobile);
                    row.push(resp.report_items[i].share_public);
                    row.push(resp.report_items[i].cust_royal);
                    row.push(resp.report_items[i].cat_royal);
                    row.push(resp.report_items[i].revenue);
                    row.push(resp.report_items[i].catalog);
                    row.push(resp.report_items[i].copyright);

                    rows.push(row);
                }
                table.handsontable({data: rows});
            }
        });
    }

    $(function () {
        $('#date-from').datetimepicker({
            pickTime: true
        });


    });
    $(function () {
        $('#date-to').datetimepicker({
            pickTime: true
        });


    });
</script>
