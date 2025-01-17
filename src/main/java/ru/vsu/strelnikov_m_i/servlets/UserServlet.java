package ru.vsu.strelnikov_m_i.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.vsu.strelnikov_m_i.entities.User;
import ru.vsu.strelnikov_m_i.enums.RoleType;
import ru.vsu.strelnikov_m_i.repositories.database_connected.UserRepository;
import ru.vsu.strelnikov_m_i.services.UserService;
import ru.vsu.strelnikov_m_i.utils.WebUtils;

import java.io.IOException;

@WebServlet("/users")
public class UserServlet extends HttpServlet {
    private UserService userService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        userService = new UserService(new UserRepository());
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session.getAttribute("user") == null) {
            resp.sendRedirect(req.getContextPath() + "/auth");
            return;
        }
        if (((User) session.getAttribute("user")).getRole() != RoleType.ADMIN) {
            resp.sendRedirect(req.getContextPath() + "/entry");
            return;
        }
        req.setAttribute("navbarFile", "navbarAdmin.jsp");
        req.setAttribute("users", userService.getAll());
        req.getRequestDispatcher("jsps/users.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = WebUtils.sanitizeOutput(req.getParameter("action"));
        if (action.equals("add")) {
            String userFullName = WebUtils.sanitizeOutput(req.getParameter("addFullName"));
            String userEmail = WebUtils.sanitizeOutput(req.getParameter("addEmail"));
            String userPassword = WebUtils.sanitizeOutput(req.getParameter("addPassword"));
            String userPhone = WebUtils.sanitizeOutput(req.getParameter("addPhoneNumber"));
            String userRoleType = WebUtils.sanitizeOutput(req.getParameter("addRoleType"));
            try {
                userService.add(userFullName, userPassword, RoleType.valueOf(userRoleType), userEmail, userPhone);
            } catch (RuntimeException e) {
                req.setAttribute("error", e.getMessage());
            }
        }
        if (action.equals("update")) {
            String userId = WebUtils.sanitizeOutput(req.getParameter("updateId"));
            String userFullName = WebUtils.sanitizeOutput(req.getParameter("updateFullName"));
            String userEmail = WebUtils.sanitizeOutput(req.getParameter("updateEmail"));
            String userPassword = WebUtils.sanitizeOutput(req.getParameter("updatePassword"));
            String userPhone = WebUtils.sanitizeOutput(req.getParameter("updatePhoneNumber"));
            String userRoleType = WebUtils.sanitizeOutput(req.getParameter("updateRoleType"));
            try {
                userService.update(Integer.parseInt(userId), userFullName, userPassword, RoleType.valueOf(userRoleType), userEmail, userPhone);
            } catch (RuntimeException e) {
                req.setAttribute("error", e.getMessage());
            }
        }
        if (action.equals("delete")) {
            String id = WebUtils.sanitizeOutput(req.getParameter("deleteId"));
            try {
                userService.delete(Integer.parseInt(id), ((User) req.getSession().getAttribute("user")).getId());
            } catch (RuntimeException e) {
                req.setAttribute("error", e.getMessage());
            }
        }
        req.setAttribute("navbarFile", "navbarAdmin.jsp");
        req.setAttribute("users", userService.getAll());
        req.getRequestDispatcher("jsps/users.jsp").forward(req, resp);
    }
}
