<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Сторінка адміна</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }

        .container {
            max-width: 800px;
            margin: 0 auto;
            padding: 20px;
        }

        .header {
            background-color: #333;
            color: #fff;
            padding: 10px;
            text-align: center;
        }

        .logout-button {
            height: 40px;
            margin-right: 15px;
            transition: background-color 0.3s, color 0.3s;
        }

        .logout-button:last-child {
            margin-right: 20px;
            position: absolute;
            right: 0;
        }

        .logout-button:hover {
            background-color: #a020f0;
            color: #fff;
        }
    </style>
</head>
<body>

<sec:authorize access="isAuthenticated()">
    <div style="text-align: right; margin-right: 20px; margin-top: 10px;">
        <form action="${pageContext.request.contextPath}/user/logout" method="post">
            <button type="submit" class="form-button logout-button">Logout</button>
        </form>
    </div>
</sec:authorize>

<div class="header">
    <h1>Сторінка адміна</h1>
</div>

<div class="container">

</div>

</body>
</html>
