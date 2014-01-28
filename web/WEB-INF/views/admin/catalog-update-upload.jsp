<%--
  Created by IntelliJ IDEA.
  User: ancalled
  Date: 1/21/14
  Time: 11:51 AM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<link rel="stylesheet" type="text/css" href="/css/jquery.modal.css" media="screen"/>
<link rel="stylesheet" type="text/css" href="/css/csv-preview.css" media="screen"/>

<style>
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

    table.preview {
        font-size: 10px;
        margin: 5px 20px 5px 5px;
    }

    #modal-container.modal {
        width: 60%;
        height: 80%;
        /*margin-left: 10%;*/
        /*margin-top: 10%;*/
    }

    #preview-container {
        overflow: scroll;
    }

    .upload-btn-container {
        text-align: center;
    }

</style>


<section>

    <%--<legend>Обновления</legend>--%>

    <div id="modal-container" class="modal">
        <p>Файл обновления должен быть в формате <strong><i>csv</i></strong> и содержать поля, как указано в таблице
            ниже.
            Разделитель '<strong>;</strong>', кодировка <strong>utf-8</strong>
        </p>

        <form class="form-horizontal" action="/admin/action/update-catalog" method="post"
              enctype="multipart/form-data">

            <div class="form-group">
                <div class="fileupload fileupload-new" data-provides="fileupload">
                    <div class="inline">

                        <div class="input-group-btn">
                            <a class="btn btn-primary btn-file">
                                <span class="fileupload-new">Загрузить обновление каталога</span>
                                <%--<span class="fileupload-exists">Изменить</span>--%>
                                <input type="file" name="file" class="file-input" id="fileinput" accept=".csv"
                                       data-url="/admin/action/update-catalog"/></a>
                            <a href="#" class="btn btn-default fileupload-exists"
                               data-dismiss="fileupload">Убрать</a>
                        </div>

                    </div>
                </div>
            </div>


            <div class="row-fluid">

                <div id="progress" class="progress progress-success progress-striped">
                    <div class="bar" style="width: 0%;"></div>
                </div>

                <div id="status-bar"></div>
            </div>

        </form>


        <div style="overflow: hidden; height: inherit">
            <div id="preview-container">
            </div>
        </div>

        <div class="upload-btn-container">
            <button class="btn btn-primary"
                    id="sbmtBtn">Загрузить
            </button>
        </div>

    </div>

    <input type="hidden" id="catId" name="catId" value="${catalog.id}">
    <input type="hidden" id="catName" name="catalog_name" value="${catalog.name}">

</section>


<script src="/js/bootstrap.js"></script>
<script src="/js/bootstrap-fileupload.js"></script>
<script src="/js/csv-helper.js"></script>
<script src="/js/jquery.ui.widget.js"></script>
<script src="/js/jquery.iframe-transport.js"></script>
<script src="/js/jquery.fileupload.js"></script>
<script src="/js/jquery.modal.min.js"></script>

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

        $('#modal-container').hide();
        $('#preview-container').hide();
        $('#progress').hide();

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
                    $('#modal-container').close();

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
                    $('#preview-container').show();
                    $('#progress').show();
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

        var $preview = $("#preview-container");
        $preview.addClass('csv-preview');
        $preview.html(tbl);

//        if (data.length > rowsOnPreview) {
//            $preview.append("<div class='preview-more'>... и еще " + addCommas(data.length - rowsOnPreview) + " строк </div>")
//        }

        if ($('#preview-table').width() > $preview.width() || $('#preview-table').height() > $preview.height()) {
            $preview.css('overflow', 'scroll');
        }

        $('#modal-container').modal();

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


</script>
