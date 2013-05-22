<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


<html>

<head>
    <script src="../../web/js/jquery.js"></script>
    <script src="../../web/js/bootstrap.js"></script>
    <script src="../../web/js/bootstrap-fileupload.js"></script>
    <script src="../../web/js/bootstrap-datetimepicker.min.js"></script>

    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">

    <link rel="stylesheet" type="text/css" href="../../web/css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="../../web/css/bootstrap-datetimepicker.min.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="../../web/css/bootstrap-fileupload.css" media="screen"/>
    <title>Обработка отчета</title>
</head>


<body>

<div class="navbar">
    <div class="navbar-inner">
        <a class="brand" href="/index.html">BGM Platform</a>
        <ul class="nav">
            <li><a href="/index.html">Главная</a></li>
            <li class="active"><a href="/data/old/report.jsp">Отчеты</a></li>
            <li><a href="/data/old/search.jspearch.jsp">Поиск</a></li>
            <li><a href="">Админка</a></li>
        </ul>
    </div>
</div>

<div class="tabbable">
    <ul class="nav nav-tabs">
        <li class="active"><a href="#tab1" data-toggle="tab">Загрузка клиентских отчетов</a></li>
        <li><a href="#tab2" data-toggle="tab">Формирование отчетов</a></li>
    </ul>
    <div class="tab-content">
        <div class="tab-pane active" id="tab1">
            <div class="container text-center">
                <div class="row text-left">
                    <legend>
                        Загрузка клиентского отчета
                    </legend>
                </div>

                <div class="row text-left">

                    <form action="/reporter" method="post" enctype="multipart/form-data">
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

                        <div class="alert alert-block">
                            <button type="button" class="close" data-dismiss="alert">&times;</button>
                            <h4>Формат отчета</h4>
                            Формат клиентского отчета Должна быть в Excel таки ;)

                        </div>

                        <div class="row-fluid">
                            <input class="btn" type="submit">
                        </div>
                    </form>

                </div>

            </div>
        </div>
        <div class="tab-pane" id="tab2">
            <div class="container text-center">
                <div class="row text-left">
                    <legend>
                        Выгрузка расчитаных отчетов
                    </legend>
                </div>
                <div class="row text-left">

                    <form action="/report-calculator" method="post">

                        <div class="row-fluid">
                            <b class="span4">Каталог</b>
                            <select>
                                <option> WCh</option>
                                <option> NMI_WEST</option>
                                <option> NMI</option>
                                <option> PMI_WEST</option>
                                <option> PMI</option>
                                <option> NMI related</option>
                                <option> PMI related</option>
                                <option> AMP</option>
                                <option>MSG_MCS</option>
                            </select>
                        </div>

                        <div class="row-fluid">
                            <b class="span4">
                                От
                            </b>
                            <%--<div class="well">--%>
                            <div id="from-date" class="input-append">
                                <input data-format="yyyy-MM-dd" type="text">
    <span class="add-on">
      <i data-time-icon="icon-time" data-whenUpdated-icon="icon-calendar">
      </i>
    </span>
                            </div>
                        </div>
                        <div class="row-fluid">
                            <b class="span4">

                                По
                            </b>
                            <%--<div class="well">--%>
                            <div id="till-date" class="input-append">
                                <input data-format="yyyy-MM-dd" type="text">
    <span class="add-on">
      <i data-time-icon="icon-time" data-whenUpdated-icon="icon-calendar">
      </i>
    </span>
                            </div>

                        </div>
                        <br>
                        <br>
                        <br>

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
    $(function () {
        $('#from-date').datetimepicker({
            pickTime: false
        });
    });
    $(function () {
        $('#till-date').datetimepicker({
            pickTime: false
        });
    });
</script>
</body>
</html>