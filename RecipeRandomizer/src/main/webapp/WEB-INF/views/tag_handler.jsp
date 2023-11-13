<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html>

<head>
    <meta charset="UTF-8">
    <title>Main</title>

    <style>


        /* Стилі для форм */
        .custom-form {
            margin-bottom: 10px;
            display: flex;
            align-items: center;
            padding: 10px;
        }

        /* Стилі для поля вводу в формі addTagForm та deleteTagForm */
        .custom-form input[type="text"] {
            width: 400px; /* Збільшити ширину в два рази */
            height: 25px;
        }

        /* Стилі для кнопок submit у формах addTagForm та deleteTagForm */
        .custom-form input[type="submit"] {
            margin-left: 10px; /* Відступ зліва для кнопки submit */
            width: 150px;
            height: 30px;
        }

        /* Стилі для форми searchForm */
        .search-form {
            display: grid;
            grid-template-columns: 1fr 1fr 1fr; /* Розділення на дві колони */
            gap: 10px; /* Відстань між елементами */
        }

        /* Стилі для випадаючих списків у формі searchForm */
        .search-form select {
            width: 200px; /* Змінити ширину випадаючого списку */
            height: 50px;
            font-size: 20px;
        }

        /* Стилі для роздільної лінії */
        .divider {
            border-top: 1px solid #ccc;
            margin-top: 20px;
        }

        /* Стилі для міток, полів вводу та випадаючих списків у формах */
        .custom-form label, .custom-form input, .custom-form select {
            margin-bottom: 10px;
            font-size: 20px;
        }

        .centered-title {
            text-align: center; /* Центруємо по горизонталі */
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

    <div style="text-align: center;">
        <c:if test="${not empty message}">
            <p style="font-weight: bold; font-size: 20px;">${message}</p>
        </c:if>
    </div>

    <div class="divider"></div>

    <form action="/add-tag" method="post" class="custom-form">
        <label for="addTag"><h4> Додати новий ТЕГ:</h4></label>
        <input type="text" id="addTag" name="name">
        <input type="submit" value="Додати" class="form-button">
    </form>

    <div class="divider"></div>

    <form action="/delete-tag" method="post" class="custom-form">
        <label for="deleteTag"><h4> Видалити ТЕГ:</h4></label>
        <select id="deleteTagSelect" name="id" class="SearchModel"></select>
        <input type="submit" value="Видалити" class="form-button">
    </form>

    <div class="divider"></div>
</body>

</html>




    <script>

        var deleteTagSelect = document.getElementById('deleteTagSelect');
        var idInput = document.getElementById('id');

        deleteTagSelect.addEventListener('change', function() {
        	var selectedTagId = deleteTagSelect.value;
        	idInput.value = selectedTagId;
        });

        fetch('/data/all-custom-tags')
        	.then(response => response.json())
        	.then(data => {
        		deleteTagSelect.innerHTML = '';

        		var allOption = document.createElement('option');
        		allOption.value = '0';
        		allOption.textContent = '';
        		allOption.selected = true;
        		deleteTagSelect.appendChild(allOption);

        		data.forEach(tag => {
        			var option = document.createElement('option');
        			option.value = tag.id;
        			option.textContent = tag.name;
        			deleteTagSelect.appendChild(option);
        		});
        	})
        	.catch(error => {
        		console.error('Помилка при отриманні даних з сервера: ' + error);
        	});



    </script>
</body>