package com.gestaocomandas.controller;

import com.gestaocomandas.service.ProdutoService;
import com.gestaocomandas.vo.Produto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@WebServlet("/produtos")
public class ProdutoController extends HttpServlet {

    private final ProdutoService service = new ProdutoService();

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
                case "delete" -> softDelete(request, response);
                case "reactivate" -> reactivate(request, response);
                default -> response.sendRedirect(request.getContextPath() + "/produtos");
            }
        } catch (SQLException exception) {
            throw new ServletException(exception);
        }
    }

    private void listAll(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        List<Produto> products = service.findAll();
        request.setAttribute("produtos", products);
        request.getRequestDispatcher("/WEB-INF/views/produtos/listar.jsp").forward(request, response);
    }

    private void showForm(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        String idParam = request.getParameter("id");
        if (idParam != null) {
            Produto produto = service.findById(Integer.parseInt(idParam));
            request.setAttribute("produto", produto);
        }
        request.getRequestDispatcher("/WEB-INF/views/produtos/form.jsp").forward(request, response);
    }

    private void save(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        Produto produto = new Produto();

        String idParam = request.getParameter("id");
        if (idParam != null && !idParam.isEmpty()) {
            produto.setId(Integer.parseInt(idParam));
        }

        produto.setNome(request.getParameter("nome"));
        produto.setPreco(Double.parseDouble(request.getParameter("preco")));
        produto.setDescricao(request.getParameter("descricao"));

        service.save(produto);
        response.sendRedirect(request.getContextPath() + "/produtos");
    }

    private void softDelete(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        service.softDelete(id);
        response.sendRedirect(request.getContextPath() + "/produtos");
    }

    private void reactivate(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int id = Integer.parseInt(request.getParameter("id"));
        service.reactivate(id);
        response.sendRedirect(request.getContextPath() + "/produtos");
    }
}
