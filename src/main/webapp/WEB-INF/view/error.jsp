<!DOCTYPE html>
<html lang="en">
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, shrink-to-fit=no">
    <link href="https://fonts.googleapis.com/css?family=Raleway:300,400,600" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="https://maxcdn.bootstrapcdn.com/bootstrap/4.0.0/css/bootstrap.min.css">
    <title>Error</title>
</head>
<body>
<main>
    <br>
    <div style="min-height: 80vh" class="d-flex flex-row align-items-center">
        <div class="container">
            <div class="row justify-content-center">
                <div class="col-md-12 text-center">
                    <span class="display-4 d-block">Error</span>
                    <div class="mb-4 lead">Error code is ${pageContext.errorData.statusCode}</div>
                    <a href="/" class="button">Home Page</a>
                </div>
            </div>
        </div>
    </div>
</main>
</body>
</html>