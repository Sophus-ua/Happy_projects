<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Main</title>

    <style>
        /* Стилі для кнопок */
        .button {
            margin-top: 15px;
            margin-right: 15px;
            padding: 10px;
        }

        /* Стилі для форм */
        .form {
            margin-bottom: 10px;
            display: flex;
            align-items: center;
            padding: 10px;
        }

        /* Стилі для поля вводу в формі recipesNameLike */
        .recipesNameLike input[type="text"] {
            width: 400px; /* Збільшити ширину в два рази */
            height: 30px;
        }

        .form.recipesNameLike input[type="submit"] {
            margin-left: 10px; /* Відступ зліва для кнопки submit */
             width: 170px;
             height: 50px;
        }

        /* Стилі для форми searchForm */
        .searchForm {
        display: grid;
        grid-template-columns: 1fr 1fr 1fr; /* Розділення на дві колони */
        gap: 10px; /* Відстань між елементами */
        }


        /* Стилі для випадаючих списків в формі searchForm */
        .searchForm select {
            width: 200px; /* Змінити ширину випадаючого списку */
        }

        /* Стилі для роздільної лінії */
        .divider {
            border-top: 10px solid #ccc;
            margin-top: 20px;
        }

        /* Стилі для міток, полів вводу та випадаючих списків у формах */
        .form label, .form input, .form select {
            margin-bottom: 10px;
        }

        .centered-title {
            text-align: center; /* Центруємо по горизонталі */
        }

        input.search.submit[type="submit"] {
            height: 40px;
        }
        /* Стилі для "Швидкий пошук по назві" форми */
        .recipesNameLike input[type="submit"]:hover {
            background-color: #a020f0; /* Світлофіолетовий колір при наведенні */
            color: #fff; /* Колір тексту при наведенні */
        }

        /* Стилі для "Звичайний пошук" і "Рандомайзер рецептів" кнопок */
        .search.submit:hover {
            background-color: #a020f0; /* Світлофіолетовий колір при наведенні */
            color: #fff; /* Колір тексту при наведенні */
        }

        /* Стилі для кнопок */
        .button-container {
            display: flex;
        }

        .form-button {
            height: 40px;
            margin-right: 15px; /* Відступ зправа між кнопками */
            transition: background-color 0.3s, color 0.3s; /* Анімація для плавного змінення коліру фону і тексту */
        }

        .form-button:last-child {
            margin-right: 20px;
            position: absolute;
            right: 0;
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
    </div>

    <div class="button-container">
            <button type="button" onclick="window.location.href='/recipe-handler'" class="form-button">Додати новий рецепт</button>
        <sec:authorize access="hasRole('ROLE_USER')">
             <button type="button" onclick="window.location.href='/tag-handler'" class="form-button">Управління ТЕГами</button>
        </sec:authorize>
            <form action="/user/logout" method="post">
                <input type="submit" value="Logout" class="form-button"/>
            </form>
    </div>

    <div style="text-align: center;">
        <c:if test="${not empty message}">
            <p style="font-weight: bold; font-size: 30px; color: #008000;">${message}</p>
        </c:if>
    </div>

    <div class="divider"></div>

    <form action="/search-recipes-name-like" method="get" class="form recipesNameLike">
        <label for="recipesNameLike"><h3> Швидкий пошук по назві: </h3></label>
        <input type="text" id="recipesNameLike" name="recipesNameLike">
        <input type="submit" value="Свої рецепти" >
        <sec:authorize access="hasRole('ROLE_USER')">
            <input type="submit" value="Загальні рецепти" formaction="/common/search-recipes-name-like" >
        </sec:authorize>
    </form>

        <div class="divider"></div>





    <form action="/search-recipes-by-form" method="get" class="form searchForm" id="searchForm">

        <input type="submit" value="12 рандомних Своїх рецептів" class="search submit" formaction="/recipe-search-randomizer">
        <h2 class="centered-title">Пошук за критеріями</h2>
        <sec:authorize access="hasRole('ROLE_USER')">
           <input type="submit" value="12 рандомних Загальних рецептів" class="search submit" formaction="/common/recipe-search-randomizer">
        </sec:authorize>
        <sec:authorize access="hasRole('ROLE_MODERATOR')">
           <label></label>
        </sec:authorize>


        <input type="submit" value="Пошук в Своїх рецептах" class="search submit">
        <label></label>
        <sec:authorize access="hasRole('ROLE_USER')">
           <input type="submit" value="Пошук в Загальних рецептах" class="search submit" formaction="/common/search-recipes-by-form">
        </sec:authorize>
        <sec:authorize access="hasRole('ROLE_MODERATOR')">
           <label></label>
        </sec:authorize>


        <div style="border: 1px solid #000; padding: 5px;">
            <label for="mealCategorySelect">Вибір категорії їжі:</label>
            <select id="mealCategorySelect" name="includeMealCategoryID" class="SearchModel" style="margin-top: 10px;"></select>
        </div>
        <div style="border: 1px solid #000; padding: 5px;">
            <label for="regionalCuisineSelect">Вибір регіональної кухні:</label>
            <select id="regionalCuisineSelect" name="includeRegionalCuisineID" class="SearchModel" style="margin-top: 10px;"></select>
        </div>
        <div style="border: 1px solid #000; padding: 5px;">
            <label for="cookingTime">Час приготування не більше (хвилин):</label>
            <input type="number" id="cookingTime" name="maxCookingTime" value="1000" style="margin-top: 10px;">
        </div>


        <div style="border: 1px solid #000; padding: 5px;">
            <label style="text-decoration: underline;">Вибрати інгрідієнти:</label>
            <input type="hidden" name="includeDishesByIngredientsIds" id="includeDishesByIngredientsIds" />
            <div id="includeDishesSelect" style="height: 215px; overflow: auto; margin-top: 10px;"></div>
        </div>
        <div style="border: 1px solid #000; padding: 5px;">
            <label style="text-decoration: underline;">Виключити інгрідієнти:</label>
            <input type="hidden" name="excludeDishesByIngredientsIds" id="excludeDishesByIngredientsIds" />
            <div id="excludeDishesSelect" style="height: 215px; overflow: auto; margin-top: 10px;"></div>
        </div>
        <div style="border: 1px solid #000; padding: 5px;">
            <label style="text-decoration: underline;">Виключити страви з алергенами:</label>
            <input type="hidden" name="excludeFoodWithAllergensIds" id="excludeFoodWithAllergensIds" />
            <div id="excludeAllergensSelect" style="height: 215px; overflow: auto; margin-top: 10px;"></div>
        </div>

        <sec:authorize access="hasRole('ROLE_USER')">
            <div style="border: 1px solid #000; padding: 5px;">
                <label style="text-decoration: underline;">Вибрати з ТЕГом:</label>
                <input type="hidden" name="includeCustomTagsIds" id="includeCustomTagsIds" />
                <div id="includeCustomTagsSelect" style="height: 165px; overflow: auto; margin-top: 10px;"></div>
            </div>
            <div style="border: 1px solid #000; padding: 5px;">
                <label style="text-decoration: underline;">Виключити з ТЕГом:</label>
                <input type="hidden" name="excludeCustomTagsIds" id="excludeCustomTagsIds" />
                <div id="excludeCustomTagsSelect" style="height: 165px; overflow: auto; margin-top: 10px;"></div>
            </div>
            <label></label>
        </sec:authorize>

    </form>





    <script>

        var mealCategorySelect = document.getElementById('mealCategorySelect');
        var regionalCuisineSelect = document.getElementById('regionalCuisineSelect');

        mealCategorySelect.addEventListener('change', function() {
        	var selectedCategoryId = mealCategorySelect.value;
        	includeMealCategoryID.value = selectedCategoryId;
        });

        regionalCuisineSelect.addEventListener('change', function() {
        	var selectedCuisineId = regionalCuisineSelect.value;
        	includeRegionalCuisineID.value = selectedCuisineId;
        });

        fetch('/data/all-meal-categories')
        	.then(response => response.json())
        	.then(data => {
        		mealCategorySelect.innerHTML = '';

        		var allOption = document.createElement('option');
        		allOption.value = '0';
        		allOption.textContent = 'Всі';
        		allOption.selected = true;
        		mealCategorySelect.appendChild(allOption);

        		data.forEach(category => {
        			var option = document.createElement('option');
        			option.value = category.id;
        			option.textContent = category.name;
        			mealCategorySelect.appendChild(option);
        		});
        	})
        	.catch(error => {
        		console.error('Помилка при отриманні даних з сервера: ' + error);
        	});

        fetch('/data/all-regional-cuisines')
        	.then(response => response.json())
        	.then(data => {
        		regionalCuisineSelect.innerHTML = '';

        		var allOption = document.createElement('option');
        		allOption.value = '0';
        		allOption.textContent = 'Всі';
        		allOption.selected = true;
        		regionalCuisineSelect.appendChild(allOption);

        		data.forEach(cuisine => {
        			var option = document.createElement('option');
        			option.value = cuisine.id;
        			option.textContent = cuisine.name;
        			regionalCuisineSelect.appendChild(option);
        		});
        	})
        	.catch(error => {
        		console.error('Помилка при отриманні даних з сервера: ' + error);
        	});


        var includeDishesSelect = document.getElementById('includeDishesSelect');
        var excludeDishesSelect = document.getElementById('excludeDishesSelect');

        fetch('/data/all-dishes-by-ingredients')
            .then(response => response.json())
            .then(data => {
                var includeDishesSelect = document.getElementById('includeDishesSelect');
                var form = document.getElementById('searchForm');

                includeDishesSelect.innerHTML = '';

                // Додати checkbox для "Всі інгрідієнти"
                var allCheckboxDiv = document.createElement('div');

                var allCheckbox = document.createElement('input');
                allCheckbox.type = 'checkbox';
                allCheckbox.value = '0';
                allCheckbox.id = 'ingredientCheckbox0';
                allCheckbox.checked = true;
                allCheckboxDiv.appendChild(allCheckbox);

                var allLabel = document.createElement('label');
                allLabel.for = 'allIngredientsCheckbox';
                allLabel.textContent = 'Всі інгрідієнти';
                allLabel.style.fontWeight = 'bold';
                allCheckboxDiv.appendChild(allLabel);


                includeDishesSelect.appendChild(allCheckboxDiv);
                includeDishesSelect.appendChild(document.createElement('br'));

                // Додати checkbox для кожного інгрідієнту
                data.forEach(dish => {
                    var checkboxDiv = document.createElement('div');

                    var checkbox = document.createElement('input');
                    checkbox.type = 'checkbox';
                    checkbox.value = dish.id;
                    checkbox.id = 'ingredientCheckbox' + dish.id;
                    checkboxDiv.appendChild(checkbox);

                    var label = document.createElement('label');
                    label.for = 'ingredientCheckbox' + dish.id;
                    label.textContent = dish.name;
                    checkboxDiv.appendChild(label);

                    includeDishesSelect.appendChild(checkboxDiv);
                    includeDishesSelect.appendChild(document.createElement('br'));
                });

                // Додати обробник подій для всіх чекбоксів
                var allCheckboxes = document.querySelectorAll('[id^="ingredientCheckbox"]');
                allCheckboxes.forEach(function(checkbox, index) {
                    checkbox.addEventListener('change', function() {
                        if (index === 0) {
                            // Якщо обраний перший чекбокс, встановити значення всіх інших чекбоксів в false
                            allCheckboxes.forEach(function(cb, idx) {
                                if (idx !== 0) {
                                    cb.checked = false;
                                }
                            });

                            // Заборонити вимкнення першого чекбоксу
                            checkbox.checked = true;
                        } else {
                            // Якщо будь-який інший чекбокс вибраний, встановити значення першого чекбокса в false
                            allCheckbox.checked = false;

                            // Якщо всі інші чекбокси (крім першого) вимкнені, вмикнути перший чекбокс
                            var allUnchecked = Array.from(allCheckboxes).slice(1).every(function(cb) {
                                return !cb.checked;
                            });

                            if (allUnchecked) {
                                allCheckbox.checked = true;
                            }
                        }
                        // Оновлення значень у відповідному полі форми при зміні чекбоксів
                        updateFormValues();
                    });
                });
                // Функція для оновлення значень у відповідному полі форми
                function updateFormValues() {
                    var selectedValues = Array.from(allCheckboxes)
                        .filter(checkbox => checkbox.checked)
                        .map(checkbox => checkbox.value);

                    // Запис вибраних значень у поле форми з атрибутом name="includeDishesByIngredientsIds"
                    form.elements['includeDishesByIngredientsIds'].value = selectedValues.join(',');
                }
            })
            .catch(error => {
                console.error('Помилка при отриманні даних з сервера: ' + error);
            });




        fetch('/data/all-dishes-by-ingredients')
            .then(response => response.json())
            .then(data => {
                var excludeDishesSelect = document.getElementById('excludeDishesSelect');
                var form = document.getElementById('searchForm');

                excludeDishesSelect.innerHTML = '';

                // Додати checkbox для "Без виключень"
                var allCheckboxDiv = document.createElement('div');

                var allCheckbox = document.createElement('input');
                allCheckbox.type = 'checkbox';
                allCheckbox.value = '0';
                allCheckbox.id = 'excludeIngredientCheckbox0';
                allCheckbox.checked = true;
                allCheckboxDiv.appendChild(allCheckbox);

                var allLabel = document.createElement('label');
                allLabel.for = 'excludeIngredientCheckbox0';
                allLabel.textContent = 'Без виключень';
                allLabel.style.fontWeight = 'bold';
                allCheckboxDiv.appendChild(allLabel);

                excludeDishesSelect.appendChild(allCheckboxDiv);
                excludeDishesSelect.appendChild(document.createElement('br'));

                // Додати checkbox для кожного інгрідієнту
                data.forEach(dish => {
                    var checkboxDiv = document.createElement('div');

                    var checkbox = document.createElement('input');
                    checkbox.type = 'checkbox';
                    checkbox.value = dish.id;
                    checkbox.id = 'excludeIngredientCheckbox' + dish.id;
                    checkboxDiv.appendChild(checkbox);

                    var label = document.createElement('label');
                    label.for = 'excludeIngredientCheckbox' + dish.id;
                    label.textContent = dish.name;
                    checkboxDiv.appendChild(label);

                    excludeDishesSelect.appendChild(checkboxDiv);
                    excludeDishesSelect.appendChild(document.createElement('br'));
                });

                // Додати обробник подій для всіх чекбоксів
                var allCheckboxes = document.querySelectorAll('[id^="excludeIngredientCheckbox"]');
                allCheckboxes.forEach(function(checkbox, index) {
                    checkbox.addEventListener('change', function() {
                        if (index === 0) {
                            // Якщо обраний перший чекбокс, встановити значення всіх інших чекбоксів в false
                            allCheckboxes.forEach(function(cb, idx) {
                                if (idx !== 0) {
                                    cb.checked = false;
                                }
                            });

                            // Заборонити вимкнення першого чекбоксу
                            checkbox.checked = true;
                        } else {
                            // Якщо будь-який інший чекбокс вибраний, встановити значення першого чекбокса в false
                            allCheckbox.checked = false;

                            // Якщо всі інші чекбокси (крім першого) вимкнені, вмикнути перший чекбокс
                            var allUnchecked = Array.from(allCheckboxes).slice(1).every(function(cb) {
                                return !cb.checked;
                            });

                            if (allUnchecked) {
                                allCheckbox.checked = true;
                            }
                        }
                        // Оновлення значень у відповідному полі форми при зміні чекбоксів
                        updateFormValues();
                    });
                });
                // Функція для оновлення значень у відповідному полі форми
                function updateFormValues() {
                    var selectedValues = Array.from(allCheckboxes)
                        .filter(checkbox => checkbox.checked)
                        .map(checkbox => checkbox.value);

                    // Запис вибраних значень у поле форми з атрибутом name="excludeDishesByIngredientsIds"
                    form.elements['excludeDishesByIngredientsIds'].value = selectedValues.join(',');
                }
            })
            .catch(error => {
                console.error('Помилка при отриманні даних з сервера: ' + error);
            });



        var excludeAllergensSelect = document.getElementById('excludeAllergensSelect');

        fetch('/data/all-allergens')
            .then(response => response.json())
            .then(data => {
                var excludeAllergensSelect = document.getElementById('excludeAllergensSelect');
                var form = document.getElementById('searchForm');

                excludeAllergensSelect.innerHTML = '';

                // Додати checkbox для "Без виключень"
                var allCheckboxDiv = document.createElement('div');

                var allCheckbox = document.createElement('input');
                allCheckbox.type = 'checkbox';
                allCheckbox.value = '0';
                allCheckbox.id = 'excludeAllergenCheckbox0';
                allCheckbox.checked = true;
                allCheckboxDiv.appendChild(allCheckbox);

                var allLabel = document.createElement('label');
                allLabel.for = 'excludeAllergenCheckbox0';
                allLabel.textContent = 'Без виключень';
                allLabel.style.fontWeight = 'bold';
                allCheckboxDiv.appendChild(allLabel);

                excludeAllergensSelect.appendChild(allCheckboxDiv);
                excludeAllergensSelect.appendChild(document.createElement('br'));

                // Додати checkbox для кожного алергену
                data.forEach(allergen => {
                    var checkboxDiv = document.createElement('div');

                    var checkbox = document.createElement('input');
                    checkbox.type = 'checkbox';
                    checkbox.value = allergen.id;
                    checkbox.id = 'excludeAllergenCheckbox' + allergen.id;
                    checkboxDiv.appendChild(checkbox);

                    var label = document.createElement('label');
                    label.for = 'excludeAllergenCheckbox' + allergen.id;
                    label.textContent = allergen.name;
                    checkboxDiv.appendChild(label);

                    excludeAllergensSelect.appendChild(checkboxDiv);
                    excludeAllergensSelect.appendChild(document.createElement('br'));
                });

                // Додати обробник подій для всіх чекбоксів
                var allCheckboxes = document.querySelectorAll('[id^="excludeAllergenCheckbox"]');
                allCheckboxes.forEach(function(checkbox, index) {
                    checkbox.addEventListener('change', function() {
                        if (index === 0) {
                            // Якщо обраний перший чекбокс, встановити значення всіх інших чекбоксів в false
                            allCheckboxes.forEach(function(cb, idx) {
                                if (idx !== 0) {
                                    cb.checked = false;
                                }
                            });

                            // Заборонити вимкнення першого чекбоксу
                            checkbox.checked = true;
                        } else {
                            // Якщо будь-який інший чекбокс вибраний, встановити значення першого чекбокса в false
                            allCheckbox.checked = false;

                            // Якщо всі інші чекбокси (крім першого) вимкнені, вмикнути перший чекбокс
                            var allUnchecked = Array.from(allCheckboxes).slice(1).every(function(cb) {
                                return !cb.checked;
                            });

                            if (allUnchecked) {
                                allCheckbox.checked = true;
                            }
                        }
                        // Оновлення значень у відповідному полі форми при зміні чекбоксів
                        updateFormValues();
                    });
                });
                // Функція для оновлення значень у відповідному полі форми
                function updateFormValues() {
                    var selectedValues = Array.from(allCheckboxes)
                        .filter(checkbox => checkbox.checked)
                        .map(checkbox => checkbox.value);

                    // Запис вибраних значень у поле форми з атрибутом name="excludeFoodWithAllergensIds"
                    form.elements['excludeFoodWithAllergensIds'].value = selectedValues.join(',');
                }
            })
            .catch(error => {
                console.error('Помилка при отриманні даних з сервера: ' + error);
            });



        var includeCustomTagsSelect = document.getElementById('includeCustomTagsSelect');
        var excludeCustomTagsSelect = document.getElementById('excludeCustomTagsSelect');

        fetch('/data/all-custom-tags')
            .then(response => response.json())
            .then(data => {
                var includeCustomTagsSelect = document.getElementById('includeCustomTagsSelect');
                var form = document.getElementById('searchForm');

                includeCustomTagsSelect.innerHTML = '';

                // Додати checkbox для "Всі"
                var allCheckboxDiv = document.createElement('div');

                var allCheckbox = document.createElement('input');
                allCheckbox.type = 'checkbox';
                allCheckbox.value = '0';
                allCheckbox.id = 'includeCustomTagCheckbox0';
                allCheckbox.checked = true;
                allCheckboxDiv.appendChild(allCheckbox);

                var allLabel = document.createElement('label');
                allLabel.for = 'includeCustomTagCheckbox0';
                allLabel.textContent = 'Всі';
                allLabel.style.fontWeight = 'bold';
                allCheckboxDiv.appendChild(allLabel);

                includeCustomTagsSelect.appendChild(allCheckboxDiv);
                includeCustomTagsSelect.appendChild(document.createElement('br'));

                // Додати checkbox для кожного тегу
                data.forEach(tag => {
                    var checkboxDiv = document.createElement('div');

                    var checkbox = document.createElement('input');
                    checkbox.type = 'checkbox';
                    checkbox.value = tag.id;
                    checkbox.id = 'includeCustomTagCheckbox' + tag.id;
                    checkboxDiv.appendChild(checkbox);

                    var label = document.createElement('label');
                    label.for = 'includeCustomTagCheckbox' + tag.id;
                    label.textContent = tag.name;
                    checkboxDiv.appendChild(label);

                    includeCustomTagsSelect.appendChild(checkboxDiv);
                    includeCustomTagsSelect.appendChild(document.createElement('br'));
                });

                // Додати обробник подій для всіх чекбоксів
                var allCheckboxes = document.querySelectorAll('[id^="includeCustomTagCheckbox"]');
                allCheckboxes.forEach(function(checkbox, index) {
                    checkbox.addEventListener('change', function() {
                        if (index === 0) {
                            // Якщо обраний перший чекбокс, встановити значення всіх інших чекбоксів в false
                            allCheckboxes.forEach(function(cb, idx) {
                                if (idx !== 0) {
                                    cb.checked = false;
                                }
                            });

                            // Заборонити вимкнення першого чекбоксу
                            checkbox.checked = true;
                        } else {
                            // Якщо будь-який інший чекбокс вибраний, встановити значення першого чекбокса в false
                            allCheckbox.checked = false;

                            // Якщо всі інші чекбокси (крім першого) вимкнені, вмикнути перший чекбокс
                            var allUnchecked = Array.from(allCheckboxes).slice(1).every(function(cb) {
                                return !cb.checked;
                            });

                            if (allUnchecked) {
                                allCheckbox.checked = true;
                            }
                        }
                        // Оновлення значень у відповідному полі форми при зміні чекбоксів
                        updateFormValues();

                    });
                });
                // Функція для оновлення значень у відповідному полі форми
                function updateFormValues() {
                    var selectedValues = Array.from(allCheckboxes)
                        .filter(checkbox => checkbox.checked)
                        .map(checkbox => checkbox.value);

                    // Запис вибраних значень у поле форми з атрибутом name="includeCustomTagsIds"
                    form.elements['includeCustomTagsIds'].value = selectedValues.join(',');
                }


            })
            .catch(error => {
                console.error('Помилка при отриманні даних з сервера: ' + error);
            });

        fetch('/data/all-custom-tags')
            .then(response => response.json())
            .then(data => {
                var excludeCustomTagsSelect = document.getElementById('excludeCustomTagsSelect');
                var form = document.getElementById('searchForm');

                excludeCustomTagsSelect.innerHTML = '';

                // Додати checkbox для "Без виключень"
                var allCheckboxDiv = document.createElement('div');

                var allCheckbox = document.createElement('input');
                allCheckbox.type = 'checkbox';
                allCheckbox.value = '0';
                allCheckbox.id = 'excludeCustomTagCheckbox0';
                allCheckbox.checked = true;
                allCheckboxDiv.appendChild(allCheckbox);

                var allLabel = document.createElement('label');
                allLabel.for = 'excludeCustomTagCheckbox0';
                allLabel.textContent = 'Без виключень';
                allLabel.style.fontWeight = 'bold';
                allCheckboxDiv.appendChild(allLabel);

                excludeCustomTagsSelect.appendChild(allCheckboxDiv);
                excludeCustomTagsSelect.appendChild(document.createElement('br'));

                // Додати checkbox для кожного тегу
                data.forEach(tag => {
                    var checkboxDiv = document.createElement('div');

                    var checkbox = document.createElement('input');
                    checkbox.type = 'checkbox';
                    checkbox.value = tag.id;
                    checkbox.id = 'excludeCustomTagCheckbox' + tag.id;
                    checkboxDiv.appendChild(checkbox);

                    var label = document.createElement('label');
                    label.for = 'excludeCustomTagCheckbox' + tag.id;
                    label.textContent = tag.name;
                    checkboxDiv.appendChild(label);

                    excludeCustomTagsSelect.appendChild(checkboxDiv);
                    excludeCustomTagsSelect.appendChild(document.createElement('br'));
                });

                // Додати обробник подій для всіх чекбоксів
                var allCheckboxes = document.querySelectorAll('[id^="excludeCustomTagCheckbox"]');
                allCheckboxes.forEach(function(checkbox, index) {
                    checkbox.addEventListener('change', function() {
                        if (index === 0) {
                            // Якщо обраний перший чекбокс, встановити значення всіх інших чекбоксів в false
                            allCheckboxes.forEach(function(cb, idx) {
                                if (idx !== 0) {
                                    cb.checked = false;
                                }
                            });

                            // Заборонити вимкнення першого чекбоксу
                            checkbox.checked = true;
                        } else {
                            // Якщо будь-який інший чекбокс вибраний, встановити значення першого чекбокса в false
                            allCheckbox.checked = false;

                            // Якщо всі інші чекбокси (крім першого) вимкнені, вмикнути перший чекбокс
                            var allUnchecked = Array.from(allCheckboxes).slice(1).every(function(cb) {
                                return !cb.checked;
                            });

                            if (allUnchecked) {
                                allCheckbox.checked = true;
                            }
                        }
                        // Оновлення значень у відповідному полі форми при зміні чекбоксів
                        updateFormValues();


                    });
                });
                // Функція для оновлення значень у відповідному полі форми
                function updateFormValues() {
                    var selectedValues = Array.from(allCheckboxes)
                        .filter(checkbox => checkbox.checked)
                        .map(checkbox => checkbox.value);

                    // Запис вибраних значень у поле форми з атрибутом name="excludeCustomTagsIds"
                    form.elements['excludeCustomTagsIds'].value = selectedValues.join(',');
                }


            })
            .catch(error => {
                console.error('Помилка при отриманні даних з сервера: ' + error);
            });


    </script>
</body>




