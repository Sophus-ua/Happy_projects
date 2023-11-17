<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Login Page</title>

    <style>

        .main-container {
            display: flex;
            flex-direction: column;
            align-items: center;
            justify-content: center;
            height: 100vh;
            margin: 0;
            font-family: Arial, sans-serif;
        }

        .form-container {
            width: 300px;
            padding: 20px;
            background-color: #f5f5f5;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
            margin-bottom: 20px;
        }

        label {
            display: block;
            margin-bottom: 8px;
        }

        input {
            width: 100%;
            padding: 8px;
            margin-bottom: 16px;
            box-sizing: border-box;
        }

        .form-button {
            display: inline-block;
            padding: 10px 20px;
            font-size: 16px;
            text-align: center;
            text-decoration: none;
            cursor: pointer;
            border: none;
            border-radius: 5px;
            transition: background-color 0.3s, color 0.3s;
        }

        .form-button:hover {
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

    <div style="text-align: center;">
        <c:if test="${not empty error}">
            <p style="font-weight: bold; font-size: 30px; color: #8B0000;">${error}</p>
        </c:if>
    </div>

    <div style="text-align: center;">
        <c:if test="${not empty message}">
            <p style="font-weight: bold; font-size: 30px; color: #008000;">${message}</p>
        </c:if>
    </div>


    <div class="main-container">
      <div class="form-container">
          <h2>Login</h2>
          <form action="/user/login" method="post">
              <label for="username">Username:</label>
              <input type="text" id="username" name="username" required>

              <label for="password">Password:</label>
              <input type="password" id="password" name="password" required>

              <input type="submit" class="form-button" value="Login">

              <div style="display: flex; ">
                 <label for="remember_me">remember_me</label>
                 <input id="remember_me" name="_spring_security_remember_me" type="checkbox"/>
              </div>
          </form>
      </div>

      <a href="/user/login" class="form-button">remind password</a>
      <br>
      <a href="/user/registration" class="form-button">Registration page</a>





    </div>


</body>
</html>



