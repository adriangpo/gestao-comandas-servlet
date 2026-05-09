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

@WebServlet("/dashboard")
public class DashboardController extends HttpServlet {

    private final MesaService mesaService = new MesaService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        try {
            List<Mesa> tables = mesaService.findAllOrdered();
            request.setAttribute("mesas", tables);
            request.getRequestDispatcher("/WEB-INF/views/dashboard.jsp").forward(request, response);
        } catch (SQLException exception) {
            throw new ServletException(exception);
        }
    }
}
