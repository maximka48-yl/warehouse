<%@ page import="ru.vsu.strelnikov_m_i.entities.Entry" %>
<%@ page import="java.util.List" %>
<%@ page import="ru.vsu.strelnikov_m_i.enums.EntryType" %><%--
  Created by IntelliJ IDEA.
  User: strem
  Date: 22.12.2024
  Time: 1:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Table Management</title>
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

        /* Стили для стрелок и номера страницы */
        #footer {
            display: flex;
            justify-content: center;
            align-items: center;
            margin-top: 20px;
            gap: 20px;
        }

        #footer button {
            background-color: #007bff; /* Синий цвет */
            color: white;
            padding: 10px 15px;
            font-size: 16px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
            transition: background-color 0.3s ease, transform 0.2s ease;
        }

        #footer button:hover {
            background-color: #0056b3; /* Темно-синий при наведении */
            transform: scale(1.1); /* Легкое увеличение при наведении */
        }

        #footer button:disabled {
            background-color: #ccc;
            cursor: not-allowed;
        }

        #footer #pageInfo {
            font-size: 16px;
            font-weight: bold;
            color: #333;
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

        /* Общие стили для формы */
    .filter-form {
        display: flex;
        flex-wrap: wrap;
        gap: 15px;
        margin-bottom: 20px;
        background-color: #fff;
        padding: 20px;
        border-radius: 8px;
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
    }

    .filter-form label {
        font-size: 14px;
        color: #555;
        margin-right: 10px;
    }

    .filter-form select,
    .filter-form input[type="date"] {
        padding: 8px;
        font-size: 14px;
        border: 1px solid #ccc;
        border-radius: 5px;
        width: 200px;
        max-width: 100%;
        background-color: #f9f9f9;
        transition: border-color 0.3s ease;
    }

    .filter-form select:focus,
    .filter-form input[type="date"]:focus {
        border-color: #007bff;
        outline: none;
    }

    .filter-form button {
        padding: 10px 20px;
        font-size: 14px;
        background-color: #007bff;
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        transition: background-color 0.3s ease, transform 0.2s ease;
    }

    .filter-form button:hover {
        background-color: #0056b3;
        transform: scale(1.05);
    }

    .filter-form button:disabled {
        background-color: #ccc;
        cursor: not-allowed;
    }

    .record-count {
        font-size: 14px;
        color: #777;  /* Серый цвет текста */
        text-align: right;  /* Выравнивание текста по правому краю */
        margin-top: 10px;
    }

    /* Стили для кнопки очистки фильтров */
    #clearFiltersBtn {
        background-color: #f44336;
        border-radius: 5px;
        font-size: 14px;
        padding: 8px 15px;
        cursor: pointer;
        color: white;
        transition: background-color 0.3s ease, transform 0.2s ease;
    }

    #clearFiltersBtn:hover {
        background-color: #d32f2f;
        transform: scale(1.05);
    }

    #clearFiltersBtn:disabled {
        background-color: #ccc;
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

        function clearFilters() {
            document.getElementById("entryTypeFilter").value = "";
            document.getElementById("batchFilter").value = "";
            document.getElementById("userFilter").value = "";
            document.getElementById("dateFilter").value = "";

            // Отправить форму для обновления страницы
            document.querySelector(".filter-form form").submit();
        }

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
            const entryType = cells[1].textContent;
            const entryTypeSelect = document.getElementById("updateEntryType");
            for (let option of entryTypeSelect.options) {
                option.selected = option.value === entryType;
            }
            const batch = cells[2].textContent;
            const batchSelect = document.getElementById("updateEntryType");
            for (let option of batchSelect.options) {
                option.selected = option.value === batch;
            }
            document.getElementById("updateUser").value = cells[3].textContent;
            document.getElementById("updateDate").value = cells[4].textContent;
            document.getElementById("updateAmount").value = cells[5].textContent;
        }

        function populateDeletePopup(row) {
            const cells = row.querySelectorAll("td");
            document.getElementById("deleteId").value = cells[0].textContent;
            const entryType = cells[1].textContent;
            const entryTypeSelect = document.getElementById("deleteEntryType");
            for (let option of entryTypeSelect.options) {
                option.selected = option.value === entryType;
            }
            const batch = cells[2].textContent;
            const batchSelect = document.getElementById("deleteEntryType");
            for (let option of batchSelect.options) {
                option.selected = option.value === batch;
            }
            document.getElementById("deleteUser").value = cells[3].textContent;
            document.getElementById("deleteDate").value = cells[4].textContent;
            document.getElementById("deleteAmount").value = cells[5].textContent;
        }


    </script>
</head>
<body>
<jsp:include page="${navbarFile}"/>
<div class="filter-form">
    <form method="get" action="${pageContext.request.contextPath}/entry">
        <input type="hidden" name="currentPage" value="1">
        <label for="entryTypeFilter">Entry Type:</label>
        <select name="entryType" id="entryTypeFilter">
            <option value="">All</option>
            <% for (EntryType entryType : EntryType.values()) { %>
                <option value="<%= entryType.name() %>" <%= request.getParameter("entryType") != null && request.getParameter("entryType").equals(entryType.name()) ? "selected" : "" %>>
                    <%= entryType.name() %>
                </option>
            <% } %>
        </select>

        <label for="batchFilter">Batch:</label>
        <select name="batchId" id="batchFilter">
            <option value="">All</option>
            <%List<Integer> ids = (List<Integer>) request.getAttribute("batch-ids");
                for (int s : ids) { %>
                <option value="<%= s %>" <%= request.getParameter("batchId") != null && request.getParameter("batchId").equals(String.valueOf(s)) ? "selected" : "" %>>
                    <%= s %>
                </option>
            <% } %>
        </select>

        <label for="userFilter">User:</label>
        <select name="userId" id="userFilter">
            <option value="">All</option>
            <%List<Integer> users = (List<Integer>) request.getAttribute("user-ids");
                for (int s : users) { %>
                <option value="<%= s %>" <%= request.getParameter("userId") != null && request.getParameter("userId").equals(String.valueOf(s)) ? "selected" : "" %>>
                    <%= s %>
                </option>
            <% } %>
        </select>

        <label for="dateFilter">Date:</label>
        <input type="date" name="date" id="dateFilter" value="<%= request.getParameter("date") == null ? "" : request.getParameter("date") %>">

        <button type="submit">Apply Filters</button>
        <button type="button" id="clearFiltersBtn" onclick="clearFilters()">Clear Filters</button>
    </form>

    <div class="record-count">
        <span>Записей найдено: ${totalRecords}</span>
    </div>
</div>
<div class="overlay" id="overlay"></div>
<table>
    <thead>
    <tr>
        <th>id</th>
        <th>EntryType</th>
        <th>batch</th>
        <th>user</th>
        <th>date</th>
        <th>amount</th>
    </tr>
    </thead>
    <tbody>
    <%
        List<Entry> entries = (List<Entry>) request.getAttribute("entries");
        for (Entry entry : entries) {
    %>
    <tr>
        <td><%= entry.getId() %></td>
        <td><%= entry.getEntryType().name() %></td>
        <td><%= entry.getBatchId() %></td>
        <td><%= entry.getUserId() %></td>
        <td><%= entry.getDate() %></td>
        <td><%= entry.getAmount() %></td>
    </tr>
    <% } %>
    </tbody>
</table>
<div id="footer">
    <div class="buttons">
        <form method="get" action="${pageContext.request.contextPath}/entry">
            <input type="hidden" name="entryType" value="<%= request.getParameter("entryType") == null ? "" : request.getParameter("entryType") %>">
            <input type="hidden" name="batchId" value="<%= request.getParameter("batchId") == null ? "" : request.getParameter("batchId") %>">
            <input type="hidden" name="userId" value="<%= request.getParameter("userId") == null ? "" : request.getParameter("userId") %>">
            <input type="hidden" name="date" value="<%= request.getParameter("date") == null ? "" : request.getParameter("date") %>">

            <%int currentPage = Integer.parseInt(request.getParameter("currentPage"));
            int totalPages = (Integer) request.getAttribute("total-pages");%>
            <button type="submit" name="currentPage" value="<%= currentPage - 1 %>" <%= currentPage == 1 ? "disabled" : "" %>>&lt;</button>
            <span id="pageInfo"><%= currentPage %> / <%= totalPages %></span>
            <button type="submit" name="currentPage" value="<%= currentPage + 1 %>" <%= currentPage == totalPages ? "disabled" : "" %>>&gt;</button>
        </form>
    </div>
</div>
<div class="error-message">
        <%= request.getAttribute("error") != null ? request.getAttribute("error") : "" %>
</div>
<div class="buttons">
    <button id="addBtn">Add</button>
    <button id="updateBtn" disabled>Update</button>
    <button id="deleteBtn" disabled>Delete</button>
</div>

<!-- Add Popup -->
<div class="popup" id="popupAdd">
    <div class="popup-header">Add Entry</div>
    <form action="${pageContext.request.contextPath}/entry" method="post">
        <label>EntryType:</label>
        <select name="addEntryType" id="addEntryType">
            <%
                for (EntryType entryType : EntryType.values()) {
            %>
            <option>
                <%= entryType.name()%>
            </option>
            <%}%>
        </select>
        <label>Batch:</label>
        <select name="addBatch" id="addBatch">
            <%
                for (int s : ids) {
            %>
            <option>
                <%= s%>
            </option>
            <%}%>
        </select>
        <label>Date:</label>
        <input type="date" name="addDate" id="addDate" required>
        <label>Amount:</label>
        <input type="number" name="addAmount" id="addAmount" required>
        <div class="popup-buttons">
            <button type="button" class="close-popup">Cancel</button>
            <button type="submit" name="action" value="add">OK</button>
        </div>
    </form>
</div>

<!-- Update Popup -->
<div class="popup" id="popupUpdate">
    <div class="popup-header">Update Entry</div>
    <form action="${pageContext.request.contextPath}/entry" method="post">
        <label>id:</label>
        <input type="text" name="updateId" id="updateId" readonly>
        <label>User:</label>
        <input type="text" name="updateUser" id="updateUser" required readonly>
        <label>EntryType:</label>
        <select name="updateEntryType" id="updateEntryType">
            <%
                for (EntryType entryType : EntryType.values()) {
            %>
            <option>
                <%= entryType.name()%>
            </option>
            <%}%>
        </select>
        <label>Batch:</label>
        <select name="updateBatch" id="updateBatch">
            <%
                for (int s : ids) {
            %>
            <option>
                <%= s%>
            </option>
            <%}%>
        </select>
        </select>
        <label>Date:</label>
        <input type="date" name="updateDate" id="updateDate" required>
        <label>Amount:</label>
        <input type="number" name="updateAmount" id="updateAmount" required>
        <div class="popup-buttons">
            <button type="button" class="close-popup">Cancel</button>
            <button type="submit" name="action" value="update">OK</button>
        </div>
    </form>
</div>

<!-- Delete Popup -->
<div class="popup" id="popupDelete">
    <div class="popup-header">Are you sure?</div>
    <form action="${pageContext.request.contextPath}/entry" method="post">
        <label>id:</label>
        <input type="text" name="deleteId" id="deleteId" readonly>
        <label>EntryType:</label>
        <select style="pointer-events: none;" name="deleteEntryType" id="deleteEntryType">
            <%
                for (EntryType entryType : EntryType.values()) {
            %>
            <option>
                <%= entryType.name()%>
            </option>
            <%}%>
        </select>
        <label>User:</label>
        <input type="text" name="deleteId" id="deleteUser" readonly>
        <label>Batch:</label>
        <select style="pointer-events: none;" name="deleteBatch" id="deleteBatch">
            <%for (int s : ids) {%>
            <option>
                <%= s%>
            </option>
            <%}%>
        </select>
        <label for="deleteDate">Date:</label>
        <input type="date" name="deleteDate" id="deleteDate" readonly>
        <label for="deleteAmount">Amount:</label>
        <input type="text" name="deleteAmount" id="deleteAmount" readonly>
        <div class="popup-buttons">
            <button type="button" class="close-popup">Cancel</button>
            <button type="submit" name="action" value="delete">OK</button>
        </div>
    </form>
</div>
</body>
</html>

