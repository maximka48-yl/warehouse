package ru.vsu.strelnikov_m_i.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.vsu.strelnikov_m_i.entities.Entry;
import ru.vsu.strelnikov_m_i.entities.User;
import ru.vsu.strelnikov_m_i.enums.EntryType;
import ru.vsu.strelnikov_m_i.enums.RoleType;
import ru.vsu.strelnikov_m_i.repositories.database_connected.BatchRepository;
import ru.vsu.strelnikov_m_i.repositories.database_connected.EntryRepository;
import ru.vsu.strelnikov_m_i.services.BatchService;
import ru.vsu.strelnikov_m_i.services.EntryService;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

@WebServlet("/entry")
public class EntryServlet extends HttpServlet {
    private EntryService entryService;
    private BatchService batchService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        entryService = new EntryService(new EntryRepository(), new BatchRepository());
        batchService = new BatchService(new BatchRepository());
    }

    private List<Entry> getEntries(User user) {
        List<Entry> entries;
        if (user.getRole() == RoleType.ADMIN) {
            entries = entryService.getAll();
        } else {
            entries = entryService.getByAuthor(user.getId());
        }
        return entries;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/auth");
            return;
        }
        User user = (User) session.getAttribute("user");
        if (user.getRole() == RoleType.ADMIN) {
            req.setAttribute("navbarFile", "navbarAdmin.jsp");
        } else {
            req.setAttribute("navbarFile", "navbarManager.jsp");
        }
        req.setAttribute("batch-ids", batchService.getAllIds());
        req.setAttribute("users", getEntries(user));
        req.getRequestDispatcher("jsps/entry.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        String action = req.getParameter("action");
        if (action.equals("add")) {
            String entryType = req.getParameter("addEntryType");
            String batch = req.getParameter("addBatch");
            String date = req.getParameter("addDate");
            String amount = req.getParameter("addAmount");
            try {
                entryService.add(EntryType.valueOf(entryType), Integer.parseInt(batch), Date.valueOf(date), Integer.parseInt(amount), user);
            } catch (RuntimeException e) {
                req.setAttribute("error", e.getMessage());
            }
        }
        if (action.equals("update")) {
            String id = req.getParameter("updateId");
            String entryType = req.getParameter("updateEntryType");
            String batch = req.getParameter("updateBatch");
            String date = req.getParameter("updateDate");
            String amount = req.getParameter("updateAmount");
            try {
                entryService.update(Integer.parseInt(id), EntryType.valueOf(entryType), Integer.parseInt(batch), Date.valueOf(date), Integer.parseInt(amount), user);
            } catch (RuntimeException e) {
                req.setAttribute("error", e.getMessage());
            }
        }
        if (action.equals("delete")) {
            String id = req.getParameter("deleteId");
            try {
                entryService.delete(Integer.parseInt(id), user);
            } catch (RuntimeException e) {
                req.setAttribute("error", e.getMessage());
            }
        }
        if (user.getRole() == RoleType.ADMIN) {
            req.setAttribute("navbarFile", "navbarAdmin.jsp");
        } else {
            req.setAttribute("navbarFile", "navbarManager.jsp");
        }
        req.setAttribute("batch-ids", batchService.getAllIds());
        req.setAttribute("users", getEntries(user));
        req.getRequestDispatcher("jsps/entry.jsp").forward(req, resp);
    }

}
