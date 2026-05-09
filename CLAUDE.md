# Gestão de Comandas

Web-based restaurant order management system built with Jakarta Servlet + JSP + Maven, running on Apache Tomcat.

## Commit rules

- Never add "Reviewed by", "Co-Authored-By", or any similar attribution lines (Sonnet, Opus, Claude, etc.) in commit messages

## Language conventions

- **Java code** (methods, variables): English
- **VOs, DAOs, Services, Controllers** (class names): Portuguese, following the DB naming (e.g. `Produto`, `ProdutoDAO`, `ProdutoService`, `ProdutoController`)
- **User-facing text** (JSP, labels, messages): Portuguese
- **Code comments**: Portuguese
- **Database** (tables, columns): Portuguese

## Package structure (`com.gestaocomandas`)

| Pacote | Responsibility |
| --- | --- |
| `connection` | Single DB connection class (code provided by the user) |
| `vo` | Value Objects (DTOs) — represent entities |
| `dao` | Data Access Objects — direct DB access |
| `service` | Business rules |
| `controller` | Servlets handling HTTP requests |

## Entities and tables

### `produtos`

- id, nome, preco, descricao, ativo (soft delete)
- Soft delete before hard delete; can be reactivated

### `mesas`

- id, identificador (name like "1", "2A"), capacidade, quantidade_pessoas, ordem_exibicao
- Status is logical (derived from orders): has PENDENTE order → occupied, otherwise → free
- quantidade_pessoas resets to 0 when the table is closed

### `pedidos`

- id, mesa_id (FK), status (PENDENTE/PAGO/CANCELADO), criado_em, atualizado_em
- An order belongs to a table and contains multiple items

### `itens_pedido`

- id, pedido_id (FK), produto_id (FK), quantidade, preco_unitario (price snapshot at order time)
- If the same product is added again, the quantity is merged (summed)

## Main flow

1. Register products (name, price, description)
2. Register tables (identifier, capacity, display order)
3. Dashboard shows all tables ordered by `ordem_exibicao`, displaying free/occupied status
4. When adding an order to a free table → prompt for `quantidade_pessoas` (only the first time)
5. An occupied table can receive more orders without closing
6. To free a table: pay (status → PAGO) or cancel (status → CANCELADO)
7. On payment, display a "Nota Fiscal"-style receipt with: restaurant name, date/time, table identifier, items with quantity/unit price/subtotal, grand total
8. quantidade_pessoas resets to 0 on close

## Order statuses

- `PENDENTE` — open/active order
- `PAGO` — processed/finished
- `CANCELADO` — cancelled

## Frontend

- JSP for all pages (no plain HTML)
- JSTL + EL for view logic
- Dashboard as the home page with table overview

## Build and deploy

```bash
mvn clean package        # generates target/gestao-comandas.war
# deploy to Tomcat by copying .war to $TOMCAT_HOME/webapps/
```

## Stack

- Java 17
- Jakarta Servlet 6.0
- JSP 3.1 + JSTL 3.0
- MySQL 8+ (via mysql-connector-j)
- Gson (JSON serialization)
- Maven
- Apache Tomcat 10.1+
