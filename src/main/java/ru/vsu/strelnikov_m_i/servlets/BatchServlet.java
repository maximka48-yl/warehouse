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
import ru.vsu.strelnikov_m_i.repositories.database_connected.BatchRepository;
import ru.vsu.strelnikov_m_i.repositories.database_connected.SampleRepository;
import ru.vsu.strelnikov_m_i.services.BatchService;
import ru.vsu.strelnikov_m_i.services.SampleService;
import ru.vsu.strelnikov_m_i.utils.WebUtils;

import java.io.IOException;
import java.sql.Date;

@WebServlet("/batch")
public class BatchServlet extends HttpServlet {
    private BatchService batchService;
    private SampleService sampleService;
    @Override
    public void init(ServletConfig config) throws ServletException {
        batchService = new BatchService(new BatchRepository());
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
        req.setAttribute("sample-names", sampleService.getAllNames());
        req.setAttribute("batches", batchService.getAll());
        req.getRequestDispatcher("jsps/batch.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = WebUtils.sanitizeOutput(req.getParameter("action"));
        if (action.equals("add")) {
            String date = WebUtils.sanitizeOutput(req.getParameter("addDate"));
            String sampleName = WebUtils.sanitizeOutput(req.getParameter("addSampleName"));
            try {
                batchService.add(Date.valueOf(date), sampleName);
            } catch (RuntimeException e) {
                req.setAttribute("error", e.getMessage());
            }
        }
        if (action.equals("update")) {
            String id = WebUtils.sanitizeOutput(req.getParameter("updateId"));
            String date = WebUtils.sanitizeOutput(req.getParameter("updateDate"));
            String sampleName = WebUtils.sanitizeOutput(req.getParameter("updateSampleName"));
            try {
                batchService.update(Integer.parseInt(id), Date.valueOf(date), sampleName);
            } catch (RuntimeException e) {
                req.setAttribute("error", e.getMessage());
            }
        }
        if (action.equals("delete")) {
            String id = WebUtils.sanitizeOutput(req.getParameter("deleteId"));
            try {
                batchService.delete(Integer.parseInt(id));
            } catch (RuntimeException e) {
                req.setAttribute("error", e.getMessage());
            }
        }
        req.setAttribute("navbarFile", "navbarAdmin.jsp");
        req.setAttribute("sample-names", sampleService.getAllNames());
        req.setAttribute("batches", batchService.getAll());
        req.getRequestDispatcher("jsps/batch.jsp").forward(req, resp);
    }

}
