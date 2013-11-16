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
    <c:param name="send-report" value="active"/>
</c:import>


<div class="tabbable">
    <div class="container text-center">
        <div class="row text-left">
            <legend>
                Загрузка отчета
            </legend>
        </div>

        <div class="row text-left">

            <form action="/customer/action/upload-report"
                  method="post" enctype="multipart/form-data">


                <div class="form-group">
                    <div class="fileupload fileupload-new" data-provides="fileupload">
                        <div class="input-group">
                            <div class="form-control uneditable-input"><i class="icon-file fileupload-exists"></i>
                                <span class="fileupload-preview"></span>
                            </div>
                            <div class="input-group-btn">
                                <a class="btn btn-default btn-file">
                                    <span class="fileupload-new">Выбрать бновление</span>
                                    <span class="fileupload-exists">Изменить</span>
                                    <input type="file" name="file"/></a>
                                <a href="#" class="btn btn-default fileupload-exists"
                                   data-dismiss="fileupload">Убрать</a>
                            </div>
                        </div>
                    </div>
                </div>


            <%--<div class="fileupload fileupload-new" data-provides="fileupload">--%>
                    <%--<div class="input-append">--%>
                        <%--<div class="uneditable-input span3">--%>
                            <%--<i class="icon-file fileupload-exists"></i>--%>
                            <%--<span class="fileupload-preview"></span></div>--%>
                                <%--<span class="btn btn-fileName">--%>
                                    <%--<span class="fileupload-new">Выбрать отчет</span>--%>
                                    <%--<span class="fileupload-exists">Изменить</span><input name="file" type="file"/>--%>
                                <%--</span>--%>
                        <%--<a href="#" class="btn fileupload-exists" data-dismiss="fileupload">Удалить</a>--%>
                    <%--</div>--%>
                <%--</div>--%>


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
                        <option value="0">месяц</option>
                        <option value="1">квартал</option>
                    </select>
                </label>


                <div class="row-fluid">
                    <input class="btn" type="submit" value="Загрузить">
                </div>
            </form>

        </div>

    </div>
</div>

<script type="text/javascript">
    $(function () {
        $('#date').datetimepicker({
            pickTime: true
        });
    });
</script>

</body>
</html>