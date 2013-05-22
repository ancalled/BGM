<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <script src="/js/jquery.js"></script>
    <script src="/js/bootstrap.js"></script>
    <script src="/js/bootstrap-fileupload.js"></script>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-fileupload.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/csv-preview.css" media="screen"/>

    <style>
        .dragtable-sortable {
            list-style-type: none; margin: 0; padding: 0; -moz-user-select: none;
        }
        .dragtable-sortable li {
            margin: 0; padding: 0; float: left; font-size: 1em; background: white;
        }

        .dragtable-sortable th, .dragtable-sortable td{
            border-left: 0px;
        }

        .dragtable-sortable li:first-child th, .dragtable-sortable li:first-child td {
            border-left: 1px solid #CCC;
        }

        .ui-sortable-helper {
            opacity: 0.7;filter: alpha(opacity=70);
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
            opacity: 0.0; visibility: hidden;
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
        <div class="span10">


            <section>
                <legend>
                    Каталог ${catalog.name}
                </legend>


                <dl class="dl-horizontal">
                    <dt>Тип прав</dt>
                    <dd>
                        <c:choose>
                            <c:when test="${catalog.copyright eq 'AUTHOR'}">
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

            <section>

                <legend>
                    Обновления
                </legend>

                <form action="../action/update-catalog" method="post" enctype="multipart/form-data">
                    <div class="fileupload fileupload-new" data-provides="fileupload">
                        <div class="input-append">
                            <div class="uneditable-input span3">
                                <i class="icon-file fileupload-exists"></i>
                                <span class="fileupload-preview"></span>
                            </div>

                <span class="btn btn-file">
                    <span class="fileupload-new">Выбрать обновление</span>
                    <span class="fileupload-exists">Изменить</span>
                    <input name="file" type="file" id="fileinput"/>
                </span>

                            <a href="#" class="btn fileupload-exists" data-dismiss="fileupload">Убрать</a>
                        </div>
                    </div>

                    <div class="row-fluid">
                        <input class="btn btn-primary" type="submit" value="Загрузить" id="sbmtBtn" disabled="disabled">
                    </div>

                    <input type="hidden" name="catId" value="${catalog.id}">
                </form>


                <div id="preview-container"></div>
            </section>

        </div>
    </div>

</div>


<script type="text/javascript" src="/js/csv-helper.js"></script>
<script type="text/javascript">

//    var rowsOnPreview = 10;
    var rowsOnPreview = 5;
    var fileData;
    var delimiter = ';';
    var headers = ['#', 'code', 'name', 'composer', 'artist', 'share mobile', 'share public'];


    $(document).ready(function () {

        $("#fileinput").on('change', function (e) {
            if (this.files && this.files[0]) {
                var reader = new FileReader();
                reader.onload = function (e) {
                    processContents(event.target.result, delimiter);
                };
                reader.readAsText(this.files[0], "UTF8");
            }
        });

    });


    function processContents(contents, delim) {
        fileData = contents;
        var data = convertCSVToArray(contents, delim);

        if (validateDate(data, headers)) {
            $('#sbmtBtn').removeAttr('disabled');
        }

        var tbl = buildTable(data, "preview-table", 'table table-striped', 0, rowsOnPreview, headers);

        var $preview = $("#preview-container");
        $preview.addClass('csv-preview');
        $preview.html(tbl);
        $preview.append("<div class='preview-more'>... и еще " + (data.length - rowsOnPreview) + " строк </div>")

    }

function validateDate(data, expectedCols) {
    return data && data[0] && data[0].length >= expectedCols.length
}


</script>

</body>
</html>