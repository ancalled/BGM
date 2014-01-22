<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html xmlns="http://www.w3.org/1999/html">

<head>
    <script src="/js/jquery.js"></script>
    <script src="/js/bootstrap.js"></script>
    <script src="/js/bootstrap-tab.js"></script>

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


<div class="container">

    <ul id="tabs-nav" class="nav nav-tabs">
        <li class="active">
            <a href="#incoming" data-toggle="tab">Входящие</a>
        </li>
        <li>
            <a href="#outgoing" data-toggle="tab">Исходящие</a>
        </li>

    </ul>

    <div class="tab-content">
        <div class="tab-pane active" id="incoming">
            <c:import url="reports-incoming.jsp"/>
        </div>
        <div class="tab-pane" id="outgoing">
            <c:import url="reports-outgoing.jsp"/>
        </div>
    </div>

</div>



<c:import url="footer.jsp"/>

<script>
    $(document).ready(function () {

        $('#tabs-nav a').click(function (e) {
            e.preventDefault();
            $(this).tab('show');
        });

    });


</script>

</body>
</html>