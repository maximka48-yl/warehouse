package ru.vsu.strelnikov_m_i.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.vsu.strelnikov_m_i.entities.Manufacture;
import ru.vsu.strelnikov_m_i.entities.Sample;
import ru.vsu.strelnikov_m_i.entities.User;
import ru.vsu.strelnikov_m_i.enums.RoleType;
import ru.vsu.strelnikov_m_i.repositories.database_connected.ManufactureRepository;
import ru.vsu.strelnikov_m_i.repositories.database_connected.SampleRepository;
import ru.vsu.strelnikov_m_i.repositories.database_connected.SampleTypeRepository;
import ru.vsu.strelnikov_m_i.services.ManufactureService;
import ru.vsu.strelnikov_m_i.services.SampleService;
import ru.vsu.strelnikov_m_i.services.SampleTypeService;
import ru.vsu.strelnikov_m_i.utils.WebUtils;

import java.io.IOException;
import java.util.List;

@WebServlet("/sample")
public class SampleServlet extends HttpServlet {
    ManufactureService manufactureService;
    SampleTypeService sampleTypeService;
    SampleService sampleService;

    @Override
    public void init(ServletConfig config) throws ServletException {
        manufactureService = new ManufactureService(new ManufactureRepository());
        sampleTypeService = new SampleTypeService(new SampleTypeRepository());
        sampleService = new SampleService(new SampleRepository());
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
        req.setAttribute("samples", sampleService.getAll());
        req.setAttribute("sample-type-names", sampleTypeService.getAllNames());
        req.setAttribute("manufacture-names", manufactureService.getAllNames());
        req.getRequestDispatcher("jsps/sample.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = WebUtils.sanitizeOutput(req.getParameter("action"));
        if (action.equals("add")) {
            String name = WebUtils.sanitizeOutput(req.getParameter("addName"));
            String sampleTypeName = WebUtils.sanitizeOutput(req.getParameter("addSampleTypeName"));
            String manufactureName = WebUtils.sanitizeOutput(req.getParameter("addManufactureName"));
            try {
                sampleService.add(name, sampleTypeName, manufactureName);
            } catch (RuntimeException e) {
                req.setAttribute("error", e.getMessage());
            }
        }
        if (action.equals("update")) {
            String id = WebUtils.sanitizeOutput(req.getParameter("updateId"));
            String name = WebUtils.sanitizeOutput(req.getParameter("updateName"));
            String sampleTypeName = WebUtils.sanitizeOutput(req.getParameter("updateSampleTypeName"));
            String manufactureName = WebUtils.sanitizeOutput(req.getParameter("updateManufactureName"));
            try {
                sampleService.update(Integer.parseInt(id), name, sampleTypeName, manufactureName);
            } catch (RuntimeException e) {
                req.setAttribute("error", e.getMessage());
            }
        }
        if (action.equals("delete")) {
            String id = WebUtils.sanitizeOutput(req.getParameter("deleteId"));
            try {
                sampleService.delete(Integer.parseInt(id));
            } catch (RuntimeException e) {
                req.setAttribute("error", e.getMessage());
            }
        }
        req.setAttribute("navbarFile", "navbarAdmin.jsp");
        req.setAttribute("samples", sampleService.getAll());
        req.setAttribute("sample-type-names", sampleTypeService.getAllNames());
        req.setAttribute("manufacture-names", manufactureService.getAllNames());
        req.getRequestDispatcher("jsps/sample.jsp").forward(req, resp);
    }
}
