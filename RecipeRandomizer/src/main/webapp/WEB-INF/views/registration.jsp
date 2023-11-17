<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Registration Page</title>

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
            display: flex;
            flex-direction: column;
            align-items: center;
            width: 300px;
            padding: 20px;
            background-color: #f5f5f5;
            border-radius: 8px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
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

        .button-container {
            display: flex;
            flex-direction: column;
            align-items: center;
            margin-top: 20px; /* Додали відступ перед кнопкою "Login" */
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


    <div class="main-container">
      <div class="form-container">
          <h2>Registration</h2>
          <form action="/user/registration" method="post">
              <label for="username">Login:</label>
              <input type="text" id="username" name="username" required>

              <label for="password">Password:</label>
              <input type="password" id="password" name="password" required>

              <label for="confirmPassword">Confirm Password:</label>
              <input type="password" id="confirmPassword" name="confirmPassword" required>

              <label for="ownName">Your name:</label>
              <input type="text" id="ownName" name="ownName" required>

              <input type="submit" class="form-button" value="Register">
          </form>
      </div>

      <div class="button-container">
          <a href="/user/login" class="form-button">Login page</a>
      </div>
    </div>

</body>
</html>





