package ru.vsu.strelnikov_m_i.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.vsu.strelnikov_m_i.entities.City;
import ru.vsu.strelnikov_m_i.entities.Manufacture;
import ru.vsu.strelnikov_m_i.entities.User;
import ru.vsu.strelnikov_m_i.enums.RoleType;
import ru.vsu.strelnikov_m_i.repositories.database_connected.CityRepository;
import ru.vsu.strelnikov_m_i.repositories.database_connected.ManufactureRepository;
import ru.vsu.strelnikov_m_i.services.CityService;
import ru.vsu.strelnikov_m_i.services.ManufactureService;
import ru.vsu.strelnikov_m_i.utils.WebUtils;

import java.io.IOException;
import java.util.List;

@WebServlet("/manufacture")
public class ManufactureServlet extends HttpServlet {
    private ManufactureService manufactureService;
    private CityService cityService;
    

    @Override
    public void init(ServletConfig config) throws ServletException {
        manufactureService = new ManufactureService(new ManufactureRepository());
        cityService = new CityService(new CityRepository());
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
        req.setAttribute("city-names", cityService.getAll());
        req.setAttribute("navbarFile", "navbarAdmin.jsp");
        List<Manufacture> manufactures = manufactureService.getAll();
        req.setAttribute("manufactures", manufactures);
        req.getRequestDispatcher("jsps/manufacture.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = WebUtils.sanitizeOutput(req.getParameter("action"));
        if (action.equals("add")) {
            String name = WebUtils.sanitizeOutput(req.getParameter("addName"));
            String cityName = WebUtils.sanitizeOutput(req.getParameter("addCityName"));
            try {
                manufactureService.add(name, cityName);
            } catch (RuntimeException e) {
                req.setAttribute("error", e.getMessage());
            }
        }
        if (action.equals("update")) {
            String id = WebUtils.sanitizeOutput(req.getParameter("updateId"));
            String name = WebUtils.sanitizeOutput(req.getParameter("updateName"));
            String cityName = WebUtils.sanitizeOutput(req.getParameter("updateCityName"));
            try {
                manufactureService.update(Integer.parseInt(id), name, cityName);
            } catch (RuntimeException e) {
                req.setAttribute("error", e.getMessage());
            }
        }
        if (action.equals("delete")) {
            String id = WebUtils.sanitizeOutput(req.getParameter("deleteId"));
            try {
                manufactureService.delete(Integer.parseInt(id));
            } catch (RuntimeException e) {
                req.setAttribute("error", e.getMessage());
            }
        }
        req.setAttribute("city-names", cityService.getAll());
        req.setAttribute("navbarFile", "navbarAdmin.jsp");
        List<Manufacture> manufactures = manufactureService.getAll();
        req.setAttribute("manufactures", manufactures);
        req.getRequestDispatcher("jsps/manufacture.jsp").forward(req, resp);
    }
}
