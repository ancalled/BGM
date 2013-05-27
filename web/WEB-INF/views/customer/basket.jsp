<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<html>
<head>
    <script src="/js/jquery.js"></script>
    <script src="/js/bootstrap.js"></script>
    <script src="/js/bootstrap-fileupload.js"></script>
    <script src="/js/bootstrap-datetimepicker.min.js"></script>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="/css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-fileupload.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-datetimepicker.min.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="/css/bootstrap-responsive.css" media="screen"/>
    <title>Выбранные композиции</title>
</head>

<c:import url="navbar.jsp">
    <c:param name="basket" value="active"/>
</c:import>


<body>

<div class="container">
    <legend>
        Выбранные треки
    </legend>
    <div class="row span4">
        <ul align="right" style="list-style-type: none">
            <c:forEach var="t" items="${basket.tracks}">
                <li>
                        ${t.name} <i class="icon-remove-circle"></i>
                </li>
            </c:forEach>
        </ul>
    </div>
</div>


<script>
    var delButtons = document.getElementsByTagName('i');
       for (var i = 0; i < delButtons.length; ++i) {
           var but = delButtons[i];
           $(but).mouseenter(function () {
               $(this).css('opacity', '0.3');
           });
           $(but).mouseout(function () {
               $(this).css('opacity', '1');
           });
           $(but).click(function () {
//               $('#customer').val(this.id);
//               $('#remove-btn').click(function () {
//                  submitRemover();
//               });
//               $('#myModal').modal('show');
//
           });
       }

</script>

</body>
</html>