<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
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

    <title>Расчет квартального отчета</title>
</head>
<body>

<c:import url="navbar.jsp">
    <c:param name="report_calculate" value="active"/>
</c:import>

<div class="container">
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
            <c:import url="footer.jsp"/>

            <input type="button" onclick="calculateReports()" id="submitBut" value="Рассчитать">

    </div>
</div>

<script>

 function calculateReports() {
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
             success: function (data) {
               alert(data.report-items);
             }
         });
 };

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

</body>
</html>