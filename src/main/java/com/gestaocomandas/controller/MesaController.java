package com.gestaocomandas.controller;

import com.gestaocomandas.service.MesaService;
import com.gestaocomandas.vo.Mesa;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/mesas")
public class MesaController extends HttpServlet {

    private final MesaService service = new MesaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("form".equals(action)) {
                showForm(request, response);
            } else {
                listAll(request, response);
            }
        } catch (SQLException exception) {
            throw new ServletException(exception);
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            switch (action) {
                case "save" -> save(request, response);
                case "delete" -> delete(request, response);
                case "reorder" -> reorder(request, response);
                default -> response.sendRedirect(request.getContextPath() + "/mesas");
            }
        } catch (SQLException exception) {
            throw new ServletException(exception);
        }
    }

    private void listAll(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Mesa> tables = service.findAllOrdered();
        request.setAttribute("mesas", tables);
        request.getRequestDispatcher("/WEB-INF/views/mesas/listar.jsp").forward(request, response);
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam != null) {
            Mesa mesa = service.findById(Integer.parseInt(idParam));
            request.setAttribute("mesa", mesa);
        }
        request.getRequestDispatcher("/WEB-INF/views/mesas/form.jsp").forward(request, response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        Mesa mesa = new Mesa();

        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            mesa.setId(Integer.parseInt(idParam));
        }

        mesa.setIdentificador(request.getParameter("identificador"));
        mesa.setCapacidade(Integer.parseInt(request.getParameter("capacidade")));

        service.save(mesa);
        response.sendRedirect(request.getContextPath() + "/mesas");
    }

    private void delete(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        service.delete(id);
        response.sendRedirect(request.getContextPath() + "/mesas");
    }

    private void reorder(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        String[] ids = request.getParameterValues("ids");
        if (ids != null) {
            for (int i = 0; i < ids.length; i++) {
                service.updateOrdemExibicao(Integer.parseInt(ids[i]), i + 1);
            }
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"ok\":true}");
    }
}
