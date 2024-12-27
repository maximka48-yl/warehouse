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
import ru.vsu.strelnikov_m_i.repositories.database_connected.UserRepository;
import ru.vsu.strelnikov_m_i.repositories.filters.EntryFilter;
import ru.vsu.strelnikov_m_i.services.BatchService;
import ru.vsu.strelnikov_m_i.services.EntryService;
import ru.vsu.strelnikov_m_i.services.UserService;
import ru.vsu.strelnikov_m_i.utils.WebUtils;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/entry")
public class EntryServlet extends HttpServlet {
    private EntryService entryService;
    private UserService userService;
    private BatchService batchService;
    private EntryFilter entryFilter;

    @Override
    public void init(ServletConfig config) throws ServletException {
        entryService = new EntryService(new EntryRepository(), new BatchRepository());
        batchService = new BatchService(new BatchRepository());
        userService = new UserService(new UserRepository());
        entryFilter = new EntryFilter();
    }

    private List<Entry> getEntries(User user, int currentPage) {
        List<Entry> entries;
        if (user.getRole() == RoleType.ADMIN) {
            entries = entryService.getAll(currentPage, entryFilter);
        } else {
            entries = entryService.getByAuthor(user.getId(), currentPage, entryFilter);
        }
        return entries;
    }

    private int getEntriesCount(User user) {
        if (user.getRole() == RoleType.ADMIN) {
            return entryService.getTotal(entryFilter);
        } else {
            return entryService.getTotalByAuthor(user.getId(), entryFilter);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/auth");
            return;
        }
        User user = (User) session.getAttribute("user");
        int currentPageInt;
        try {
            currentPageInt = Integer.parseInt(req.getParameter("currentPage"));
        } catch (RuntimeException e) {
            resp.sendRedirect(req.getRequestURI() + "?currentPage=1");
            return;
        }

        entryFilter.setFilters(new String[] {
                req.getParameter("entryType"),
                req.getParameter("batchId"),
                req.getParameter("userId"),
                req.getParameter("date")
        });

        if (user.getRole() == RoleType.ADMIN) {
            req.setAttribute("total-pages", entryService.getTotalPages(entryFilter));
            req.setAttribute("navbarFile", "navbarAdmin.jsp");
        } else {
            req.setAttribute("total-pages", entryService.getTotalPagesByAuthor(user.getId(), entryFilter));
            req.setAttribute("navbarFile", "navbarManager.jsp");
        }
        try {
            req.setAttribute("batch-ids", batchService.getAllIds());
            req.setAttribute("user-ids", getUsers(user));
            req.setAttribute("totalRecords", getEntriesCount(user));
            req.setAttribute("entries", getEntries(user, currentPageInt));
        } catch (RuntimeException e) {
            req.setAttribute("error", e.getMessage());
            resp.sendRedirect(req.getRequestURI() + "?currentPage=1");
            return;
        }
        req.getRequestDispatcher("jsps/entry.jsp").forward(req, resp);
    }

    private List<Integer> getUsers(User user) {
        List<Integer> users;
        if (user.getRole() == RoleType.ADMIN) {
            users = userService.getAllIds();
        } else {
            users = new ArrayList<>();
            users.add(user.getId());
        }
        return users;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        User user = (User) session.getAttribute("user");
        String action = WebUtils.sanitizeOutput(req.getParameter("action"));
        if (action != null) {
            try {
                actionParser(req, action, user);
            } catch (RuntimeException e) {
                req.setAttribute("error", e.getMessage());
            }
        }
        resp.sendRedirect(req.getContextPath() + buildFilterUrl(req));
    }

    private void actionParser(HttpServletRequest req, String action, User user) {
        if (action.equals("add")) {
            String entryType = WebUtils.sanitizeOutput(req.getParameter("addEntryType"));
            String batch = WebUtils.sanitizeOutput(req.getParameter("addBatch"));
            String date = WebUtils.sanitizeOutput(req.getParameter("addDate"));
            String amount = WebUtils.sanitizeOutput(req.getParameter("addAmount"));
            entryService.add(EntryType.valueOf(entryType), Integer.parseInt(batch), Date.valueOf(date), Integer.parseInt(amount), user);
        }
        if (action.equals("update")) {
            String id = WebUtils.sanitizeOutput(req.getParameter("updateId"));
            String entryType = WebUtils.sanitizeOutput(req.getParameter("updateEntryType"));
            String batch = WebUtils.sanitizeOutput(req.getParameter("updateBatch"));
            String date = WebUtils.sanitizeOutput(req.getParameter("updateDate"));
            String amount = WebUtils.sanitizeOutput(req.getParameter("updateAmount"));
            entryService.update(Integer.parseInt(id), EntryType.valueOf(entryType), Integer.parseInt(batch), Date.valueOf(date), Integer.parseInt(amount), user);
        }
        if (action.equals("delete")) {
            String id = WebUtils.sanitizeOutput(req.getParameter("deleteId"));
            entryService.delete(Integer.parseInt(id), user);
        }
    }

    private String buildFilterUrl(HttpServletRequest request) {
    String entryType = request.getParameter("entryType");
    String batchId = request.getParameter("batchId");
    String userId = request.getParameter("userId");
    String dateFilter = request.getParameter("dateFilter");
    String currentPage = request.getParameter("currentPage");

    StringBuilder redirectUrl = new StringBuilder("/entry");

    boolean firstParam = true;

    if (entryType != null) {
        redirectUrl.append("?").append("entryType=").append(entryType);
        firstParam = false;
    }
    if (batchId != null) {
        redirectUrl.append(firstParam ? "?" : "&").append("batchId=").append(batchId);
        firstParam = false;
    }
    if (userId != null) {
        redirectUrl.append(firstParam ? "?" : "&").append("userId=").append(userId);
        firstParam = false;
    }
    if (dateFilter != null) {
        redirectUrl.append(firstParam ? "?" : "&").append("dateFilter=").append(dateFilter);
        firstParam = false;
    }
    if (currentPage != null) {
        redirectUrl.append(firstParam ? "?" : "&").append("currentPage=").append(currentPage);
    } else {
        redirectUrl.append(firstParam ? "?" : "&").append("currentPage=1");
    }

    return redirectUrl.toString();
}
}
