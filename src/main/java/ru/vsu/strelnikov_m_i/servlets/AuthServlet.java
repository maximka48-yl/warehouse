package ru.vsu.strelnikov_m_i.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.vsu.strelnikov_m_i.entities.User;
import ru.vsu.strelnikov_m_i.repositories.database_connected.UserRepository;
import ru.vsu.strelnikov_m_i.services.AuthService;
import ru.vsu.strelnikov_m_i.utils.WebUtils;

import java.io.IOException;

@WebServlet("/auth")
public class AuthServlet extends HttpServlet {
    private AuthService authService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        authService = new AuthService(new UserRepository());
        super.init(config);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        HttpSession session = req.getSession();
        if (session.getAttribute("user") == null) {
            req.getRequestDispatcher("jsps/auth.jsp").forward(req, resp);
            return;
        }
        resp.sendRedirect(req.getContextPath() + "/entry?currentPage=1");
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            User user = authService.authorization(Integer.parseInt(WebUtils.sanitizeOutput(req.getParameter("username"))), WebUtils.sanitizeOutput(req.getParameter("password")));
            HttpSession session = req.getSession();
            session.setAttribute("user", user);
            resp.sendRedirect(req.getContextPath() + "/entry?currentPage=1");
        } catch (RuntimeException e) {
            req.getSession().setAttribute("error", e.getMessage());
            req.getRequestDispatcher("jsps/auth.jsp").forward(req, resp);
        }
    }
}
