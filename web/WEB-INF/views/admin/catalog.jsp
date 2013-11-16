<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>

    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-fileupload.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/csv-preview.css" media="screen"/>

    <style>
        #random-tracks {
            margin-top: 100px;
            padding: 10px 20px 10px 30px;
            background-color: #edf9f9;
        }

        #random-tracks li {
            padding-bottom: 7px;
        }

        .dragtable-sortable {
            list-style-type: none;
            margin: 0;
            padding: 0;
            -moz-user-select: none;
        }

        .dragtable-sortable li {
            margin: 0;
            padding: 0;
            float: left;
            font-size: 1em;
            background: white;
        }

        .dragtable-sortable th, .dragtable-sortable td {
            border-left: 0px;
        }

        .dragtable-sortable li:first-child th, .dragtable-sortable li:first-child td {
            border-left: 1px solid #CCC;
        }

        .ui-sortable-helper {
            opacity: 0.7;
            filter: alpha(opacity=70);
        }

        .ui-sortable-placeholder {
            -moz-box-shadow: 4px 5px 4px #C6C6C6 inset;
            -webkit-box-shadow: 4px 5px 4px #C6C6C6 inset;
            box-shadow: 4px 5px 4px #C6C6C6 inset;
            border-bottom: 1px solid #CCCCCC;
            border-top: 1px solid #CCCCCC;
            visibility: visible !important;
            background: #EFEFEF !important;
            visibility: visible !important;
        }

        .ui-sortable-placeholder * {
            opacity: 0.0;
            visibility: hidden;
        }

        div.pane {
            padding: 10px 0 5px 0;
            background-color: #edf9f9;
            width: 60%;
            margin: 5px 0 15px 0;
        }

        table.preview {
            font-size: 10px;
            margin: 5px 20px 5px 5px;
        }

        #preview-container {
            height: 40%;
        }

        .table, .preview {
            max-width: none;
        }

        #upload-options {

        }

        #progress {
            width: 300px;
        }

        .bar {
            height: 10px;
            background: green;
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
        <div class="span7">

            <section>
                <legend>
                    Каталог ${catalog.name}
                </legend>


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
                    <dd>
                        <fmt:formatNumber type="number" maxFractionDigits="3" value="${catalog.tracks}"/>
                    </dd>

                    <dt>Артистов</dt>
                    <dd>
                        <fmt:formatNumber type="number" maxFractionDigits="3" value="${catalog.artists}"/>
                    </dd>
                </dl>


            </section>

        </div>

        <%--<div class="span4">--%>
        <%--<div id="random-tracks">--%>
        <%--<h4>Случайно всплыло:</h4>--%>

        <%--<ul class="unstyled">--%>
        <%--<c:forEach var="t" items="${randomTracks}">--%>
        <%--<li>--%>
        <%--<a href="#">--%>
        <%--${t.name}<c:if test="${not empty t.artist}">: ${t.artist}</c:if>--%>
        <%--</a>--%>
        <%--</li>--%>
        <%--</c:forEach>--%>
        <%--</ul>--%>
        <%--</div>--%>
        <%--</div>--%>
    </div>

    <div class="row">
        <div class="span10"/>

        <section>

            <legend>
                Обновления
            </legend>


            <p>
                <c:forEach items="${updates}" var="u">
                <c:if test="${u.applied}">
            <dd>

            </dd>

            </c:if>
            </c:forEach>
            </p>


            <%--<c:if test="${u.applied}">--%>
            <%--<dl class="dl-horizontal">--%>
            <%--<dt>Обновлен</dt>--%>
            <%--<dd>${u.whenUpdated}</dd>--%>

            <%--<dt>Файл</dt>--%>
            <%--<dd>${u.fileName}</dd>--%>

            <%--<dt>Новых треков</dt>--%>
            <%--<dd>${u.tracks - u.crossing}</dd>--%>

            <%--<dt>Измененных</dt>--%>
            <%--<dd>${u.crossing}</dd>--%>
            <%--</dl>--%>
            <%--</c:if>--%>

            <%--</c:forEach>--%>


            <form class="form-horizontal" action="../action/update-catalog" method="post"
                  enctype="multipart/form-data">

                <%--test--%>
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
                                    <input type="file" name="file" class="file-input" id="file-input" accept=".csv"
                                           data-url="../action/update-catalog"/></a>
                                <a href="#" class="btn btn-default fileupload-exists"
                                   data-dismiss="fileupload">Убрать</a>
                            </div>
                        </div>
                    </div>
                </div>
                <%--test--%>

                <%--<div class="fileupload fileupload-new" data-provides="fileupload">--%>
                    <%--<div class="input-append">--%>
                        <%--<div class="uneditable-input span3">--%>
                            <%--<i class="icon-file fileupload-exists"></i>--%>
                            <%--<span class="fileupload-preview"></span>--%>
                        <%--</div>--%>

                <%--<span class="btn btn-fileName">--%>
                    <%--<span class="fileupload-new">Выбрать обновление</span>--%>
                    <%--<span class="fileupload-exists">Изменить</span>--%>

                    <%--<input name="file" type="file" id="file-input" accept=".csv" data-url="../action/update-catalog"/>--%>
                <%--</span>--%>

                        <%--<a href="#" class="btn fileupload-exists" data-dismiss="fileupload">Убрать</a>--%>
                    <%--</div>--%>
                <%--</div>--%>

                <div class="row-fluid">

                    <div id="progress" class="progress progress-success progress-striped">
                        <div class="bar" style="width: 0%;"></div>
                    </div>

                    <input class="btn btn-primary"
                           type="submit"
                           value="Загрузить"
                           id="sbmtBtn"
                           disabled="disabled">

                    <span id="status">Загружено <span id="load-status"></span></span>
                </div>

                <input type="hidden" name="catId" value="${catalog.id}">
                <%--<input type="button" id="test" onclick="getLoadStatus()" value="TEST">--%>

                <br>
                <span id="example">Файл csv должен содержать следующие поля</span>

                <%--<div id="upload-options">--%>
                <%--<div class="control-group">--%>
                <%--<label class="control-label" for="inputEmail">Email</label>--%>
                <%--<div class="controls">--%>
                <%--<input type="text" id="inputEmail" placeholder="Email">--%>
                <%--</div>--%>
                <%--</div>--%>

                <%--</div>--%>
            </form>


            <div id="preview-container"></div>
        </section>

    </div>
</div>


<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
<script src="/js/bootstrap.js"></script>
<script src="/js/bootstrap-fileupload.js"></script>
<script src="/js/csv-helper.js"></script>
<script src="/js/jquery.ui.widget.js"></script>
<script src="/js/jquery.iframe-transport.js"></script>
<script src="/js/jquery.fileupload.js"></script>
<script type="text/javascript">

    $("#status").hide();

    //    var rowsOnPreview = 10;
    var rowsOnPreview = 100;
    var fileData;
    var delimiter = ';';
    var headers = ['#', 'code', 'name', 'composer', 'artist', 'share mobile', 'share public'];
    var empty = ['', '', '', '', '', '', ''];
    var testData = [
        ['Номер', 'Код композиции', 'Название песни', 'Имя автора', 'Имя исполнителя', 'Моб.контент', 'Публичка']
    ];


    $(document).ready(function () {

        $('#file-input').fileupload({
            dataType: 'json',

            done: function (e, data) {

                $('#progress').html("");

                if (data.result.status == 'ok') {
                    console.log('Redirect url: ' + data.result.redirect)
                    $('#status-bar').html("<strong>Загружено</strong>");
                    window.location.href = data.result.redirect;

                } else if (data.result.status == 'warn') {
                    console.log('Got uplaod waringns: ');
                    $.each(data.result.warningsList, function (index, wrn) {
                        console.log(wrn);
                    });

                    $('#status-bar').html("<strong>Неверный формат данных в файле!</strong>");

                    window.location.href = data.result.redirect;

                } else if (data.result.status == 'error') {
                    $('#status-bar').html("<strong>Ошибка!</strong>");

                    console.log('Got uplaod error: ' + data.result.er);
                }

            },

            add: function (e, data) {
                $("#sbmtBtn").click(function () {
                    $("#status").show();
                    getLoadStatus();
                    data.context = $('<p/>').text('Загрузка...').replaceAll($(this));
                    data.submit();
                });
            },

            progressall: function (e, data) {
                var progress = parseInt(data.loaded / data.total * 100, 10);
                $('#progress .bar').css('width', progress + '%');
            }
        });


        $("#file-input").on('change', function (e) {
            var f = this.files[0];
            if (this.files && f) {
                var reader = new FileReader();
                reader.onload = function (e) {
                    processContents(event.target.result, delimiter);
                };

//                reader.readAsText(f, "UTF8");
                reader.readAsText(f.slice(0, 1024 * 100), "UTF8");
            }
        });


    });

    var tbl_test = buildTable(testData, headers, 0, 1, "preview-table", 'table table-bordered preview');

    var $preview = $("#preview-container");
    $preview.addClass('csv-preview');
    $preview.html(tbl_test);

    function askForDone() {

        $.ajax({
            url: "../action/dbActivityServlet",
            dataType: 'json',
            error: function () {
                alert("Error Occured");
            },
            success: function (data) {
                alert(data.status);
            }
        });
    }


    function processContents(contents, delim) {
        fileData = contents;
        var data = parseCSV(contents, delim);

        console.log('Got data of ' + data.length);

        if (validateDate(data, headers)) {
            $('#sbmtBtn').removeAttr('disabled');
        }

        var tbl = buildTable(data, headers, 0, rowsOnPreview, "preview-table", 'table table-bordered preview');

        $('#example').html('');

        var $preview = $("#preview-container");
        $preview.addClass('csv-preview');
        $preview.html(tbl);

//        if (data.length > rowsOnPreview) {
//            $preview.append("<div class='preview-more'>... и еще " + addCommas(data.length - rowsOnPreview) + " строк </div>")
//        }

        if ($('#preview-table').width() > $preview.width() || $('#preview-table').height() > $preview.height()) {
            $preview.css('overflow', 'scroll');
        }


    }

    function getLoadRowsCount() {

        $.ajax({
            url: "../action/get-id",
            dataType: 'json',
            method: 'post',
            async: 'true',
            error: function () {
                alert("Error Occured");
            },
            success: function (data) {
                $('#load-status').html(data.rows);
            }
        });
    }


    var i = 0;
    var stop = false;

    function getLoadStatus() {
        setTimeout(getLoadRowsCount, 30);

        while (stop) {
            getLoadStatus();
        }
    }

</script>

</body>
</html>