<%--
  Created by IntelliJ IDEA.
  User: strem
  Date: 22.12.2024
  Time: 2:50
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<div class="navbar">
    <div class="nav-buttons">
        <button onclick="navigate('entry')">Entry</button>
        <button onclick="navigate('city')">City</button>
        <button onclick="navigate('manufacture')">Manufacture</button>
        <button onclick="navigate('sampleType')">Sample Type</button>
        <button onclick="navigate('sample')">Sample</button>
        <button onclick="navigate('batch')">Batch</button>
        <button onclick="navigate('users')">Users</button>
    </div>
    <button class="logout" onclick="navigateToLogout()">Log out</button>
</div>

<style>
    .navbar {
        position: fixed; /* Закрепляем сверху */
        top: 0;
        left: 0;
        width: 98%; /* Панель на всю ширину экрана */
        z-index: 1000; /* Панель поверх других элементов */
        display: flex;
        align-items: center;
        background-color: #333;
        padding: 10px 1%; /* Пространство внутри панели */
        color: white;
        box-shadow: 0px 2px 5px rgba(0, 0, 0, 0.2); /* Тень под панелью */
    }

    .nav-buttons {
        display: flex;
        gap: 10px; /* Расстояние между кнопками */
    }

    .navbar button {
        background-color: #444;
        color: white;
        border: none;
        padding: 10px 15px;
        cursor: pointer;
        font-size: 14px;
        border-radius: 5px;
    }

    .navbar button:hover {
        background-color: #555;
    }

     .navbar .logout {
        margin-left: auto; /* Сдвиг кнопки Log out вправо */
        background-color: #d9534f;
    }

     .navbar .logout:hover {
        background-color: #c9302c;
    }

    /* Контент */
    body {
        padding-top: 60px; /* Отступ равный высоте панели */
        margin: 0;
    }

    /* Адаптивный дизайн */
    @media (max-width: 768px) {
        .navbar {
            flex-wrap: wrap; /* Разрешаем перенос содержимого */
        }
        .nav-buttons {
            justify-content: center; /* Центрируем кнопки */
            flex-wrap: wrap; /* Перенос кнопок на новую строку */
        }
        .logout {
            margin-left: 0; /* Убираем смещение вправо на маленьких экранах */
            margin-top: 10px; /* Отступ сверху для кнопки Log out */
            width: 100%; /* Кнопка растягивается на всю ширину */
            text-align: center;
        }
    }
</style>

<script>
    function navigate(page) {
        window.location.href = '<%= request.getContextPath() %>/' + page;
    }

    function navigateToLogout() {
        window.location.href = '<%= request.getContextPath() %>/logout';
    }
</script>

