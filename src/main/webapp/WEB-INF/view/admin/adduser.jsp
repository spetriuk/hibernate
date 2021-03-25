<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link href="https://fonts.googleapis.com/css?family=Raleway:300,400,600" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="../static/style.css">
    <title>Add User</title>
</head>
<body>
    <div class="container-fluid">
        <%@include file="../jspf/navbar.jspf"%>
        <div class="row justify-content-center">
            <h2 class="text-center">Add User</h2>
        </div>
        <div class="pt-5 row justify-content-center">
            <form class="col-12 col-md-8" action="/admin/adduser" method="POST">
                <%@include file="../jspf/userformcontent.jspf"%>
            </form>
        </div>
    </div>
</body>
</html>