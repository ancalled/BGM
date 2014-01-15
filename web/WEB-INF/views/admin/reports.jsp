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
    <title>Обработка отчета</title>
</head>


<body>

<c:import url="navbar.jsp">
    <c:param name="reports" value="active"/>
</c:import>


<div class="container text-center">
    <div class="row text-left">
        <legend>
            Загрузка отчета
        </legend>
    </div>

    <div class="row text-left">
        <c:forEach var="c" items="${customers}" varStatus="s">
            <input type="hidden" id="customer-${c.id}" value="${c.customerType}">
        </c:forEach>
        <form action="/admin/action/upload-report"
              method="post" enctype="multipart/form-data">

            <label>
                Пользователь <br>
                <select name="selector" id="customer">

                    <c:forEach var="c" items="${customers}" varStatus="s" >
                        <option class="${c.name}" value="${s.index}" id="${c.id}">${c.name}</option>
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

            <input type="hidden" name="customer-id" id="customer-id"/>
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
                <input class="btn" type="submit" value="Отправить">
            </div>
        </form>

    </div>

</div>

<script>

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


    $('#customer').change(function () {
                $('#customer-id').val(this.options[this.selectedIndex].id);
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


</script>

<c:import url="footer.jsp"/>

</body>
</html>