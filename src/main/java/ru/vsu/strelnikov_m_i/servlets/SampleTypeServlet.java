package ru.vsu.strelnikov_m_i.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.vsu.strelnikov_m_i.entities.SampleType;
import ru.vsu.strelnikov_m_i.entities.User;
import ru.vsu.strelnikov_m_i.enums.RoleType;
import ru.vsu.strelnikov_m_i.repositories.database_connected.SampleTypeRepository;
import ru.vsu.strelnikov_m_i.services.SampleTypeService;

import java.io.IOException;
import java.util.List;

@WebServlet("/sampleType")
public class SampleTypeServlet extends HttpServlet {
    private SampleTypeService sampleTypeService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        sampleTypeService = new SampleTypeService(new SampleTypeRepository());
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
        List<SampleType> sampleTypes = sampleTypeService.getAll();
        req.setAttribute("sampleTypes", sampleTypes);
        req.getRequestDispatcher("jsps/sampleType.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter("action");
        if (action.equals("add")) {
            String typeName = req.getParameter("addName");
            try {
                sampleTypeService.add(typeName);
            } catch (RuntimeException e) {
                req.setAttribute("error", e.getMessage());
            }
        }
        if (action.equals("update")) {
            String typeId = req.getParameter("updateId");
            String typeName = req.getParameter("updateName");
            try {
                sampleTypeService.update(Integer.parseInt(typeId), typeName);
            } catch (RuntimeException e) {
                req.setAttribute("error", e.getMessage());
            }
        }
        if (action.equals("delete")) {
            String typeId = req.getParameter("deleteId");
            try {
                sampleTypeService.delete(Integer.parseInt(typeId));
            } catch (RuntimeException e) {
                req.setAttribute("error", e.getMessage());
            }
        }
        req.setAttribute("navbarFile", "navbarAdmin.jsp");
        List<SampleType> sampleTypes = sampleTypeService.getAll();
        req.setAttribute("sampleTypes", sampleTypes);
        req.getRequestDispatcher("jsps/sampleType.jsp").forward(req, resp);
    }

}
