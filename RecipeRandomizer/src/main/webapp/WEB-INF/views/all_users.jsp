<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
	<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
		<html>

		<head>
			<meta charset="UTF-8">
			<title>Users found</title>

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
               }

			</style>

		</head>

		<body>
		    <div class="header">
                <h1>Сторінка адміна</h1>
            </div>

		    <button type="button" onclick="window.location.href='/admin/main'" class="form-button">На головну сторінку</button>

			<div style="text-align: center;">
                <c:if test="${not empty message}">
                    <p style="font-weight: bold; font-size: 30px; color: #008000;">${message}</p>
                </c:if>
            </div>

			<h3 style="text-align: center;"> Список користувачів </h3>
			<table>
				<tr>
					<th style="width: 40px; border: 1px solid gray;"> ID </th>
					<th style="width: 100px; border: 1px solid gray;"> Логін </th>
					<th style="width: 100px; border: 1px solid gray;"> Ім`я </th>
					<th style="width: 120px; border: 1px solid gray;"> Дата реїстрації </th>
					<th style="width: 130px; border: 1px solid gray;"> Дата останнього логування </th>
					<th style="width: 85px; border: 1px solid gray;"> Стан активації </th>
					<th style="width: 150px; border: 1px solid gray;"></th>
					<th style="width: 150px; border: 1px solid gray;"></th>
				</tr>
				<c:forEach items="${users}" var="user">
					<tr>
						<td>${user.id}</td>
						<td>${user.username}</td>
						<td>${user.ownName}</td>
						<td>${user.registrationDate}</td>
						<td>${user.lastLoginDate}</td>
						<td>${user.enabled}</td>
						<td>
                            <c:url var="changeStatusUrl" value="/admin/change-activity-status/${user.id}" />
                            <c:choose>
                                <c:when test="${user.enabled}">
                                    <a href="${changeStatusUrl}">Деактивувати</a>
                                </c:when>
                                <c:otherwise>
                                    <a href="${changeStatusUrl}">Активувати</a>
                                </c:otherwise>
                            </c:choose>
                        </td>
						<td>
                            <a href="<c:url value='/admin/delete-user/${user.id}' />" onclick="return confirmDelete();">Видалити</a>
                        </td>
					</tr>
				</c:forEach>
			</table>

		<script>
            function confirmDelete() {
                return confirm("Ви впевнені, що хочете видалити цього користувача?");
            }
        </script>


	</body>

	</html>