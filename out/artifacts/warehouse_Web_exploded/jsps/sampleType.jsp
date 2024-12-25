<%@ page import="ru.vsu.strelnikov_m_i.entities.SampleType" %>
<%@ page import="java.util.List" %><%--
  Created by IntelliJ IDEA.
  User: strem
  Date: 23.12.2024
  Time: 18:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>SampleType Table Management</title>
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
            document.getElementById("updateName").value = cells[1].textContent;
        }

        function populateDeletePopup(row) {
            const cells = row.querySelectorAll("td");
            document.getElementById("deleteId").value = cells[0].textContent;
            document.getElementById("deleteName").value = cells[1].textContent;
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
                <th>Name</th>
            </tr>
        </thead>
        <tbody>
            <%
            List<SampleType> sampleTypes = (List<SampleType>) request.getAttribute("sampleTypes");
            for (SampleType sampleType : sampleTypes) {
            %>
            <tr>
                <td><%= sampleType.getId() %></td>
                <td><%= sampleType.getName() %></td>
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
        <div class="popup-header">Add SampleType</div>
        <form action="${pageContext.request.contextPath}/sampleType" method="post">
            <label>Name:</label>
            <input type="text" name="addName" required>
            <div class="popup-buttons">
                <button type="button" class="close-popup">Cancel</button>
                <button type="submit" name="action" value="add">OK</button>
            </div>
        </form>
    </div>

    <!-- Update Popup -->
    <div class="popup" id="popupUpdate">
        <div class="popup-header">Update SampleType</div>
        <form action="${pageContext.request.contextPath}/sampleType" method="post">
            <label>id:</label>
            <input type="text" name="updateId" id="updateId" readonly>
            <label>Name:</label>
            <input type="text" name="updateName" id="updateName" required>
            <div class="popup-buttons">
                <button type="button" class="close-popup">Cancel</button>
                <button type="submit" name="action" value="update">OK</button>
            </div>
        </form>
    </div>

    <!-- Delete Popup -->
    <div class="popup" id="popupDelete">
        <div class="popup-header">Are you sure?</div>
        <form action="${pageContext.request.contextPath}/sampleType" method="post">
            <label>id:</label>
            <input type="text" name="deleteId" id="deleteId" readonly>
            <label>Name:</label>
            <input type="text" name="deleteName" id="deleteName" readonly>
            <div class="popup-buttons">
                <button type="button" class="close-popup">Cancel</button>
                <button type="submit" name="action" value="delete">OK</button>
            </div>
        </form>
    </div>
</body>
</html>
