package ru.vsu.strelnikov_m_i.servlets;

import jakarta.servlet.ServletConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import ru.vsu.strelnikov_m_i.entities.City;
import ru.vsu.strelnikov_m_i.entities.User;
import ru.vsu.strelnikov_m_i.enums.RoleType;
import ru.vsu.strelnikov_m_i.repositories.database_connected.CityRepository;
import ru.vsu.strelnikov_m_i.services.CityService;
import ru.vsu.strelnikov_m_i.utils.WebUtils;

import java.io.IOException;
import java.util.List;

@WebServlet("/city")
public class CityServlet extends HttpServlet {
    private CityService cityService;
    @Override
    public void init(ServletConfig config) throws ServletException {
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
        req.setAttribute("navbarFile", "navbarAdmin.jsp");
        List<City> cities = cityService.getAll();
        req.setAttribute("cities", cities);
        req.getRequestDispatcher("jsps/city.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = WebUtils.sanitizeOutput(req.getParameter("action"));
        if (action.equals("add")) {
            String cityName = WebUtils.sanitizeOutput(req.getParameter("addCityName"));
            try {
                cityService.add(cityName);
            } catch (RuntimeException e) {
                req.setAttribute("error", e.getMessage());
            }
        }
        if (action.equals("update")) {
            String cityId = WebUtils.sanitizeOutput(req.getParameter("updateId"));
            String cityName = WebUtils.sanitizeOutput(req.getParameter("updateCityName"));
            try {
                cityService.update(Integer.parseInt(cityId), cityName);
            } catch (RuntimeException e) {
                req.setAttribute("error", e.getMessage());
            }
        }
        if (action.equals("delete")) {
            String id = WebUtils.sanitizeOutput(req.getParameter("deleteId"));
            try {
                cityService.delete(Integer.parseInt(id));
            } catch (RuntimeException e) {
                req.setAttribute("error", e.getMessage());
            }
        }
        req.setAttribute("navbarFile", "navbarAdmin.jsp");
        List<City> cities = cityService.getAll();
        req.setAttribute("cities", cities);
        req.getRequestDispatcher("jsps/city.jsp").forward(req, resp);
    }
}
