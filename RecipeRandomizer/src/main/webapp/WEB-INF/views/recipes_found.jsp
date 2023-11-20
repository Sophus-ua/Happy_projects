<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
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
					<th style="width: 250px; border: 1px solid gray;"> (натисніть для переходу)</th>
					<th style="width: 250px; border: 1px solid gray;"> Назва </th>
					<th style="width: 150px; border: 1px solid gray;"> Категорія їжі </th>
					<th style="width: 170px; border: 1px solid gray;"> Регіональна кухня </th>
					<th style="width: 70px; border: 1px solid gray;"> Час(хв) </th>
					<th style="width: 60px; border: 1px solid gray;"> Порцій </th>
					<th style="width: 80px; border: 1px solid gray;"> Калорії </th>
					<sec:authorize access="hasRole('ROLE_USER') and ${recipesUsername == 'Moderator'}">
					    <th style="width: 100px; border: 1px solid gray;">Додати до своїх</th>
				    </sec:authorize>
				</tr>
				<c:forEach items="${recipes}" var="recipe">
					<tr>
					    <td>
                            <div id="recipeCell${recipe.id}" style="min-height: 50px; display: flex; align-items: center;">
                                <img id="recipeImage${recipe.id}" onclick="redirectToRecipe('${recipe.id}')"
                                        style="max-width: 300px; max-height: 130px; width: auto; height: auto; margin: 0 auto;" />
                            </div>

                            <script>
                                const recipeCell${recipe.id} = document.getElementById('recipeCell${recipe.id}');
                                const recipeImage${recipe.id} = document.getElementById('recipeImage${recipe.id}');
                                const imageUrl${recipe.id} = `/data/recipe-image-data/${recipe.id}`;

                                recipeImage${recipe.id}.onload = function() {
                                    recipeImage${recipe.id}.style.display = "block";
                                };

                                recipeImage${recipe.id}.onerror = function() {
                                    recipeCell${recipe.id}.innerHTML = `<a href="/recipe/${recipe.id}">Вибрати</a>`;
                                };

                                recipeImage${recipe.id}.src = imageUrl${recipe.id};

                                function redirectToRecipe(recipeId) {
                                    window.location.href = `/recipe/`+recipeId;
                                }

                            </script>

                        </td>
                        <td>${recipe.name}</td>
						<td>${recipe.mealCategory.name}</td>
						<td>${recipe.regionalCuisine.name}</td>
						<td>${recipe.cookingTimeMin}</td>
						<td>${recipe.portions}</td>
						<td>${recipe.calories}</td>
						<sec:authorize access="hasRole('ROLE_USER') and ${recipesUsername == 'Moderator'}">
                          <td>
                              <div id="buttonContainer_${recipe.id}"></div>

                              <script>
                                  document.addEventListener("DOMContentLoaded", function () {
                                      const recipeId = '${recipe.id}';
                                      const buttonContainer = document.getElementById('buttonContainer_' + recipeId);

                                      fetch(`/data/has-this-common-recipe`, {
                                          method: 'POST',
                                          headers: {
                                              'Content-Type': 'application/json',
                                          },
                                          body: JSON.stringify({ recipeId: recipeId }),
                                      })
                                          .then(response => response.json())
                                          .then(hasCommonRecipe => {
                                              if (!hasCommonRecipe) {
                                                  // Якщо рецепта немає у користувача, створити кнопку
                                                  const button = document.createElement('button');
                                                  button.textContent = 'Додати';
                                                  button.onclick = function () {
                                                      addRecipeToMine(recipeId, button);
                                                  };
                                                  buttonContainer.appendChild(button);
                                              } else {
                                                  // Якщо рецепт вже є у користувача, деактивувати кнопку
                                                  buttonContainer.innerHTML = '';
                                              }
                                          })
                                          .catch(error => {
                                              console.error('Помилка при перевірці наявності спільного рецепту', error);
                                          });
                                  });

                                  function addRecipeToMine(recipeId, button) {
                                      button.disabled = true;

                                      fetch('/data/add-recipe-to-mine', {
                                          method: 'POST',
                                          headers: {
                                              'Content-Type': 'application/json',
                                          },
                                          body: JSON.stringify({ recipeId: recipeId }),
                                      })
                                          .then(response => {
                                              if (response.ok) {
                                                  console.log('Рецепт успішно додано до своїх');
                                                   button.textContent = 'Додано';
                                              } else {
                                                  console.error('Помилка при додаванні рецепту до своїх');
                                              }
                                          })
                                          .catch(error => {
                                              console.error('Помилка при виконанні AJAX-запиту', error);
                                          });
                                  }

                              </script>
                          </td>
                        </sec:authorize>
					</tr>
				</c:forEach>
			</table>

</body>
</html>


