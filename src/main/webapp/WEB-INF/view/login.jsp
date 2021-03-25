<!DOCTYPE html>
<html lang="en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<head>
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <title>Login Page</title>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css">
    <link rel="stylesheet" href="./static/style.css">
</head>
<body>
    <c:choose>
        <c:when test="${sessionScope.loggedUser != null}">
            <div class="login-form">
                <form action="/logout" method="GET">
                    <h2 class="text-center">Log in</h2>
                    <div class="form-group">
                        <h5>You are already logged in as <span class="font-weight-bold">${sessionScope.user.login}</span>, you need to log out before logging in as different user</h5>
                    </div>
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary btn-block">Log out</button>
                    </div>
                </form>
            </div>
        </c:when>
        <c:otherwise>
            <div class="login-form">
                <form action="${pageContext.request.contextPath}/login" method="post">
                    <h2 class="text-center">Log in</h2>
                    <div class="form-group">
                        <input name="login" value="${login}" type="text" class="form-control" placeholder="Login" required="required">
                    </div>
                    <div class="form-group">
                        <input name="password" type="password" class="form-control" placeholder="Password" required="required">
                    </div>
                    <div class="form-group">
                        <div class="text-danger">${error}</div>
                    </div>
                    <div class="form-group">
                        <button type="submit" class="btn btn-primary btn-block">Log in</button>
                    </div>
                </form>
            </div>
        </c:otherwise>
    </c:choose>
</body>

</html>