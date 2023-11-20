<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
		<html>

		<head>
			<meta charset="UTF-8">
			<title>Recipe</title>
			<style>

			.centered-title {
				text-align: center;
			}

			.button-container {
                display: flex;
                justify-content: flex-start; /* Залишити кнопки від лівого краю */
                gap: 10px; /* Встановіть відступ між кнопками за бажанням */
            }
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

		         <sec:authorize access="hasRole('ROLE_USER') and ${recipeDTO.username == 'Moderator'}">
		           <h2>Загальний рецепт</h2>
                 </sec:authorize>
            </div>



            <div class="button-container">
                <button type="button" onclick="window.location.href='/main'" class="form-button">На головну сторінку</button>

                <sec:authorize access="!(hasRole('ROLE_USER') and ${recipeDTO.username == 'Moderator'})">
                    <form id="updateRecipe" action="/recipe-handler" method="post">
                        <input type="hidden" name="recipeID" value="${recipeDTO.id}">
                        <button type="submit" class="form-button">Редагувати рецепт</button>
                    </form>
                </sec:authorize>

                <sec:authorize access="!(hasRole('ROLE_USER') and ${recipeDTO.username == 'Moderator'})">
                    <form id="deleteRecipe" action="/delete-recipe" method="post">
                        <button type="button" onclick="showConfirmation()" class="form-button">Видалити Рецепт</button>
                        <input type="hidden" name="recipeID" value="${recipeDTO.id}">
                        <input type="submit" id="hiddenDeleteButton" style="display: none;">
                    </form>
                </sec:authorize>

                <sec:authorize access="hasRole('ROLE_USER') and ${recipeDTO.username == 'Moderator'}">
                    <form id="updateRecipe" action="/add-recipe-to-mine" method="post">
                        <input type="hidden" name="recipeID" value="${recipeDTO.id}">
                        <button type="submit" class="form-button">Додати рецепт до своїх</button>
                    </form>
                </sec:authorize>
            </div>



            <div style="text-align: center; margin-top: 20px;">
            <img id="recipeImage" style="max-width: 900px; max-height: 300px; width: auto; height: auto; margin: 0 auto;" /> </div>
			<h1 class="centered-title"> ${recipeDTO.name} </h1>
			<p><b>Категорія:</b> ${recipeDTO.mealCategoryName}</p>
			<p><b>Регіональна кухня:</b> ${recipeDTO.regionalCuisineName}</p>
			<p><b>Час приготування хв:</b> ${recipeDTO.cookingTimeMin != 0 ? recipeDTO.cookingTimeMin : ''} </p>
			<p><b>Порцій:</b> ${recipeDTO.portions != 0 ? recipeDTO.portions : ''}</p>
			<p><b>Калорії:</b> ${recipeDTO.calories != 0 ? recipeDTO.calories : ''}</p>
			<p><b>Інгредієнти:</b>
				<c:choose>
					<c:when test="${not empty recipeDTO.dishesByIngredientsNames}">
						<c:forEach items="${recipeDTO.dishesByIngredientsNames}" var="name" varStatus="status"> ${name}
							<c:if test="${not status.last}">, </c:if>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<p></p>
					</c:otherwise>
				</c:choose>
			</p>
			<p><b>Алергени:</b>
				<c:choose>
					<c:when test="${not empty recipeDTO.allergensNames}">
						<c:forEach items="${recipeDTO.allergensNames}" var="name" varStatus="status"> ${name}
							<c:if test="${not status.last}">, </c:if>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<p></p>
					</c:otherwise>
				</c:choose>
			</p>
			<p><b>Мітки:</b>
				<c:choose>
					<c:when test="${not empty recipeDTO.customTagsNames}">
						<c:forEach items="${recipeDTO.customTagsNames}" var="name" varStatus="status"> ${name}
							<c:if test="${not status.last}">, </c:if>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<p></p>
					</c:otherwise>
				</c:choose>
			</p>
			<h3 class="centered-title" style="margin-top: 10px;">Рецепт приготування</h3>
			<c:if test="${not empty recipeDTO.recipeText}">
				<c:forEach items="${recipeDTO.recipeText}" var="line">
					<p>${line}</p>
				</c:forEach>
			</c:if>
            <div id="recipeID" data-recipes='${recipeDTO.id}' style="display: none;"></div>



    <script>


      const recipeIDElement = document.getElementById('recipeID');
      const recipeID = parseInt(recipeIDElement.getAttribute('data-recipes'), 10);
      const recipeImage = document.getElementById('recipeImage');
      const imageUrl = `/data/recipe-image-data/` + recipeID;

      recipeImage.onload = function() {
      	// Якщо зображення завантажилося успішно, то показуємо
      	recipeImage.style.display = "block";
      };

      recipeImage.onerror = function() {
      	// Якщо сталася помилка завантаження, приховуємо зображення
      	recipeImage.style.display = "none";
      };

      recipeImage.src = imageUrl;



      function showConfirmation() {
          var result = window.confirm("Ви впевнені, що хочете видалити цей рецепт?");
          if (result) {
              document.getElementById('hiddenDeleteButton').click();
          } else {
              alert("Видалення рецепту відмінено !");
          }
      }

    </script>


</body>
</html>