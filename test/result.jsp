<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <script src="../web/js/jquery.js"></script>
    <script src="../web/js/bootstrap.js"></script>
    <script src="../web/js/bootstrap-fileupload.js"></script>
    <meta http-equiv="Content-Type" content="text/html;charset=utf-8">
    <link rel="stylesheet" type="text/css" href="../web/css/bootstrap.css" media="screen"/>
    <link rel="stylesheet" type="text/css" href="../web/css/bootstrap-fileupload.css" media="screen"/>
    <title>Реультат</title>
</head>
<legend>
    Резултат
</legend>

<c:choose>
    <c:when test="${param.er=='no-customer-id-provided'}">
        <c:set var="message" value="Не найден id провайдера" scope="page"/>
    </c:when>
    <c:when test="${param.er=='no-customer-found'}">
        <c:set var="message" value="Не найден провайдер" scope="page"/>
    </c:when>
    <c:when test="${param.er=='no-file-reports-uploaded'}">
        <c:set var="message" value="Нет загруженных файлов отчетов " scope="page"/>
    </c:when>
    <c:otherwise>
        <c:set var="message" value="${param.er}" scope="page"/>
    </c:otherwise>
</c:choose>

<div class="container span6">
    <blockquote>
        <p><c:out value="${message}"/></p>
    </blockquote>
</div>


<body>

</body>
</html>