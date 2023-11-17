<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Рецептарій</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #f4f4f4;
            margin: 0;
            padding: 0;
        }

        header {
            background-color: #4B0082; /* Темнофіолетовий колір */
            color: #fff;
            text-align: center;
            padding: 10px;
        }

        nav {
            background-color: #eee;
            padding: 10px;
            text-align: center;
        }

        nav ul {
            list-style: none;
            padding: 0;
            margin: 0;
        }

        nav ul li {
            display: inline-block;
            margin-right: 20px;
        }

        nav ul li button {
            height: 40px;
            margin-right: 15px;
            transition: background-color 0.3s, color 0.3s;
            cursor: pointer;
            border: none;
            border-radius: 5px;
            background-color: #eee;
            color: #333;
        }

        nav ul li button:hover {
            background-color: #a020f0;
            color: #fff;
        }

        section {
            margin: 20px;
            text-align: center;
        }

        footer {
            background-color: #333;
            color: #fff;
            text-align: center;
            padding: 10px;
            position: fixed;
            bottom: 0;
            width: 100%;
        }

        form {
            text-align: center;
        }

        .form-button {
            height: 40px;
            margin-right: 15px;
            transition: background-color 0.3s, color 0.3s;
            cursor: pointer;
            border: none;
            border-radius: 5px;
            background-color: #eee;
            color: #333;
        }

        .form-button:last-child {
            margin-right: 20px;
            position: relative;
            right: unset;
        }

        .form-button:hover {
            background-color: #a020f0;
            color: #fff;
        }

        /* Стилі для роздільної лінії */
        .divider {
            border-top: 1px solid #ccc;
            margin-top: 20px;
        }



    </style>
</head>
<body>

    <header>
        <h1>Вітання у Рецептарії!</h1>
    </header>


        <!-- Якщо користувач НЕ авторизований -->
        <sec:authorize access="isAnonymous()">
            <form action="${pageContext.request.contextPath}/user/login" method="get">
                <button type="submit" class="form-button" style="font-size: 1.5em;">Увійти</button>
            </form>
            <form action="${pageContext.request.contextPath}/user/registration" method="get">
                <button type="submit" class="form-button" style="font-size: 1.5em;">Зареєструватися</button>
            </form>
        </sec:authorize>

        <!-- Якщо користувач авторизований -->

        <sec:authorize access="hasRole('ROLE_USER')">
            <form action="${pageContext.request.contextPath}/main" method="get">
                <button type="submit" class="form-button" style="font-size: 1.5em;">Перехід в застосунок</button>
            </form>
        </sec:authorize>

        <sec:authorize access="hasRole('ROLE_ADMIN')">
            <form action="${pageContext.request.contextPath}/admin/main" method="get">
                <button type="submit" class="form-button" style="font-size: 1.5em;">Перехід в застосунок</button>
            </form>
        </sec:authorize>

        <sec:authorize access="hasRole('ROLE_MODERATOR')">
            <form action="${pageContext.request.contextPath}/moderator/main" method="get">
                <button type="submit" class="form-button" style="font-size: 1.5em;">Перехід в застосунок</button>
            </form>
        </sec:authorize>

        <sec:authorize access="isAuthenticated()">
            <form action="${pageContext.request.contextPath}/user/logout" method="post">
                <button type="submit" class="form-button" style="font-size: 1.5em;">Logout</button>
            </form>
        </sec:authorize>

    <div class="divider"></div>

    <section>
        <h2>Опис застосунку</h2>
        <p>Рецептарій - це веб-застосунок, створений для зручного зберігання та рандомізації рецептів. Ви можете легко створювати, редагувати та ділитися своїми улюбленими рецептами.</p>
        <p>Застосунок дозволяє вам:</p>
        <ul>
            <li>Додавати та зберігати рецепти.</li>
            <li>Редагувати існуючі рецепти.</li>
            <li>Швидко знаходити рецепти за різними категоріями.</li>
            <li>Випадковим чином обирати рецепт для приготування, якщо у вас складності з вибором блюда.</li>
        </ul>
    </section>

    <footer>
        <p>&copy; 2023 Рецептарій. Усі права захищені.</p>
    </footer>

</body>
</html>


