-- mysql -u root -p < sql/clear.sql

USE gestao_comandas;

DROP TABLE IF EXISTS itens_pedido;
DROP TABLE IF EXISTS pedidos;
DROP TABLE IF EXISTS mesas;
DROP TABLE IF EXISTS produtos;

DROP DATABASE IF EXISTS gestao_comandas;
