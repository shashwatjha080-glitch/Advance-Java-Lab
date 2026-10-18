# ShopSphere - RTU Advanced Java Viva Questions & Answers

This guide contains essential technical questions and model answers tailored for RTU Advanced Java project evaluations, practical exams, and project demonstrations.

---

## 1. Architecture & Design Patterns

### Q1: What architecture does ShopSphere follow?
**Answer:** ShopSphere implements an **N-Tier Enterprise Architecture** adhering to the **Model-View-Controller (MVC)** design pattern:
- **Presentation Layer (View):** JSP pages augmented with Expression Language (EL), JSTL tags, and custom tag files (`<tags:productCard />`).
- **Controller Layer:** Jakarta Servlets extending `HttpServlet` (`ProductServlet`, `CartServlet`, `CheckoutServlet`, `AdminDashboardServlet`) routing HTTP requests and dispatching views.
- **Service Layer (Model):** Encapsulates core business rules (`AuthenticationService`, `CheckoutService`, `ProductService`, `CouponService`).
- **Data Access Layer (DAO):** JDBC-based persistence with `PreparedStatement`, transaction handling, and connection pooling in `DBUtil`.
- **Database Layer:** MySQL 8.0 relational database with referential integrity and indexes.

### Q2: Why is business logic kept out of JSP pages?
**Answer:** Placing business logic in JSP scriptlets violates the Single Responsibility Principle, hinders maintainability, prevents unit testing, and creates security vulnerabilities. In ShopSphere, business rules are cleanly isolated inside POJO service classes, ensuring JSPs remain declarative presentation templates.

---

## 2. Servlets, Filters, and Listeners

### Q3: Explain the Servlet Lifecycle and where it is demonstrated in ShopSphere.
**Answer:**
1. **Loading & Instantiation:** Servlet container (Tomcat) loads the servlet class and invokes its default constructor.
2. **Initialization (`init(ServletConfig config)`):** Invoked once upon startup or first request to initialize servlet resources.
3. **Request Handling (`service()`, `doGet()`, `doPost()`):** Dispatches concurrent client requests across container threads.
4. **Destruction (`destroy()`):** Called prior to unloading to release open resources.
In ShopSphere, lifecycle operations are demonstrated in all controller servlets and application lifecycle events are monitored in `AppListener`.

### Q4: How do Servlet Filters work in ShopSphere?
**Answer:** Filters intercept requests before reaching servlets and responses before returning to the browser.
- `AuthenticationFilter`: Intercepts protected customer routes (`/cart`, `/checkout`, `/orders`, `/profile`, `/wishlist`). If `session.getAttribute("user")` is null, it saves the requested URL and redirects to `/login.jsp`.
- `AdminFilter`: Secures `/admin/*` routes by verifying `user != null && user.isAdmin()`. Unauthorized access is blocked with a 403 or admin login redirect.
- `I18nFilter`: Sets UTF-8 encoding on requests/responses and dynamically binds the active `Locale` (`en` or `hi`) to the session for internationalization.

### Q5: What is the purpose of `AppListener`?
**Answer:** It implements both `ServletContextListener` and `HttpSessionListener`:
- On `contextInitialized()`, it logs application startup metadata, registers application-scoped constants (`appName`, `appVersion`), and preloads active product categories into application scope.
- On `sessionCreated()` / `sessionDestroyed()`, it atomically updates active user session counts using `AtomicInteger` for real-time monitoring.

---

## 3. Session, Cookies & Security

### Q6: How does ShopSphere manage session tracking?
**Answer:** Using `HttpServletRequest.getSession(true)`. Tomcat issues a unique `JSESSIONID` cookie with `HttpOnly` enabled to prevent Cross-Site Scripting (XSS) attacks. The session stores the authenticated `User` object, customer role, and cached cart item counts.

### Q7: How are passwords secured in the database?
**Answer:** Plaintext passwords are never stored. Passwords are hashed using **SHA-256** cryptographic digests in `PasswordUtil.hashPassword()`. During authentication, the user-supplied password is encrypted with SHA-256 and compared in constant time with the stored 64-character hexadecimal digest.

### Q8: What prevents SQL Injection in ShopSphere?
**Answer:** All database operations across every DAO (`UserDAO`, `ProductDAO`, `OrderDAO`, `CouponDAO`) strictly utilize `PreparedStatement` with typed placeholders (`?`). Values are bound using `ps.setString()`, `ps.setInt()`, and `ps.setDouble()`, preventing user input from escaping into the SQL query context.

### Q9: How are transactional order placements made ACID-compliant?
**Answer:** In `OrderDAO.placeOrder()`:
1. `conn.setAutoCommit(false);`
2. Deduct inventory stock for each cart item. If stock is insufficient, `conn.rollback()` is immediately triggered.
3. Insert order header into `orders`.
4. Batch-insert line items into `order_items`.
5. Record simulated payment in `payments`.
6. Record coupon audit usage in `coupon_usage`.
7. Clear user cart from `cart`.
8. `conn.commit();` executes atomically. Any unexpected `SQLException` triggers `conn.rollback()`.

---

## 4. JSP, Expression Language (EL), and JSTL

### Q10: What is the difference between JSP Scriptlets and EL?
**Answer:**
- Scriptlets (`<% ... %>`) embed raw Java code into HTML, which clutters markup and makes code error-prone.
- Expression Language (`${...}`) provides a concise, null-safe syntax for accessing JavaBean properties and scoped attributes (`requestScope`, `sessionScope`, `applicationScope`).

### Q11: What custom JSP tags did you implement?
**Answer:**
- `<tags:productCard product="${p}" />`: Reusable component rendering image thumbnail, discount badge, pricing calculation, brand, title, and Add-to-Cart form.
- `<tags:rating rating="${averageRating}" showNumber="true" />`: Dynamic star-rating component rendering filled, half-filled, or empty star icons according to numeric score.

---

## 5. Desktop Java Swing & Networking

### Q12: How is the Swing module architected?
**Answer:** Following Swing MVC:
- **Model:** Domain entities and DTOs.
- **View:** `LoginFrame`, `AdminDashboard`, `ProductFrame`, `CategoryFrame`, `CustomerFrame`, `OrderFrame`, `InventoryFrame`.
- **Controller / DAO:** `AdminDAO` interfacing directly with MySQL via JDBC.
- **Pluggable Look & Feel:** Configured via `UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())`.

### Q13: How does the Java Socket Networking module work?
**Answer:**
- `InventoryServer` creates a `ServerSocket` on TCP port 8888 and spawns a new `Thread` for each connecting client (`ClientHandler`).
- `InventoryClient` connects via `Socket` and exchanges commands (`GET_STOCK`, `SEARCH_PRODUCT`, `GET_PRODUCT`).
- It demonstrates **Java Object Serialization** by transmitting serialized `ProductDTO` instances over `ObjectOutputStream` and deserializing them with `ObjectInputStream`.

### Q14: How does the Java RMI module operate?
**Answer:**
- `InventoryService` extends `java.rmi.Remote` declaring remote methods throwing `RemoteException`.
- `InventoryServiceImpl` extends `UnicastRemoteObject` implementing the remote interface.
- `RmiServerMain` initializes the RMI Registry on port 1099 using `LocateRegistry.createRegistry(1099)` and registers the service with `Naming.rebind("rmi://localhost:1099/ShopSphereInventoryService", service)`.
- `RmiClientMain` retrieves the remote stub via `Naming.lookup()` and executes distributed method calls (`getProduct`, `getStock`, `updateStock`).
