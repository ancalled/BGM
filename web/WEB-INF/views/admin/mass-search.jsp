<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html xmlns="http://www.w3.org/1999/html">

<head>

    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-fileupload.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-datetimepicker.min.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/jquery.handsontable.full.css" media="screen"/>
    <title>Поиск по файлу</title>
    <style>
        table#preview-table {
            font-size: 10pt;
        }

    </style>
</head>



<body>

<c:import url="navbar.jsp">
    <c:param name="massSearch" value="active"/>
</c:import>


<div class="container">
    <div class="row text-left">
        <legend>
            Поиск по файлу
        </legend>
    </div>

    <div class="row">

        <%--<form action="/admin/action/mass-search"--%>
              <%--method="post" enctype="multipart/form-data">--%>

            <div class="fileupload fileupload-new" data-provides="fileupload">
                <div class="input-append">
                    <div class="uneditable-input span3">
                        <i class="icon-file fileupload-exists"></i>
                        <span class="fileupload-preview"></span></div>
                                <span class="btn btn-fileName">
                                    <span class="fileupload-new">Выбрать отчет</span>
                                    <span class="fileupload-exists">Изменить</span>
                                    <input name="file" type="file" id="fileinput" accept=".csv"
                                           data-url="/admin/action/mass-search"/>
                                </span>
                    <a href="#" class="btn fileupload-exists" data-dismiss="fileupload">Удалить</a>
                </div>
            </div>


            <%--<div class="row-fluid">--%>
                <%--<button class="btn" id="submitBtn">Отправить</button>--%>
            <%--</div>--%>
        <%--</form>--%>


        <div id="preview-container" class="handsontable">
        </div>
    </div>

</div>

<script src="/js/bootstrap.js"></script>
<script src="/js/bootstrap-fileupload.js"></script>
<script src="/js/bootstrap-datetimepicker.min.js"></script>
<script src="/js/jquery.fileupload.js"></script>
<%--<script src="//ajax.googleapis.com/ajax/libs/jquery/1.9.1/jquery.min.js"></script>--%>
<script src="/js/handsontable/jquery.min.js"></script>

<script src="/js/csv-helper.js"></script>
<script src="/js/jquery.ui.widget.js"></script>
<%--<script src="/js/jquery.iframe-transport.js"></script>--%>
<script src="/js/jquery.fileupload.js"></script>
<script src="/js/jquery.handsontable.full.js"></script>
<script src="/js/jquery.fileupload.js"></script>
<script>
//    var delimiter = ';';
    var delimiter = ',';
//    var tbl_test = buildTable(testData, headers, 0, 1, "preview-table", 'table table-bordered preview');

    $(document).ready(function () {

        $('#fileinput').fileupload({
            dataType: 'json',

            done: function (e, data) {

//                $('#progress').html("");
//
//                if (data.result.status == 'ok') {
////                    console.log('Redirect url: ' + data.result.redirect)
//                    $('#status-bar').html("<strong>Загружено</strong>");
//                    window.location.href = data.result.redirect;
//
//                } else if (data.result.status == 'warn') {
////                    console.log('Got uplaod waringns: ');
//                    $.each(data.result.warningsList, function (index, wrn) {
//                        console.log(wrn);
//                    });
//
//                    $('#status-bar').html("<strong>Неверный формат данных в файле!</strong>");
//
//                    window.location.href = data.result.redirect;
//
//                } else if (data.result.status == 'error') {
//                    $('#status-bar').html("<strong>Ошибка!</strong>");
//
////                    console.log('Got uplaod error: ' + data.result.er);
//                }

            },

            add: function (e, data) {
//                $("#sbmtBtn").click(function () {
//                    $("#status").show();
//                    getLoadStatus();
//                    data.context = $('<p/>').text('Загрузка...').replaceAll($(this));
//                    data.submit();
//                });
            },

            progressall: function (e, data) {
//                var progress = parseInt(data.loaded / data.total * 100, 10);
//                $('#progress .bar').css('width', progress + '%');
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


    function processContents(contents, delim) {
        fileData = contents;
        var data = parseCSV(contents, delim);

        console.log('Got data of ' + data.length);

        $('#preview-container').handsontable({
            data: data,
            minSpareRows: 1,
            colHeaders: true,
            contextMenu: true
        });
    }

    function processContentsOld(contents, delim) {
        fileData = contents;
        var data = parseCSV(contents, delim);

        console.log('Got data of ' + data.length);

//        if (validateDate(data, headers)) {
//            $('#sbmtBtn').removeAttr('disabled');
//        }


        var size = 1000;
        var from = 1;
        var artistId = 1;
        var trackId = 2;

        var tbl = buildTable(data, data[0], from, size, "preview-table", 'table table-bordered preview');


//        $('#example').html('');

        var $preview = $("#preview-container");
        $preview.addClass('csv-preview');
        $preview.html(tbl);

//        if (data.length > rowsOnPreview) {
//            $preview.append("<div class='preview-more'>... и еще " + addCommas(data.length - rowsOnPreview) + " строк </div>")
//        }

        if ($preview.width() > $preview.width() || $('#preview-table').height() > $preview.height()) {
            $preview.css('overflow', 'scroll');
        }

        for (i = from; i < Math.min(size, data.length); i++) {
            var col = data[i];
            var track = col[trackId];
            var artist = col[artistId];

        }


    }

    function doSearchRequest(track, artist, rowEl) {
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