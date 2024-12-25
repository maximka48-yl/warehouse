<%@ page import="ru.vsu.strelnikov_m_i.entities.User" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.vsu.strelnikov_m_i.enums.RoleType" %><%--
  Created by IntelliJ IDEA.
  User: strem
  Date: 24.12.2024
  Time: 20:24
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>City Table Management</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            margin: 0;
            padding: 20px;
            background-color: #f9f9f9;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }

        table, th, td {
            border: 1px solid #ccc;
        }

        th, td {
            padding: 10px;
            text-align: left;
        }

        tr.selected {
            background-color: #d0ebff;
        }

        .buttons {
            display: flex;
            gap: 10px;
        }

        button {
            padding: 10px 15px;
            font-size: 14px;
            cursor: pointer;
            border: none;
            border-radius: 5px;
        }

        button:disabled {
            background-color: #ccc;
            cursor: not-allowed;
        }

        .popup {
            display: none;
            position: fixed;
            top: 50%;
            left: 50%;
            transform: translate(-50%, -50%);
            background: #fff;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2);
            z-index: 1000;
        }

        .popup-header {
            margin-bottom: 10px;
            font-size: 18px;
            font-weight: bold;
        }

        .popup-buttons {
            margin-top: 20px;
            text-align: right;
        }

        .overlay {
            display: none;
            position: fixed;
            top: 0;
            left: 0;
            width: 100%;
            height: 100%;
            background: rgba(0, 0, 0, 0.5);
            z-index: 999;
        }

        .error-message {
            color: #ff0000;
            font-size: 14px;
            text-align: center;
            margin-bottom: 15px;
        }
    </style>
    <script>
        document.addEventListener("DOMContentLoaded", function () {
            const rows = document.querySelectorAll("tbody tr");
            const updateBtn = document.getElementById("updateBtn");
            const deleteBtn = document.getElementById("deleteBtn");

            let selectedRow = null;

            rows.forEach(row => {
                row.addEventListener("click", () => {
                    rows.forEach(r => r.classList.remove("selected"));
                    row.classList.add("selected");
                    selectedRow = row;
                    updateBtn.disabled = false;
                    deleteBtn.disabled = false;
                });
            });

            document.getElementById("addBtn").addEventListener("click", () => {
                showPopup("popupAdd");
            });

            updateBtn.addEventListener("click", () => {
                if (selectedRow) {
                    populateUpdatePopup(selectedRow);
                    showPopup("popupUpdate");
                }
            });

            deleteBtn.addEventListener("click", () => {
                if (selectedRow) {
                    populateDeletePopup(selectedRow);
                    showPopup("popupDelete");
                }
            });

            document.querySelectorAll(".close-popup").forEach(btn => {
                btn.addEventListener("click", () => {
                    closePopup();
                });
            });
        });

        function showPopup(popupId) {
            document.getElementById(popupId).style.display = "block";
            document.getElementById("overlay").style.display = "block";
        }

        function closePopup() {
            document.querySelectorAll(".popup").forEach(popup => popup.style.display = "none");
            document.getElementById("overlay").style.display = "none";
        }

        function populateUpdatePopup(row) {
            const cells = row.querySelectorAll("td");
            document.getElementById("updateId").value = cells[0].textContent;
            document.getElementById("updateFullName").value = cells[1].textContent;
            document.getElementById("updateEmail").value = cells[3].textContent;
            document.getElementById("updatePhoneNumber").value = cells[4].textContent;
            const roleType = cells[2].textContent;
            const roleTypeSelect = document.getElementById("updateRoleType");
            for (let option of roleTypeSelect.options) {
                option.selected = option.value === roleType;
            }
        }

        function populateDeletePopup(row) {
            const cells = row.querySelectorAll("td");
            document.getElementById("deleteId").value = cells[0].textContent;
            document.getElementById("deleteFullName").value = cells[1].textContent;
            document.getElementById("deleteEmail").value = cells[3].textContent;
            document.getElementById("deletePhoneNumber").value = cells[4].textContent;
            const roleType = cells[2].textContent;
            const roleTypeSelect = document.getElementById("deleteRoleType");
            for (let option of roleTypeSelect.options) {
                option.selected = option.value === roleType;
            }
        }
    </script>
</head>
<body>
<jsp:include page="${navbarFile}"/>
<div class="overlay" id="overlay"></div>
<table>
    <thead>
    <tr>
        <th>id</th>
        <th>FullName</th>
        <th>RoleType</th>
        <th>e-mail</th>
        <th>Phone Number</th>
    </tr>
    </thead>
    <tbody>
    <%
        List<User> users = (List<User>) request.getAttribute("users");
        for (User user : users) {
    %>
    <tr>
        <td><%= user.getId() %></td>
        <td><%= user.getFullName()%></td>
        <td><%= user.getRole().name() %></td>
        <td><%= user.getEmail()%></td>
        <td><%= user.getPhone()%></td>
    </tr>
    <% } %>
    </tbody>
</table>
<c:if test="${not empty error}">
    <div class="error-message">${error}</div>
</c:if>
<div class="buttons">

    <button id="addBtn">Add</button>
    <button id="updateBtn" disabled>Update</button>
    <button id="deleteBtn" disabled>Delete</button>
</div>

<!-- Add Popup -->
<div class="popup" id="popupAdd">
    <div class="popup-header">Add User</div>
    <form action="${pageContext.request.contextPath}/users" method="post">
        <label for="addFullName">Full Name:</label>
        <input type="text" name="addFullName" id="addFullName" required>
        <label for="addPassword">Password:</label>
        <input type="text" name="addPassword" id="addPassword" required>
        <label for="addRoleType">Role Type:</label>
        <select name="addRoleType" id="addRoleType">
            <%for (RoleType s : RoleType.values()) {%>
            <option>
                <%= s.name()%>
            </option>
            <%}%>
        </select>
        <label for="addEmail">E-mail:</label>
        <input type="email" name="addEmail" id="addEmail" required>
        <label for="addPhoneNumber">Phone Number:</label>
        <input type="tel" name="addPhoneNumber" id="addPhoneNumber" required pattern="\+?[0-9]{1,4}?[-.\s]?[0-9]{1,15}">
        <div class="popup-buttons">
            <button type="button" class="close-popup">Cancel</button>
            <button type="submit" name="action" value="add">OK</button>
        </div>
    </form>
</div>

<!-- Update Popup -->
<div class="popup" id="popupUpdate">
    <div class="popup-header">Update User</div>
    <form action="${pageContext.request.contextPath}/users" method="post">
        <label for="updateId">Full Name:</label>
        <input type="text" name="updateId" id="updateId" required readonly>

        <label for="updateFullName">Full Name:</label>
        <input type="text" name="updateFullName" id="updateFullName" required>

        <label for="updatePassword">Password:</label>
        <input type="text" name="updatePassword" id="updatePassword" placeholder="Leave blank to keep current">

        <label for="updateRoleType">Role Type:</label>
        <select name="updateRoleType" id="updateRoleType">
            <% for (RoleType s : RoleType.values()) { %>
            <option>
                <%= s.name() %>
            </option>
            <% } %>
        </select>

        <label for="updateEmail">E-mail:</label>
        <input type="email" name="updateEmail" id="updateEmail" required>

        <label for="updatePhoneNumber">Phone Number:</label>
        <input type="tel" name="updatePhoneNumber" id="updatePhoneNumber" required
               pattern="\+?[0-9]{1,4}?[-.\s]?[0-9]{1,15}">

        <div class="popup-buttons">
            <button type="button" class="close-popup">Cancel</button>
            <button type="submit" name="action" value="update">Update</button>
        </div>
    </form>
</div>

<!-- Delete Popup -->
<div class="popup" id="popupDelete">
    <div class="popup-header">Are you sure?</div>
    <form action="${pageContext.request.contextPath}/users" method="post">
        <label for="deleteId">Full Name:</label>
        <input type="text" name="deleteId" id="deleteId" required readonly>

        <label for="deleteFullName">Full Name:</label>
        <input type="text" name="deleteFullName" id="deleteFullName" value="User Name" readonly>

        <label for="deleteRoleType">Role Type:</label>
        <select style="pointer-events: none;" name="deleteRoleType" id="deleteRoleType">
            <% for (RoleType s : RoleType.values()) { %>
            <option>
                <%= s.name() %>
            </option>
            <% } %>
        </select>

        <label for="deleteEmail">E-mail:</label>
        <input type="email" name="deleteEmail" id="deleteEmail" value="user@example.com" readonly>

        <label for="deletePhoneNumber">Phone Number:</label>
        <input type="tel" name="deletePhoneNumber" id="deletePhoneNumber" value="+1234567890" readonly
               pattern="\+?[0-9]{1,4}?[-.\s]?[0-9]{1,15}">

        <div class="popup-buttons">
            <button type="button" class="close-popup">Cancel</button>
            <button type="submit" name="action" value="delete">Delete</button>
        </div>
    </form>
</div>
</body>
</html>
