# ShopSphere: Advanced Java Enterprise E-Commerce Management System
## Academic Capstone Project Report

**Degree / Course:** RTU Advanced Java / B.Tech Computer Science & Information Technology  
**Project Title:** ShopSphere - Distributed Multi-Tier E-Commerce Platform  
**Target Environment:** Apache Tomcat 11, Jakarta EE 10, Java SE 17+, MySQL 8.0, Java Swing  

---

## 1. Introduction
Modern online commerce demands scalable, secure, and distributed enterprise architectures capable of handling real-time inventory management, multi-channel customer access, and atomic transactional integrity.

**ShopSphere** is a complete Java Enterprise E-Commerce Management System engineered specifically to satisfy the Rajasthan Technical University (RTU) Advanced Java syllabus. The project connects enterprise web applications (Jakarta Servlets, JSP, EL, JSTL, Filters, Listeners) with desktop management tools (Java Swing MVC), TCP socket networking, Java RMI distributed computing, and internationalization.

---

## 2. Problem Statement
Many educational academic projects demonstrate isolated technology concepts (e.g. a simple Swing form or a single servlet) without demonstrating how these technologies cooperate in a cohesive enterprise setting. 

ShopSphere addresses this limitation by delivering a real, production-grade e-commerce application where:
1. Web shoppers browse catalog products, manage persistent carts, apply promo coupons, and place orders with mock payments.
2. Store administrators manage inventory, fulfill customer orders, and analyze revenue via either a web dashboard or a standalone Java Swing desktop application.
3. Automated inventory and warehouse systems interact with product databases via low-latency TCP Sockets and Java RMI distributed services.

---

## 3. Technology Stack

| Layer | Technologies Selected | Justification |
|---|---|---|
| **Language** | Java SE 17+ (Java 26 verified) | Modern Java language features, robust concurrency, strong typing |
| **Web Container** | Apache Tomcat 11 | Premier open-source Jakarta EE container supporting Servlet 6.1 |
| **Servlet Specification**| Jakarta Servlet API 6.1.0 | Latest enterprise namespace (`jakarta.*`), removing obsolete `javax.*` |
| **View Layer** | JSP, JSTL 3.0, EL, Bootstrap 5 | Clean declarative views without business scriptlets |
| **Database** | MySQL 8.0+ | ACID-compliant relational database with foreign key referential integrity |
| **Persistence** | JDBC 4.2 & MySQL Connector/J | Raw high-performance SQL with `PreparedStatement` and connection pooling |
| **Desktop Module** | Java Swing, AWT | Pluggable Look and Feel, Swing MVC architecture |
| **Networking** | `java.net` Sockets, ServerSocket | Low-level TCP streaming protocol and multi-threaded client handling |
| **Distributed Computing**| Java RMI, RMI Registry | Remote method invocation across distinct JVM instances |
| **Security** | SHA-256 Digest Hashing | Cryptographic one-way hashing for user authentication |
| **Build Tool** | Apache Maven 3.6+ | Automated dependency management and multi-module lifecycle builds |

---

## 4. System Architecture
ShopSphere follows an **N-Tier Architecture**:
1. **Client Tier:** Web Browsers (Chrome, Edge, Firefox), Java Swing Desktop GUI, Socket Network Clients, RMI Stubs.
2. **Web Tier:** Apache Tomcat 11 executing `AuthenticationFilter`, `AdminFilter`, `I18nFilter`, and Servlet Controllers.
3. **Application & Service Tier:** Business services (`AuthenticationService`, `ProductService`, `CheckoutService`, `OrderService`, `CouponService`).
4. **Data Access Tier:** Data Access Objects (`UserDAO`, `ProductDAO`, `CartDAO`, `OrderDAO`, `CouponDAO`) communicating through `DBUtil`.
5. **Data Persistence Tier:** MySQL 8.0 database engine (`shopsphere` schema).

---

## 5. Key Modules Implemented

### 5.1 Customer Web Storefront (`web`)
- **Homepage (`index.jsp`):** Dynamic hero banner, category pills, featured deals, and core service guarantees.
- **Product Catalog (`products.jsp`):** Real-time search, category filters, multi-criteria sorting (Price, Discount, Name), and pagination.
- **Product Details (`product-details.jsp`):** High-resolution imagery, pricing breakdown with discount badges, stock warnings, verified customer reviews, and Add-to-Cart / Buy-Now workflows.
- **Shopping Cart (`cart.jsp`):** Line items, quantity increments/decrements, item removal, promo coupon validation (`WELCOME10`, `FESTIVE20`, `FLAT500`), and free shipping thresholds.
- **Checkout & Payment (`checkout.jsp`):** Saved address selection or new address capture, payment method selector (Cards, UPI, Net Banking, COD), and server-side pricing recalculation.
- **Order Tracking (`orders.jsp`, `order-details.jsp`):** Customer order history and visual status progression timeline (Placed -> Confirmed -> Shipped -> Delivered).
- **Profile & Wishlist (`profile.jsp`, `wishlist.jsp`):** Password updates, address book management, and favorite product management.

### 5.2 Desktop Swing Administration (`swing-admin`)
- **Swing MVC:** Cleanly isolates entity models, view frames, controllers, and database access.
- **Components:** Employs `JFrame`, `JPanel`, `JLabel`, `JTextField`, `JPasswordField`, `JButton`, `JCheckBox`, `JComboBox`, `JTable`, `JScrollPane`, and `JOptionPane`.
- **Management Screens:** `LoginFrame`, `AdminDashboard` (with real-time revenue cards), `ProductFrame` (complete product CRUD), `CategoryFrame`, `CustomerFrame`, `OrderFrame`, and `InventoryFrame` (low-stock alerts and restock controls).

### 5.3 Java Socket Networking (`network`)
- **`InventoryServer`:** Listens on port 8888 using `ServerSocket`, spawns worker threads, processes commands: `GET_STOCK`, `SEARCH_PRODUCT`, `GET_PRODUCT`.
- **Object Serialization:** Demonstrates transmission of binary serialized `ProductDTO` instances using `ObjectOutputStream` and `ObjectInputStream`.

### 5.4 Java Remote Method Invocation (`rmi`)
- **`InventoryService`:** Extends `java.rmi.Remote` exposing remote methods: `getProduct(id)`, `getStock(id)`, and `updateStock(id, qty)`.
- **RMI Registry:** Server bootstraps `LocateRegistry.createRegistry(1099)` and registers the service stub under `ShopSphereInventoryService`.
- **RMI Client:** Binds to the remote registry via `Naming.lookup()` and invokes distributed inventory operations.

---

## 6. Verification and Testing Results
- **Automated Tests:** 4/4 JUnit 5 tests passed in `BusinessLogicTest` covering password hashing, validations, discount computations, and coupon business logic.
- **Maven Multi-Module Build:** 5/5 modules compiled and packaged successfully into `shopsphere.war` and associated JARs.
- **Security Validation:** Tested unauthenticated cart access, duplicate email rejection, role-based admin filtering, and server-side total verification.

---

## 7. Conclusion
ShopSphere bridges the gap between academic Java concepts and real-world enterprise software engineering. By implementing modern Jakarta EE servlets, responsive JSTL/EL views, desktop Swing MVC, low-level socket communication, and distributed RMI, the project provides a comprehensive capstone deliverable for RTU Advanced Java examinations.
