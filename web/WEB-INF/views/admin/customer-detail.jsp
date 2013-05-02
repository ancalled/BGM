<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<html>
<head>
    <title>Компания ${name}</title>
</head>
<body>


<div class="container">
    <ul>
        <li>
            ${details.address}
        </li>
        <li>
            ${details.rnn}
        </li>
        <li>
            ${details.boss}
        </li>
    </ul>
</div>


<div class="container">
    <table class="table">
        <thead>
        <tr>
            <th>#</th>
            <th>Имя</th>
            <th>Пароль</th>
        </tr>
        </thead>
        <tbody>


        <c:forEach var="u" items="${users}" varStatus="status">
            <tr>
                <td>${status.index}
                </td>
                <td>${u.login}
                </td>
                <td>${u.pass}
                </td>
            </tr>
        </c:forEach>

        </tbody>
    </table>


</div>


</body>
</html>