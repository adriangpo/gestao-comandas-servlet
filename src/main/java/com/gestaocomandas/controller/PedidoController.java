package com.gestaocomandas.controller;

import com.gestaocomandas.service.MesaService;
import com.gestaocomandas.service.PedidoService;
import com.gestaocomandas.service.ProdutoService;
import com.gestaocomandas.vo.ItemPedido;
import com.gestaocomandas.vo.Mesa;
import com.gestaocomandas.vo.Pedido;
import com.gestaocomandas.vo.Produto;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("/pedidos")
public class PedidoController extends HttpServlet {

    private final PedidoService pedidoService = new PedidoService();
    private final MesaService mesaService = new MesaService();
    private final ProdutoService produtoService = new ProdutoService();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String action = request.getParameter("action");

        try {
            if ("receipt".equals(action)) {
                showReceipt(request, response);
            } else {
                showTableOrders(request, response);
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
                case "createOrder" -> createOrder(request, response);
                case "addItem" -> addItem(request, response);
                case "updateItemQuantity" -> updateItemQuantity(request, response);
                case "removeItem" -> removeItem(request, response);
                case "pay" -> pay(request, response);
                case "cancel" -> cancel(request, response);
                default -> response.sendRedirect(request.getContextPath() + "/dashboard");
            }
        } catch (SQLException exception) {
            throw new ServletException(exception);
        }
    }

    private void showTableOrders(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int mesaId = Integer.parseInt(request.getParameter("mesaId"));
        Mesa mesa = mesaService.findById(mesaId);
        List<Pedido> orders = pedidoService.findPendingByMesa(mesaId);
        List<Produto> products = produtoService.findActive();
        boolean ocupada = pedidoService.hasPendingOrders(mesaId);

        request.setAttribute("mesa", mesa);
        request.setAttribute("pedidos", orders);
        request.setAttribute("produtos", products);
        request.setAttribute("ocupada", ocupada);
        request.getRequestDispatcher("/WEB-INF/views/pedidos/detalhes.jsp").forward(request, response);
    }

    private void createOrder(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int mesaId = Integer.parseInt(request.getParameter("mesaId"));

        String peopleCountParam = request.getParameter("quantidadePessoas");
        if (peopleCountParam != null && !peopleCountParam.isEmpty()) {
            int quantidadePessoas = Integer.parseInt(peopleCountParam);
            mesaService.updateQuantidadePessoas(mesaId, quantidadePessoas);
        }

        pedidoService.createOrder(mesaId);
        response.sendRedirect(request.getContextPath() + "/pedidos?mesaId=" + mesaId);
    }

    private void addItem(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int mesaId = Integer.parseInt(request.getParameter("mesaId"));
        int pedidoId = Integer.parseInt(request.getParameter("pedidoId"));
        int produtoId = Integer.parseInt(request.getParameter("produtoId"));
        int quantidade = Integer.parseInt(request.getParameter("quantidade"));

        Produto produto = produtoService.findById(produtoId);
        pedidoService.addItem(pedidoId, produto, quantidade);

        response.sendRedirect(request.getContextPath() + "/pedidos?mesaId=" + mesaId);
    }

    private void updateItemQuantity(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int mesaId = Integer.parseInt(request.getParameter("mesaId"));
        int itemId = Integer.parseInt(request.getParameter("itemId"));
        int quantidade = Integer.parseInt(request.getParameter("quantidade"));

        if (quantidade <= 0) {
            pedidoService.removeItem(itemId);
        } else {
            pedidoService.updateItemQuantity(itemId, quantidade);
        }
        response.sendRedirect(request.getContextPath() + "/pedidos?mesaId=" + mesaId);
    }

    private void removeItem(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int mesaId = Integer.parseInt(request.getParameter("mesaId"));
        int itemId = Integer.parseInt(request.getParameter("itemId"));

        pedidoService.removeItem(itemId);
        response.sendRedirect(request.getContextPath() + "/pedidos?mesaId=" + mesaId);
    }

    private void pay(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int mesaId = Integer.parseInt(request.getParameter("mesaId"));
        List<Pedido> paidOrders = pedidoService.payOrders(mesaId);
        Mesa mesa = mesaService.findById(mesaId);

        List<ItemPedido> allItems = new ArrayList<>();
        for (Pedido order : paidOrders) {
            allItems.addAll(order.getItems());
        }

        double total = allItems.stream()
                .mapToDouble(ItemPedido::getSubtotal)
                .sum();

        request.setAttribute("mesa", mesa);
        request.setAttribute("items", allItems);
        request.setAttribute("total", total);
        request.getRequestDispatcher("/WEB-INF/views/pedidos/nota-fiscal.jsp").forward(request, response);
    }

    private void cancel(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, IOException {
        int mesaId = Integer.parseInt(request.getParameter("mesaId"));
        pedidoService.cancelOrders(mesaId);
        response.sendRedirect(request.getContextPath() + "/dashboard");
    }

    private void showReceipt(HttpServletRequest request, HttpServletResponse response)
            throws SQLException, ServletException, IOException {
        int mesaId = Integer.parseInt(request.getParameter("mesaId"));
        Mesa mesa = mesaService.findById(mesaId);
        request.setAttribute("mesa", mesa);
        request.getRequestDispatcher("/WEB-INF/views/pedidos/nota-fiscal.jsp").forward(request, response);
    }
}
