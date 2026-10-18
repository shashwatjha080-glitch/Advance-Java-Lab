# ShopSphere - System Architecture & Technical Design

## 1. High-Level System Architecture

ShopSphere is designed around a modern **N-Tier Enterprise Architecture** adhering to RTU Advanced Java standards. The platform decouples presentation, controller, business services, data access, and database storage. In addition to the web store, desktop and distributed network services connect through JDBC, TCP Sockets, and Java RMI.

```mermaid
graph TD
    subgraph Client Layer
        Browser["Modern Web Browser<br/>(HTML5 / CSS3 / Bootstrap / JS)"]
        SwingApp["Desktop Admin Suite<br/>(Java Swing JFrame / MVC)"]
        NetClient["Socket Network Client<br/>(java.net.Socket)"]
        RmiClient["Distributed RMI Client<br/>(Naming.lookup)"]
    end

    subgraph Presentation & Controller Layer
        Tomcat["Apache Tomcat 11 Web Container<br/>(Jakarta EE 10 / Servlet 6.1)"]
        JSPViews["JSP Views + EL + JSTL<br/>(Custom Tags: productCard, rating)"]
        Filters["Filters: AuthFilter, AdminFilter, I18nFilter"]
        Listeners["Listeners: AppListener<br/>(ServletContext & HttpSession)"]
        Servlets["Controllers:<br/>Product, Cart, Checkout, Order, Admin"]
    end

    subgraph Business Service Layer
        AuthService["AuthenticationService<br/>(SHA-256 Hashing)"]
        ProdService["ProductService<br/>(Catalog, Low Stock)"]
        OrderService["OrderService<br/>(Fulfillment, Dashboard)"]
        CheckoutService["CheckoutService<br/>(Server-side Totals, Coupons)"]
        CouponService["CouponService<br/>(Promotional Discounts)"]
    end

    subgraph Data Access Layer
        DBUtil["DBUtil Connection Pool<br/>(JNDI DataSource / DriverManager)"]
        DAOs["DAOs: UserDAO, ProductDAO, CartDAO,<br/>OrderDAO, CouponDAO, ReviewDAO"]
    end

    subgraph Distributed & Networking Subsystems
        InventorySocketServer["InventoryServer<br/>(ServerSocket Port 8888)"]
        RmiRegistry["RMI Registry<br/>(LocateRegistry Port 1099)"]
        RmiService["InventoryServiceImpl<br/>(UnicastRemoteObject)"]
    end

    subgraph Persistence Layer
        MySQL[("MySQL 8.0 Database<br/>(Schema: shopsphere)")]
    end

    Browser <-->|HTTP / HTTPS| Tomcat
    Tomcat --> Filters --> Servlets --> JSPViews
    Servlets --> AuthService & ProdService & OrderService & CheckoutService & CouponService
    AuthService & ProdService & OrderService & CheckoutService & CouponService --> DAOs
    DAOs --> DBUtil --> MySQL

    SwingApp --> DAOs
    NetClient <-->|TCP Socket Port 8888| InventorySocketServer --> MySQL
    RmiClient <-->|RMI Registry Lookup| RmiRegistry
    RmiClient <-->|Remote Invocations| RmiService --> MySQL
```

---

## 2. N-Tier Web Layered Architecture

```mermaid
sequenceDiagram
    autonumber
    actor Customer as Web Customer
    participant Browser as Browser (View)
    participant Filter as AuthenticationFilter
    participant Controller as CheckoutServlet
    participant Service as CheckoutService
    participant DAO as OrderDAO
    participant DB as MySQL Database

    Customer->>Browser: Clicks "Place Order Securely"
    Browser->>Filter: POST /checkout (Address, Payment, Coupon)
    Filter->>Filter: Verify HttpSession (User != null)
    Filter->>Controller: Forward Request
    Controller->>Service: placeOrder(userId, addressId, paymentMethod, coupon)
    Service->>Service: Recalculate Totals & Verify Stock on Server
    Service->>DAO: placeOrder(order, items, couponId)
    Note over DAO,DB: BEGIN TRANSACTION (conn.setAutoCommit(false))
    DAO->>DB: Deduct Product Stocks (stock = stock - qty)
    DAO->>DB: INSERT INTO orders
    DAO->>DB: INSERT INTO order_items (batch)
    DAO->>DB: INSERT INTO payments (mock transaction reference)
    DAO->>DB: INSERT INTO coupon_usage
    DAO->>DB: DELETE FROM cart WHERE user_id = ?
    Note over DAO,DB: COMMIT TRANSACTION (conn.commit())
    DAO-->>Service: Return Order with Generated ID
    Service-->>Controller: Return Order Confirmation
    Controller-->>Browser: Redirect 302 -> /order-success?orderId=...
    Browser-->>Customer: Display Receipt & Tracking Timeline
```

---

## 3. Security Architecture

1. **Authentication:**
   - Passwords hashed using cryptographically secure **SHA-256** with hexadecimal digest encoding. Plaintext passwords are never persisted in the database or stored in cookies.
2. **Session State:**
   - `HttpSession` tracks authenticated identity and security roles (`ADMIN`, `CUSTOMER`). Cookies are strictly used for non-sensitive convenience functions (remembering login email, recently viewed product IDs).
3. **Role-Based Authorization:**
   - `AuthenticationFilter` guards customer transactions (`/cart`, `/checkout`, `/orders`, `/profile`, `/wishlist`).
   - `AdminFilter` intercepts and strictly restricts `/admin/*` routes to accounts bearing the `ADMIN` role.
4. **Data Integrity & SQL Injection Defense:**
   - 100% of SQL statements across all DAOs use `PreparedStatement` with typed parameters (`ps.setInt`, `ps.setString`, `ps.setDouble`). Zero string concatenation is used in queries.
5. **Server-Side Pricing Authority:**
   - Grand totals, shipping fees, product discounts, and coupon calculations are strictly re-evaluated and enforced on the server. Browser-submitted prices are ignored.
