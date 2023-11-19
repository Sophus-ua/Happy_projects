<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
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

               .header {
                   background-color: #4B0082; /* Темнофіолетовий колір */
                   color: #fff;
                   padding: 10px;
                   text-align: center;
                   margin-bottom: 20px;
               }



			</style>

		</head>

		<body>
		    <div class="header">
                <sec:authorize access="hasRole('ROLE_MODERATOR')">
                    <h1>Сторінка модератора</h1>
                </sec:authorize>
                <div style="text-align: center;">
                    <c:if test="${not empty message}">
                        <p style="font-weight: bold; font-size: 30px; ">${message}</p>
                    </c:if>
                </div>
            </div>



			<button type="button" onclick="window.location.href='/main'" class="form-button">На головну сторінку</button>
			<h3 style="text-align: center;"> Список знайдених Рецептів </h3>

			<!-- Пагінація -->
            <div style="text-align: center; margin-top: 20px;">
                <c:if test="${currentPage > 1}">
                    <a href="<c:url value='/recipes-found?page=1' />">  << </a>
                    <span style="margin-left: 15px;"></span>
                    <a href="<c:url value='/recipes-found?page=${currentPage - 1}' />"><</a>
                    <span style="margin-right: 10px;"></span>
                </c:if>

                <c:forEach begin="1" end="${totalPages}" var="pageNumber">
                    <c:choose>
                        <c:when test="${pageNumber eq currentPage}">
                            ${pageNumber}
                        </c:when>
                        <c:otherwise>
                            <a href="<c:url value='/recipes-found?page=${pageNumber}' />">${pageNumber}</a>
                        </c:otherwise>
                    </c:choose>
                </c:forEach>

                <c:if test="${currentPage < totalPages}">
                    <span style="margin-left: 10px;"></span>
                    <a href="<c:url value='/recipes-found?page=${currentPage + 1}' />">> </a>
                    <span style="margin-left: 15px;"></span>
                    <a href="<c:url value='/recipes-found?page=${totalPages}' />">>> </a>
                </c:if>
            </div>


            <table>
				<tr>
					<th style="width: 270px; border: 1px solid gray;"> Назва </th>
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


