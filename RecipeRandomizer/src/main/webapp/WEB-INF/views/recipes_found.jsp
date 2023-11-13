<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		<html>

		<head>
			<meta charset="UTF-8">
			<title>Recipes found</title>

			<style>
			   .form-button {
                   height: 40px;
                   transition: background-color 0.3s, color 0.3s; /* Анімація для плавного змінення коліру фону і тексту */
               }

               .form-button:hover {
                   background-color: #a020f0; /* Фіолетовий колір при наведенні */
                   color: #fff; /* Колір тексту при наведенні */
               }
			</style>

		</head>

		<body>
			<button type="button" onclick="window.location.href='/main'" class="form-button">На головну сторінку</button>
			<h3 style="text-align: center;"> Список знайдених Рецептів </h3>
			<table>
				<tr>
					<th style="width: 220px; border: 1px solid gray;"> Назва </th>
					<th style="width: 150px; border: 1px solid gray;"> Категорія їжі </th>
					<th style="width: 170px; border: 1px solid gray;"> Регіональна кухня </th>
					<th style="width: 70px; border: 1px solid gray;"> Час(хв) </th>
					<th style="width: 70px; border: 1px solid gray;"> Порцій </th>
					<th style="width: 70px; border: 1px solid gray;"> Калорії </th>
					<th style="width: 100px; border: 1px solid gray;"></th>
				</tr>
				<c:forEach items="${recipes}" var="recipe">
					<tr>
						<td>${recipe.name}</td>
						<td>${recipe.mealCategory.name}</td>
						<td>${recipe.regionalCuisine.name}</td>
						<td>${recipe.cookingTimeMin}</td>
						<td>${recipe.portions}</td>
						<td>${recipe.calories}</td>
						<td><a href="<c:url value='/recipe/${recipe.id}' />">Вибрати</a></td>
					</tr>
				</c:forEach>
			</table>
		</body>

		</html>


