<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
	<!DOCTYPE html>
	<html>

	<style>
        .button-container {
            text-align: center;
        }

        #prevButton, #nextButton, #selectButton {
            width: 100px;
            height: 60px;
            margin-top: 10px;
            display: inline-block;
            transition: background-color 0.3s ease; /* Анімацію для зміни фонового кольору */
        }

        #selectButton {
            width: 150px;
            margin: 0 20px;
        }

        #prevButton:hover:not([disabled]), #nextButton:hover:not([disabled]), #selectButton:hover:not([disabled]) {
            background-color: #a020f0; /* Змінюємо фоновий колір при наведенні */
            color: #fff; /* Колір тексту при наведенні */
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

	<body>
	    <div class="header"></div>


	    <button type="button" onclick="window.location.href='/main'" class="form-button">На головну сторінку</button>
		<div class="button-container">
			<button id="prevButton">Назад</button>
			<button id="selectButton">Вибрати</button>
			<button id="nextButton">Вперед</button>
		</div>
	    <div style="text-align: center;">
	    <h2 id="recipeName"></h2>
	    <img id="recipeImage" style="max-width: 1200px; max-height: 400px; width: auto; height: auto; margin: 0 auto;" />
		</div>
		<div id="recipesData" data-recipes='${recipesIdsJson}' style="display: none;"></div>





    <script>

        const recipesData = document.getElementById('recipesData');
        const recipeIds = JSON.parse(recipesData.getAttribute('data-recipes'));
        let currentRecipeIndex = 0;

        // Отримуємо посилання на HTML-елементи
        const prevButton = document.getElementById('prevButton');
        const selectButton = document.getElementById('selectButton');
        const nextButton = document.getElementById('nextButton');
        const recipeName = document.getElementById('recipeName');
        const recipeImage = document.getElementById('recipeImage');

        // Функція для відображення поточного рецепту
        function displayCurrentRecipe() {

        	if (recipeIds.length === 0) {
        		recipeName.innerText = "Рецептів по таким параметрам не знайдено :(";
        		recipeImage.style.display = "none";
        		return;
        	}

        	const currentRecipeId = recipeIds[currentRecipeIndex];

        	// Отримання назви рецепту
        	fetch(`/data/recipe-name/` + currentRecipeId)
        		.then(response => response.text())
        		.then(recipeNameText => {
        			recipeName.innerText = recipeNameText;
        		})
        		.catch(error => {
        			console.error('Помилка отримання назви рецепту', error);
        		});

        	// Отримання зображення рецепту
        	const imageUrl = `/data/recipe-image-data/` + currentRecipeId;
        	recipeImage.onload = function() {
        		// Якщо зображення завантажилося успішно, то показуємо
        		recipeImage.style.display = "block";
        	};

        	recipeImage.onerror = function() {
        		// Якщо сталася помилка завантаження, приховуємо зображення
        		recipeImage.style.display = "none";
        	};

        	recipeImage.src = imageUrl;
        }



        // Обробники подій для кнопок "Назад", "Вибрати" та "Вперед"
        prevButton.disabled = true;
        if (recipeIds.length < 2){
            nextButton.disabled = true;
        }
        if (recipeIds.length == 0) {
            selectButton.disabled = true;
        }

        prevButton.addEventListener('click', () => {
        	if (currentRecipeIndex > 0) {
        		nextButton.disabled = false;
        		currentRecipeIndex--;
        		displayCurrentRecipe();
        	}
        	if (currentRecipeIndex === 0){
                prevButton.disabled = true;
            }
        });

        selectButton.addEventListener('click', () => {
            window.location.href = `/recipe/` + recipeIds[currentRecipeIndex];
        });

        nextButton.addEventListener('click', () => {
        	if (currentRecipeIndex < recipeIds.length - 1) {
        	    prevButton.disabled = false;
        		currentRecipeIndex++;
        		displayCurrentRecipe();
        	}
        	if (currentRecipeIndex === recipeIds.length - 1) {
        	    nextButton.disabled = true;
        	}
        });

        displayCurrentRecipe();





    </script>

</body>
</html>



