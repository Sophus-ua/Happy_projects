<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Recipe Handler</title>

    <style>

        .recipeDesigner textarea {
          width: 100%; /* Розширення на всі три колонки */
          padding: 10px; /* Попередній відступ */
          border: 1px solid #ccc;
          border-radius: 2px;
          font-size: 16px;
          box-sizing: border-box; /* Включити розрахунок ширини, включаючи границі та внутрішні відступи */
          resize: none; /* Вимкнути зміну розміру поля */
        }

        .recipeText {
          grid-column: span 3;
        }



        .file-upload {
            display: inline-block;
            position: relative;
            overflow: hidden;
            border: 1px solid #ccc;
            background-color: #f9f9f9;
            color: #333;
            border-radius: 5px;
            padding: 5px 10px;
            cursor: pointer;
          }

          .file-upload input[type="file"] {
            position: absolute;
            top: 0;
            right: 0;
            margin: 0;
            padding: 0;
            font-size: 20px;
            cursor: pointer;
            opacity: 0;
            filter: alpha(opacity=0);
          }


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

        /* Стилі для форми recipeDesigner */
        .recipeDesigner {
        display: grid;
        grid-template-columns: 1fr 1fr 1fr; /* Розділення на дві колони */
        gap: 10px; /* Відстань між елементами */
        }


        /* Стилі для випадаючих списків в формі recipeDesigner */
        .recipeDesigner select {
            width: 200px; /* Змінити ширину випадаючого списку */
        }

        /* Стилі для роздільної лінії */
        .divider {
            border-top: 1px solid #ccc;
            margin-top: 5px;
            grid-column: span 3;
        }

        /* Стилі для міток, полів вводу та випадаючих списків у формах */
        .form label, .form input, .form select {
            margin-bottom: 10px;
        }

        .centered-title {
            text-align: center;
        }

        .form-group {
            display: grid;
            grid-template-columns: 1fr 1fr 1fr; /* Розділення на дві колони */
            gap: 10px; /* Відстань між елементами */
        }

        #visibleSubmitButton {
            height: 50px;
        }

        .file-upload {
            height: 40px;
            display: inline-block; /* щоб властивість height застосовувалася правильно */
            position: relative; /* Додаємо позиціонування для зручності центрування тексту */
        }
        #fileText {
            position: absolute;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            color: black; /* Початковий колір тексту */
        }
        .file-upload:hover #fileText {
            color: white; /* Колір тексту при наведенні */
        }

        #visibleSubmitButton:hover,
        .file-upload:hover {
            background-color: #a020f0; /* Світлофіолетовий колір */
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

    </style>

</head>






<body>

        <button type="button" onclick="window.location.href='/main'" class="form-button">На головну сторінку</button>

        <h3 class="centered-title">Конструктор Рецепта</h3>

    <form action="/recipe-add-or-update" method="post" class="recipeDesigner" id="recipeDesigner" >
        <input type="hidden" id="recipeID" name="id" >

        <label for="recipeName"  class="centered-title" style="color: red;">Введіть Назву:</label>
        <input type="text" id="recipeName" name="name" required>
        <label></label>

        <div class="divider"></div>

        <label for="mealCategorySelect" style="color: red;">Виберіть Категорію Їжі:</label>
        <label for="regionalCuisineSelect">Можлива Регіональна Кухня:</label>
        <label></label>

        <select id="mealCategorySelect" name="mealCategoryID" class="RecipeDTO" required>
                </select>
        <select id="regionalCuisineSelect" name="regionalCuisineID" class="RecipeDTO">
                </select>
        <label></label>

        <div class="divider"></div>

        <label for="cookingTime">Час Приготування (хвилин):</label>
        <label for="portions">Порцій в рецепті:</label>
        <label for="calories">Калорій:</label>

        <input type="number" id="cookingTime" name="cookingTimeMin" class="RecipeDTO">
        <input type="number" id="portions" name="portions" class="RecipeDTO">
        <input type="number" id="calories" name="calories" class="RecipeDTO">

        <div style="border: 1px solid #000; padding: 5px;">
             <label for="dishesIngredientsSelect" style="text-decoration: underline;">Можливі Інгрідієнти:</label>
             <div id="dishesIngredientsSelect" style="height: 200px; overflow: auto; margin-top: 10px;"></div>
        </div>
        <div style="border: 1px solid #000; padding: 5px;">
             <label for="commonAllergensSelect" style="text-decoration: underline;">Можливі Алергени:</label>
             <div id="commonAllergensSelect" style="height: 200px; overflow: auto; margin-top: 10px;"></div>
        </div>
        <div style="border: 1px solid #000; padding: 5px;">
             <label for="customTagsSelect" style="text-decoration: underline;">Свої Теги:</label>
             <div id="customTagsSelect" style="height: 200px; overflow: auto; margin-top: 10px;"></div>
        </div>

        <input type="hidden" name="dishesByIngredientsIds" id="dishesByIngredientsIds" />
        <input type="hidden" name="allergensIds" id="allergensIds" />
        <input type="hidden" name="customTagsIds" id="customTagsIds" />


        <label></label>
        <label for="recipeText" class="centered-title"><h4>Рецепт:</h4></label>
        <label></label>

        <div class="recipeText">
          <textarea id="recipeText" name="recipeTextLine" rows="10" cols="30" required></textarea>
        </div>

        <input type="submit" id="hiddenSubmitButton" style="display: none;">

    </form>

    <div class="form-group">
        <label for="imageInput" class="file-upload">
            <span id="fileText">Вибрати малюнок</span>
            <input type="file" id="imageInput" accept=".jpg, .jpeg, .png" style="display: none;"
                   onchange="fileSelected(this);" >
        </label>

        <button id="visibleSubmitButton" onclick="submitForm()">Зберегти рецепт</button>
    </div>

    <div id="recipeDTOData" data-recipes='${recipeDTOJson}' style="display: none;"></div>








     <script>

       function submitForm() {
           const imageInput = document.getElementById('imageInput');
           const imageKey = document.getElementById('recipeName').value;
           const mealCategorySelect = document.getElementById('mealCategorySelect').value;
           const recipeTextInputted = document.getElementById('recipeText').value;

           if (imageInput.files.length > 0 && imageKey && mealCategorySelect && recipeTextInputted) {
               document.getElementById('hiddenSubmitButton').click();

               const selectedFile = imageInput.files[0];
               const reader = new FileReader();

               reader.onloadend = function () {
                   // Отримати байти
                   const imageBytes = new Uint8Array(reader.result);

                   // Конвертувати байти в base64
                   const uint8Array = new Uint8Array(imageBytes);
                   const base64Image = btoa(String.fromCharCode(...uint8Array));

                   // Створити JSON-об'єкт для відправки на сервер
                   const jsonPayload = {
                       base64Image: base64Image,
                       imageKey: imageKey
                   };

                   const xhr = new XMLHttpRequest();
                   xhr.open('POST', '/data/imageUpload', true);
                   xhr.setRequestHeader('Content-Type', 'application/json');
                   xhr.onload = function () {
                       if (xhr.status === 200) {
                           alert('Зображення та дані успішно збережено.');
                       } else {
                           alert('Помилка під час збереження зображення та даних.');
                       }
                   };
                   xhr.onerror = function () {
                       alert('Помилка під час відправки запиту.');
                   };

                   // Відправити JSON-строку в тілі запиту
                   xhr.send(JSON.stringify(jsonPayload));
               };

               // Прочитати файл в Array Buffer
               reader.readAsArrayBuffer(selectedFile);
           } else {
               document.getElementById('hiddenSubmitButton').addEventListener('click', function() {
                   alert('зачекайте пару секунд...');
               });
               document.getElementById('hiddenSubmitButton').click();
           }
       }









       // Перевірка завантаженного файлу
         const imageInput = document.getElementById('imageInput');

         imageInput.addEventListener('change', function() {
             const selectedFile = imageInput.files[0]; // Отримуємо вибраний файл

             if (selectedFile) {
                 const maxFileSize = 16777215; // Максимальний розмір MEDIUMBLOB

                 if (selectedFile.size > maxFileSize) {
                     alert('Розмір файлу перевищує допустимий ліміт.');
                     // Очистити значення поля вводу файлу
                     imageInput.value = null;
                 }
             }
         });

         // Зміна тексту кнопки малюнка
         function fileSelected(input) {
             var fileText = document.getElementById('fileText');
             if (input.files.length > 0) {
                 fileText.textContent = 'Файл вибрано';
                 fileText.style.color = 'green';
             } else {
                 fileText.textContent = 'Вибрати малюнок';
                 fileText.style.color = 'black'; // Повернемо колір тексту до чорного, якщо файл не вибрано
             }
         }






        var mealCategorySelect = document.getElementById('mealCategorySelect');
        var regionalCuisineSelect = document.getElementById('regionalCuisineSelect');

        var recipeDTOData = document.getElementById('recipeDTOData');
        var recipeDTOJson = recipeDTOData.getAttribute('data-recipes');
        var recipeDTO = null;
        if (recipeDTOJson) {
            recipeDTO = JSON.parse(recipeDTOJson);
            console.log(recipeDTO);
        } else {
            console.log('data-recipes відсутній або пустий');
        }


        mealCategorySelect.addEventListener('change', function() {
        	var selectedCategoryId = mealCategorySelect.value;
        	includeMealCategoryID.value = selectedCategoryId;

            if (selectedCategoryId === '') {
                mealCategorySelect.setCustomValidity("Будь ласка, оберіть категорію їжі");
            } else {
                mealCategorySelect.setCustomValidity("");
            }

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
        		allOption.value = '';
        		allOption.textContent = '';
        		allOption.disabled = true;
                allOption.selected = !recipeDTO || !recipeDTO.mealCategoryID || recipeDTO.mealCategoryID == 0 ;
        		mealCategorySelect.appendChild(allOption);

        		data.forEach(category => {
        			var option = document.createElement('option');
        			option.value = category.id;
        			option.textContent = category.name;
        			option.selected = !allOption.selected && recipeDTO.mealCategoryID == category.id;
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
        		allOption.textContent = '';
        		if (!recipeDTO || !recipeDTO.regionalCuisineID || recipeDTO.regionalCuisineID == 0){
        		    allOption.selected = true;
        		} else {
        		    allOption.disabled = true;
        		    allOption.selected = false;
        		};
        		regionalCuisineSelect.appendChild(allOption);

        		data.forEach(cuisine => {
        			var option = document.createElement('option');
        			option.value = cuisine.id;
        			option.textContent = cuisine.name;
        			if (!allOption.selected && recipeDTO.regionalCuisineID == cuisine.id){
        			    option.selected = true;
        			    allOption.disabled = false;
        			} else {
        			    option.selected = false;
        			};
        			option.selected = !allOption.selected && recipeDTO.regionalCuisineID == cuisine.id;

        			regionalCuisineSelect.appendChild(option);
        		});
        	})
        	.catch(error => {
        		console.error('Помилка при отриманні даних з сервера: ' + error);
        	});


        var dishesIngredientsSelect = document.getElementById('dishesIngredientsSelect');
        var commonAllergensSelect = document.getElementById('commonAllergensSelect');
        var customTagsSelect = document.getElementById('customTagsSelect');

        fetch('/data/all-dishes-by-ingredients')
            .then(response => response.json())
            .then(data => {
                var dishesIngredientsSelect = document.getElementById('dishesIngredientsSelect');
                var form = document.getElementById('recipeDesigner');

                dishesIngredientsSelect.innerHTML = '';

                var allCheckboxDiv = document.createElement('div');

                var allCheckbox = document.createElement('input');
                allCheckbox.type = 'checkbox';
                allCheckbox.value = '0';
                allCheckbox.id = 'dishesIngredientCheckbox0';
                allCheckbox.checked = !recipeDTO || !Array.isArray(recipeDTO.dishesByIngredientsIds) || recipeDTO.dishesByIngredientsIds.length == 0;
                allCheckboxDiv.appendChild(allCheckbox);

                var allLabel = document.createElement('label');
                allLabel.for = 'dishesIngredientCheckbox0';
                allLabel.textContent = 'Без вибору інгрідієнтів';
                allLabel.style.fontWeight = 'bold';
                allCheckboxDiv.appendChild(allLabel);

                dishesIngredientsSelect.appendChild(allCheckboxDiv);
                dishesIngredientsSelect.appendChild(document.createElement('br'));


                data.forEach(dish => {
                    var checkboxDiv = document.createElement('div');

                    var checkbox = document.createElement('input');
                    checkbox.type = 'checkbox';
                    checkbox.value = dish.id;
                    checkbox.id = 'dishesIngredientCheckbox' + dish.id;
                    checkbox.checked = !allCheckbox.checked && recipeDTO.dishesByIngredientsIds.includes(parseInt(dish.id));
                    checkboxDiv.appendChild(checkbox);

                    var label = document.createElement('label');
                    label.for = 'dishesIngredientCheckbox' + dish.id;
                    label.textContent = dish.name;
                    checkboxDiv.appendChild(label);

                    dishesIngredientsSelect.appendChild(checkboxDiv);
                    dishesIngredientsSelect.appendChild(document.createElement('br'));
                });

                // Додати обробник подій для всіх чекбоксів
                var allCheckboxes = document.querySelectorAll('[id^="dishesIngredientCheckbox"]');
                allCheckboxes.forEach(function (checkbox, index) {
                    checkbox.addEventListener('change', function () {
                        if (index === 0) {
                            allCheckboxes.forEach(function (cb, idx) {
                                if (idx !== 0) {
                                    cb.checked = false;
                                }
                            });
                            checkbox.checked = true;
                        } else {
                            allCheckbox.checked = false;

                            var allUnchecked = Array.from(allCheckboxes).slice(1).every(function (cb) {
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
                        .filter(checkbox => checkbox.checked && checkbox.value !== '0')
                        .map(checkbox => checkbox.value);

                    // Запис вибраних значень у поле форми з атрибутом name="dishesByIngredientsIds"
                    form.elements['dishesByIngredientsIds'].value = selectedValues.join(',');
                }
            })
            .catch(error => {
                console.error('Помилка при отриманні даних з сервера: ' + error);
            });


        fetch('/data/all-allergens')
            .then(response => response.json())
            .then(data => {
                var commonAllergensSelect = document.getElementById('commonAllergensSelect');
                var form = document.getElementById('recipeDesigner');

                commonAllergensSelect.innerHTML = '';


                var allCheckboxDiv = document.createElement('div');

                var allCheckbox = document.createElement('input');
                allCheckbox.type = 'checkbox';
                allCheckbox.value = '0';
                allCheckbox.id = 'commonAllergenCheckbox0';
                allCheckbox.checked = !recipeDTO || !Array.isArray(recipeDTO.allergensIds) || recipeDTO.allergensIds.length === 0;
                allCheckboxDiv.appendChild(allCheckbox);


                var allLabel = document.createElement('label');
                allLabel.for = 'commonAllergenCheckbox0';
                allLabel.textContent = 'Без вибору Алергена';
                allLabel.style.fontWeight = 'bold';
                allCheckboxDiv.appendChild(allLabel);

                commonAllergensSelect.appendChild(allCheckboxDiv);
                commonAllergensSelect.appendChild(document.createElement('br'));

                // Додати checkbox для кожного алергену
                data.forEach(allergen => {
                    var checkboxDiv = document.createElement('div');

                    var checkbox = document.createElement('input');
                    checkbox.type = 'checkbox';
                    checkbox.value = allergen.id;
                    checkbox.id = 'commonAllergenCheckbox' + allergen.id;
                    checkbox.checked = !allCheckbox.checked && recipeDTO.allergensIds.includes(parseInt(allergen.id));
                    checkboxDiv.appendChild(checkbox);

                    var label = document.createElement('label');
                    label.for = 'commonAllergenCheckbox' + allergen.id;
                    label.textContent = allergen.name;
                    checkboxDiv.appendChild(label);

                    commonAllergensSelect.appendChild(checkboxDiv);
                    commonAllergensSelect.appendChild(document.createElement('br'));
                });

                // Додати обробник подій для всіх чекбоксів
                var allCheckboxes = document.querySelectorAll('[id^="commonAllergenCheckbox"]');
                allCheckboxes.forEach(function (checkbox, index) {
                    checkbox.addEventListener('change', function () {
                        if (index === 0) {
                            allCheckboxes.forEach(function (cb, idx) {
                                if (idx !== 0) {
                                    cb.checked = false;
                                }
                            });
                            checkbox.checked = true;
                        } else {
                            allCheckboxes[0].checked = false;

                            var allUnchecked = Array.from(allCheckboxes).every(function (cb) {
                                return !cb.checked;
                            });

                            if (allUnchecked) {
                                allCheckboxes[0].checked = true;
                            }
                        }
                        // Оновлення значень у відповідному полі форми при зміні чекбоксів
                        updateFormValues();
                    });
                });

                // Функція для оновлення значень у відповідному полі форми
                function updateFormValues() {
                    var selectedValues = Array.from(allCheckboxes)
                        .filter(checkbox => checkbox.checked && checkbox.value !== '0')
                        .map(checkbox => checkbox.value);

                    // Запис вибраних значень у поле форми з атрибутом name="allergensIds"
                    form.elements['allergensIds'].value = selectedValues.join(',');
                }
            })
            .catch(error => {
                console.error('Помилка при отриманні даних з сервера: ' + error);
            });



        fetch('/data/all-custom-tags')
            .then(response => response.json())
            .then(data => {
                var customTagsSelect = document.getElementById('customTagsSelect');
                var form = document.getElementById('recipeDesigner');

                customTagsSelect.innerHTML = '';


                var allCheckboxDiv = document.createElement('div');

                var allCheckbox = document.createElement('input');
                allCheckbox.type = 'checkbox';
                allCheckbox.value = '0';
                allCheckbox.id = 'customTagCheckbox0';
                allCheckbox.checked = !recipeDTO || !Array.isArray(recipeDTO.customTagsIds) || recipeDTO.customTagsIds.length == 0;
                allCheckboxDiv.appendChild(allCheckbox);


                var allLabel = document.createElement('label');
                allLabel.for = 'customTagCheckbox0';
                allLabel.textContent = 'Без вибору Тега';
                allLabel.style.fontWeight = 'bold';
                allCheckboxDiv.appendChild(allLabel);

                customTagsSelect.appendChild(allCheckboxDiv);
                customTagsSelect.appendChild(document.createElement('br'));

                // Додати checkbox для кожного тегу
                data.forEach(tag => {
                    var checkboxDiv = document.createElement('div');

                    var checkbox = document.createElement('input');
                    checkbox.type = 'checkbox';
                    checkbox.value = tag.id;
                    checkbox.id = 'customTagCheckbox' + tag.id;
                    checkbox.checked = !allCheckbox.checked && recipeDTO.customTagsIds.includes(parseInt(tag.id));
                    checkboxDiv.appendChild(checkbox);

                    var label = document.createElement('label');
                    label.for = 'customTagCheckbox' + tag.id;
                    label.textContent = tag.name;
                    checkboxDiv.appendChild(label);

                    customTagsSelect.appendChild(checkboxDiv);
                    customTagsSelect.appendChild(document.createElement('br'));
                });

                // Додати обробник подій для всіх чекбоксів
                var allCheckboxes = document.querySelectorAll('[id^="customTagCheckbox"]');
                allCheckboxes.forEach(function (checkbox, index) {
                    checkbox.addEventListener('change', function () {
                        if (index === 0) {
                            allCheckboxes.forEach(function (cb, idx) {
                                if (idx !== 0) {
                                    cb.checked = false;
                                }
                            });
                            checkbox.checked = true;
                        } else {
                            allCheckbox.checked = false;

                            var allUnchecked = Array.from(allCheckboxes).slice(1).every(function (cb) {
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

                    // Запис вибраних значень у поле форми з атрибутом name="customTagsIds"
                    form.elements['customTagsIds'].value = selectedValues.join(',');
                }
            })
            .catch(error => {
                console.error('Помилка при отриманні даних з сервера: ' + error);
            });


        // автозаповнення прости полів
        document.addEventListener("DOMContentLoaded", () => {
            var recipeDTOData = document.getElementById('recipeDTOData');
            var recipeDTOJson = recipeDTOData.getAttribute('data-recipes');

            if (recipeDTOJson !== null) {
                var recipeDTO = JSON.parse(recipeDTOJson);

                // Встановити значення для полів форми на основі `recipeDTO`
                document.getElementById('recipeID').value = recipeDTO.id;
                document.getElementById('recipeName').value = recipeDTO.name;
                document.getElementById('cookingTime').value = recipeDTO.cookingTimeMin;
                document.getElementById('portions').value = recipeDTO.portions;
                document.getElementById('calories').value = recipeDTO.calories;
                document.getElementById('recipeText').value = recipeDTO.recipeTextLine;

                // Викликати функцію для оновлення тексту на кнопці завантаження файлу
                fileSelected(document.getElementById('imageInput'));
            }
        });



     </script>
</body>