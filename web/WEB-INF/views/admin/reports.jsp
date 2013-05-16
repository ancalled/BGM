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


<div class="tabbable">
    <ul class="nav nav-tabs" id="report-tab">
        <li><a href="#tab1" data-toggle="tab">Загрузка мобильного отчета</a></li>
        <li class="active"><a href="#tab2" data-toggle="tab">Загрузка публичного отчета</a></li>
        <li><a href="#tab3" data-toggle="tab">Квартальный отчет</a></li>
        <li><a href="#tab4" data-toggle="tab">Список всех отчетов</a></li>
    </ul>
    <div class="tab-content">
        <div class="tab-pane " id="tab1">
            <div class="container text-center">
                <div class="row text-left">
                    <legend>
                        Загрузка мобильного отчета
                    </legend>
                </div>

                <div class="row text-left">

                    <form action="/admin/action/upload-mobile-report"
                          method="post" enctype="multipart/form-data">


                        <div class="fileupload fileupload-new" data-provides="fileupload">
                            <div class="input-append">
                                <div class="uneditable-input span3">
                                    <i class="icon-file fileupload-exists"></i>
                                    <span class="fileupload-preview"></span></div>
                                <span class="btn btn-file">
                                    <span class="fileupload-new">Выбрать отчет</span>
                                    <span class="fileupload-exists">Изменить</span><input name="file" type="file"/>
                                </span>
                                <a href="#" class="btn fileupload-exists" data-dismiss="fileupload">Удалить</a>
                            </div>
                        </div>

                        <!--<div class="alert alert-block">-->
                        <!--<button type="button" class="close" data-dismiss="alert">&times;</button>-->
                        <!--<h4>Формат отчета</h4>-->
                        <!--Формат клиентского отчета Должна быть в Excel таки ;)-->

                        <!--</div>-->


                        <div class="well">
                            <div id="date" class="input-append">
                                <input data-format="yyyy-MM-dd" id="dt" class="input-block-level" name="dt" type="text">
                              <span class="add-on">
                                 <i data-time-icon="icon-time" data-date-icon="icon-calendar">
                                 </i>
                                  </span>
                            </div>
                        </div>


                        <label>
                            Дата отчета
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
        </div>
        <div class="tab-pane" id="tab2">
            <div class="container text-center">
                <div class="row text-left">
                    <legend>
                        Загрузка публичного отчета
                    </legend>
                </div>

                <div class="row text-left">

                    <form action="/admin/action/upload-public-report" method="post" enctype="multipart/form-data">
                        <div class="fileupload fileupload-new" data-provides="fileupload">
                            <div class="input-append">
                                <div class="uneditable-input span3">
                                    <i class="icon-file fileupload-exists"></i>
                                    <span class="fileupload-preview"></span></div>
                                <span class="btn btn-file">
                                    <span class="fileupload-new">Выбрать отчет</span>
                                    <span class="fileupload-exists">Изменить</span><input name="file" type="file"/>
                                </span>
                                <a href="#" class="btn fileupload-exists" data-dismiss="fileupload">Удалить</a>
                            </div>
                        </div>

                        <div class="alert alert-block">
                            <button type="button" class="close" data-dismiss="alert">&times;</button>
                            <h4>Формат отчета</h4>
                            Формат клиентского отчета Должна быть в Excel таки ;)

                        </div>

                        <div class="row-fluid">
                            <input class="btn" type="submit" value="Отправить">
                        </div>
                    </form>

                </div>

            </div>
        </div>
        <div class="tab-pane" id="tab3">
            <div class="container text-center">
                <div class="row text-left">
                    <legend>
                        Выгрузка расчитаных отчетов
                    </legend>
                </div>
                <div class="row text-left">

                    <label for="type-report-change">Тип отчета</label>
                    <select id="type-report-change">

                        <option value="1">Публичный</option>
                        <option value="2">Мобильный</option>
                    </select>

                    <form action="/admin/action/load-mobile-reports" id="report-load-form" method="post">

                        <label for="catalog">Каталог</label>
                        <select name="catalog" id="catalog">
                            <option value="WCh">WCh</option>
                            <option value="NMI_WEST">NMI_WEST</option>
                            <option value="NMI">NMI</option>
                            <option value="PMI_WEST">PMI_WEST</option>
                            <option value="PMI">PMI</option>
                            <option value="NMI related">NMI related</option>
                            <option value="PMI related">PMI related</option>
                            <option value="Sony ATV">Sony ATV</option>
                            <option value="MSG_MCS">MSG_MCS</option>
                        </select>

                        <div class="row-fluid">
                            <input class="btn" type="submit" value="Загрузить отчеты">
                        </div>
                    </form>
                </div>
            </div>
        </div>
        <div class="tab-pane" id="tab4">
            <div class="container text-center">
                <div class="row text-left">
                    <legend>
                        Список всех клиентских отчетов
                    </legend>
                </div>
                <div class="row text-left">

                    <form action="/admin/view/report-calculator" method="post">


                        <div class="row-fluid">
                            <input class="btn" type="submit" value="Загрузить отчеты">
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>


<script type="text/javascript">

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

    var el = document.getElementById('date');

    //    el.on('changeDate', function (e) {
    //        alert(e.date.toString());
    //        console.log(e.localDate.toString());
    //    });


    $(function () {
        $('#tab3').tab('show');
    })


</script>

</body>
</html>