package com.gestaocomandas.service;

import com.gestaocomandas.dao.ItemPedidoDAO;
import com.gestaocomandas.dao.MesaDAO;
import com.gestaocomandas.dao.PedidoDAO;
import com.gestaocomandas.vo.ItemPedido;
import com.gestaocomandas.vo.Pedido;
import com.gestaocomandas.vo.Produto;

import java.sql.SQLException;
import java.util.List;

public class PedidoService {

    private final PedidoDAO pedidoDAO;
    private final ItemPedidoDAO itemDAO;
    private final MesaDAO mesaDAO;

    public PedidoService() {
        this.pedidoDAO = new PedidoDAO();
        this.itemDAO = new ItemPedidoDAO();
        this.mesaDAO = new MesaDAO();
    }

    public int createOrder(int mesaId) throws SQLException {
        Pedido pedido = new Pedido();
        pedido.setMesaId(mesaId);
        return pedidoDAO.insert(pedido);
    }

    public void addItem(int pedidoId, Produto produto, int quantidade) throws SQLException {
        ItemPedido existing = itemDAO.findByOrderAndProduct(pedidoId, produto.getId());

        if (existing != null) {
            itemDAO.updateQuantity(existing.getId(), existing.getQuantidade() + quantidade);
        } else {
            ItemPedido item = new ItemPedido();
            item.setPedidoId(pedidoId);
            item.setProdutoId(produto.getId());
            item.setQuantidade(quantidade);
            item.setPrecoUnitario(produto.getPreco());
            itemDAO.insert(item);
        }
    }

    public void updateItemQuantity(int itemId, int quantidade) throws SQLException {
        itemDAO.updateQuantity(itemId, quantidade);
    }

    public void removeItem(int itemId) throws SQLException {
        itemDAO.delete(itemId);
    }

    public List<Pedido> findPendingByMesa(int mesaId) throws SQLException {
        List<Pedido> orders = pedidoDAO.findPendingByMesa(mesaId);
        for (Pedido order : orders) {
            order.setItems(itemDAO.findByOrder(order.getId()));
        }
        return orders;
    }

    public Pedido findById(int id) throws SQLException {
        Pedido pedido = pedidoDAO.findById(id);
        if (pedido != null) {
            pedido.setItems(itemDAO.findByOrder(pedido.getId()));
        }
        return pedido;
    }

    public boolean hasPendingOrders(int mesaId) throws SQLException {
        return pedidoDAO.hasPendingOrders(mesaId);
    }

    public List<Pedido> payOrders(int mesaId) throws SQLException {
        List<Pedido> orders = findPendingByMesa(mesaId);
        for (Pedido order : orders) {
            pedidoDAO.updateStatus(order.getId(), "PAGO");
        }
        mesaDAO.updateQuantidadePessoas(mesaId, 0);
        return orders;
    }

    public void cancelOrders(int mesaId) throws SQLException {
        List<Pedido> orders = pedidoDAO.findPendingByMesa(mesaId);
        for (Pedido order : orders) {
            pedidoDAO.updateStatus(order.getId(), "CANCELADO");
        }
        mesaDAO.updateQuantidadePessoas(mesaId, 0);
    }
}
