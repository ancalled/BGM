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

        #loading-gif {
            margin: 0px 40px 0px 0px;
        }

        #file-link {
            margin: 20px 20px 15px 0px;
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

        .fileDt {
            margin: 20px 20px 0px 0px;
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
                    <%--<dt class="fileDt">Файл каталога</dt>--%>
                    <dt class="fileDt"></dt>
                    <dd>
                        <div id="file-link"></div>
                        <input class="btn btn-primary"
                               type="button"
                               onclick="downloadCatalog()"
                               value="Выгрузить каталог в csv"
                               id="downloadBtn">

                        <img id="loading-gif" src="../../../img/loading.GIF" style="visibility: hidden">
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

    <input type="hidden" id="catId" name="catalog_id" value="${catalog.id}">
    <input type="hidden" id="catName" name="catalog_name" value="${catalog.name}">

    <br>

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


            <form class="form-horizontal" action="/admin/action/update-catalog" method="post"
                  enctype="multipart/form-data">

                <div class="form-group">
                    <div class="fileupload fileupload-new" data-provides="fileupload">
                        <div class="inline">

                            <div class="input-group-btn">
                                <a class="btn btn-primary btn-file">
                                    <span class="fileupload-new">Выбрать бновление</span>
                                    <span class="fileupload-exists">Изменить</span>
                                    <input type="file" name="file" class="file-input" id="fileinput" accept=".csv"
                                           data-url="/admin/action/update-catalog"/></a>
                                <a href="#" class="btn btn-default fileupload-exists"
                                   data-dismiss="fileupload">Убрать</a>
                            </div>

                            <%--<div class="form-control uneditable-input"><i class="icon-file fileupload-exists"></i>--%>
                                <%--<span class="fileupload-preview"></span>--%>
                            <%--</div>--%>
                        </div>
                    </div>
                </div>


                <div class="row-fluid">

                    <div id="progress" class="progress progress-success progress-striped">
                        <div class="bar" style="width: 0%;"></div>
                    </div>

                    <input class="btn btn-primary"
                           type="submit"
                           value="Загрузить"
                           id="sbmtBtn"
                           disabled="disabled">

                    <div id="status-bar"></div>
                </div>

                <input type="hidden" name="catId" value="${catalog.id}">

                <br>
                <span id="example">Файл обновления должен быть в формате <strong><i>csv</i></strong> и содержать поля, как указано в таблице ниже.
                    Разделитель '<strong>;</strong>', кодировка <strong>utf-8</strong>
                </span>


            </form>

            <div id="preview-container"></div>
        </section>

    </div>
</div>


<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>
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

// csv parameters
var encoding = 'utf8';
var delimiter = ';';
var enclosedBy = '\\';
var newline = '\n';
var fromLine = 1;

var headers = ['#', 'Код', 'Композиция', 'Автор произведения', 'Исполнитель', 'Доля / моб. контент', 'Доля / публичка'];
var empty = ['', '', '', '', '', '', ''];
var testData = [
    ['1', '4235', 'Ramble On', 'Robert Plant', 'Led Zeppelin', '70', '100']
];


$(document).ready(function () {

    $('#fileinput').fileupload({
        dataType: 'json',

        done: function (e, data) {

            $('#progress').html("");

            if (data.result.status == 'ok') {
                $('#status-bar').html("<strong>Файл загружен на сервер, идет обработка...</strong>");
                var updateId = data.result.uid;
                startTaskStatusChecker(updateId);

            } else if (data.result.status == 'warn') {
                $.each(data.result.warningsList, function (index, wrn) {
                    console.log(wrn);
                });

                $('#status-bar').html("<strong>Неверный формат данных в файле!</strong>");
//                    window.location.href = data.result.redirect;

            } else if (data.result.status == 'error') {
                $('#status-bar').html("<strong>Ошибка!</strong>");
            }

        },

        add: function (e, data) {
            $("#sbmtBtn").click(function () {
                $("#status").show();
//                    data.context = $('<p/>').text('Загрузка...').replaceAll($(this));
                data.context = $('<p/>').text('').replaceAll($(this));
                data.submit();
            });
        },

        progressall: function (e, data) {
            var progress = parseInt((data.loaded / data.total * 100) * .25, 10);
            $('#progress .bar').css('width', progress + '%');
        }
    });

    $("#fileinput").on('change', function (e) {
        var f = this.files[0];
        this.setAttribute('text', f.name);
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


function startTaskStatusChecker(updateId) {
    var stop = false;
    (function worker() {
        $.ajax({
            url: '/admin/action/check-update-status',
            dataType: 'json',
            method: 'get',
            async: 'true',
            data: {'uid': updateId},

            success: function (data) {
                if (data.status == 'found') {
                    var taskStatus = data.taskStatus;
                    console.log('Got task-status: ' + taskStatus);

                    if (taskStatus == 'FILE_UPLOADED') {

                    } else if (taskStatus == 'SQL_LOAD_COMPLETE') {
                        $('#status-bar').html("<strong>Обновление загружено, идет пересчет статистики...</strong>");
                        $('#progress .bar').css('width', '75%');
                    } else if (taskStatus == 'UPDATE_STATISTICS_FINISHED') {
                        $('#status-bar').html("<strong>Загрузка окончена.</strong>");
                        stop = true;
                        $('#progress .bar').css('width', '100%');
                        window.location.href = '/admin/view/catalog-update?id=' + updateId;
                    }
                } else {
                    $('#status-bar').html("<strong>Задача " + updateId + " не найдена!</strong>");
                    stop = true;
                }

            },
            complete: function () {
                // Schedule the next request when the current one's complete
                if (!stop) {
                    setTimeout(worker, 2000);
                }
            },

            error: function (jqXHR, textStatus, errorThrown) {
                console.log(textStatus, errorThrown);
                stop = true;
            }
        });
    })();
}


// -------------------------------------------------------

function downloadCatalog() {
    $('#downloadBtn').remove();
    $('#loading-gif').css('visibility', 'visible');

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
            $("#file-link").append("<a href='" + data.path + "'>" +
                    "<i class='icon-download-alt'></i>" + "Скачать " +
                    $('#catName').val() + ".csv (" + Math.round(data.size / 1024 / 1024) + " Мб)" +
                    "</a>");
            $('#loading-gif').css('visibility', 'hidden');
        }
    });
}


</script>


<c:import url="footer.jsp"/>

</body>
</html>