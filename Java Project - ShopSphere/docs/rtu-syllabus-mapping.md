# ShopSphere - RTU Advanced Java Syllabus Mapping

This document comprehensively maps every topic in the **Rajasthan Technical University (RTU) Advanced Java Programming Syllabus** to its exact implementation in the ShopSphere enterprise codebase.

---

## Comprehensive Topic-to-Implementation Matrix

| RTU Syllabus Topic | Implementation in ShopSphere | Source File / Artifact |
|---|---|---|
| **Java Swing (GUI)** | Desktop Admin Application Suite with Pluggable Look and Feel | `swing-admin/src/main/java/com/shopsphere/admin/AdminMain.java` |
| **Swing Components** | JFrame, JPanel, JLabel, JTextField, JPasswordField, JButton, JCheckBox, JComboBox, JTable, JScrollPane, JOptionPane | `swing-admin/src/main/java/com/shopsphere/admin/view/` (`LoginFrame.java`, `ProductFrame.java`, `AdminDashboard.java`) |
| **Pluggable Look and Feel (PLAF)** | `UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName())` with graceful fallback | `swing-admin/src/main/java/com/shopsphere/admin/AdminMain.java` |
| **MVC Architecture (Swing)** | Decoupled Model (DTOs), View (JFrames), Controller, and DAO | `swing-admin/src/main/java/com/shopsphere/admin/` |
| **MVC Architecture (Web)** | Jakarta Servlets (Controller), Services/DAOs (Model), JSP/EL/JSTL (View) | `web/src/main/java/com/shopsphere/` |
| **N-Tier Architecture** | Browser -> Filter -> Servlet -> Service -> DAO -> DBUtil -> MySQL | `docs/system-architecture.md` |
| **JDBC / java.sql** | Connection, PreparedStatement, ResultSet, Statement, Database transactions | `web/src/main/java/com/shopsphere/dao/` (`UserDAO.java`, `OrderDAO.java`, `ProductDAO.java`) |
| **JDBC Driver & Connection Pooling** | MySQL Connector/J 8+ with JNDI DataSource support & classpath fallback | `web/src/main/java/com/shopsphere/util/DBUtil.java` |
| **java.net Networking** | Multi-threaded TCP inventory server using `ServerSocket` on port 8888 | `network/src/main/java/com/shopsphere/network/server/InventoryServer.java` |
| **Client-Server Communication** | Java TCP Socket client with command-response protocol (`GET_STOCK`, `SEARCH_PRODUCT`) | `network/src/main/java/com/shopsphere/network/client/InventoryClient.java` |
| **Object Serialization** | `Serializable`, `ObjectOutputStream`, `ObjectInputStream` transmitting `ProductDTO` | `network/src/main/java/com/shopsphere/network/model/ProductDTO.java` |
| **Java RMI Architecture** | Remote Interface (`InventoryService`), Skeleton/Stub implementation | `rmi/src/main/java/com/shopsphere/rmi/remote/InventoryService.java` |
| **RMI Registry & Naming** | Programmatic RMI Registry bootstrap (`LocateRegistry.createRegistry(1099)`), `Naming.rebind`, `Naming.lookup` | `rmi/src/main/java/com/shopsphere/rmi/server/RmiServerMain.java` |
| **RMI Client Execution** | Remote method invocation against inventory service | `rmi/src/main/java/com/shopsphere/rmi/client/RmiClientMain.java` |
| **JNDI (Naming & Directory)** | InitialContext lookup for enterprise `jdbc/ShopSphereDB` connection pool | `web/src/main/java/com/shopsphere/util/JndiDemo.java` |
| **URL & URLConnection** | HTTP connection inspection, protocol handlers, headers, and response streams | `web/src/main/java/com/shopsphere/util/UrlConnectionDemo.java` |
| **Internationalization (i18n)** | ResourceBundle, Locale switching (`messages_en.properties`, `messages_hi.properties`), `I18nFilter` | `web/src/main/resources/messages_*.properties` & `I18nFilter.java` |
| **Servlet Lifecycle** | `init()`, `service()`, `doGet()`, `doPost()`, `destroy()` in all controller servlets | `web/src/main/java/com/shopsphere/controller/` (`ProductServlet.java`, `CartServlet.java`) |
| **ServletConfig & ServletContext** | Application-wide attributes, configuration parameters, container metadata | `web/src/main/java/com/shopsphere/listener/AppListener.java` |
| **Session Tracking** | `HttpSession` managing login identity, role permissions, active session counts | `web/src/main/java/com/shopsphere/controller/LoginServlet.java` |
| **Cookies** | Non-sensitive cookies: `savedEmail` (Remember Me) and `recentProducts` (Recently viewed) | `web/src/main/java/com/shopsphere/controller/ProductServlet.java` |
| **Event Handling & Listeners** | `ServletContextListener` and `HttpSessionListener` tracking startup and active sessions | `web/src/main/java/com/shopsphere/listener/AppListener.java` |
| **Servlet Filters** | `AuthenticationFilter` (customer pages), `AdminFilter` (/admin/*), `I18nFilter` (Locale & UTF-8) | `web/src/main/java/com/shopsphere/filter/` |
| **JSP Architecture & Directives** | `<%@ page ... %>`, `<%@ include ... %>`, `<%@ taglib ... %>` | `web/src/main/webapp/fragments/header.jsp`, `navbar.jsp` |
| **JSP Elements (Declarations, Scriptlets, Expressions)** | Dedicated syllabus demonstration page with `<%! ... %>`, `<% ... %>`, `<%= ... %>`, `<%-- ... --%>` | `web/src/main/webapp/jsp-demo.jsp` |
| **JSP Expression Language (EL)** | Scoped access: `${product.name}`, `${sessionScope.user.name}`, `${calc.grandTotal}` | All frontend JSPs (`index.jsp`, `products.jsp`, `cart.jsp`) |
| **JSP Fragments** | Reusable modular components: header, navbar, footer, alerts, sidebar | `web/src/main/webapp/fragments/` |
| **JSP Tag Files** | Custom component tags: `<tags:productCard />` and `<tags:rating />` | `web/src/main/webapp/WEB-INF/tags/` |
| **JSTL Core Tags** | `<c:forEach>`, `<c:if>`, `<c:choose>`, `<c:when>`, `<c:otherwise>`, `<c:set>` | Throughout all customer and administrative JSP pages |
| **JSTL SQL Tags** | Demonstration of `<sql:setDataSource>`, `<sql:query>`, `<sql:param>` | `web/src/main/webapp/jstl-sql-demo.jsp` |
| **JSTL XML Tags** | Demonstration of `<x:parse>`, `<x:out>`, `<x:forEach>` with XPath queries | `web/src/main/webapp/jstl-xml-demo.jsp` |
| **JSTL Formatting & Functions** | `<fmt:formatNumber>`, `<fmt:formatDate>`, `fn:length` | `cart.jsp`, `order-details.jsp`, `productCard.tag` |
| **Java Applets (Historical Context)** | Comprehensive historical documentation and migration path analysis | Section below and in project documentation |

---

## Note on Java Applets in the Modern Ecosystem

The RTU Advanced Java syllabus includes Java Applets. Applets were historically used for executing interactive Java client programs embedded in web browsers via the `<applet>` HTML tag.
- In **Java 9**, Applets were officially deprecated (`@Deprecated(since="9")`).
- In **Java 11**, the Applet API was deprecated for removal.
- In **Java 17+ / Java 26**, browser NPAPI plugins have been completely removed across all modern browsers (Chrome, Edge, Firefox, Safari).
- **ShopSphere Architecture Choice:** Rather than relying on non-functional applet browser plugins, ShopSphere implements the complete administrative and GUI client functionality via a modern **Java Swing Desktop Application** and **Jakarta Servlet/JSP Web Client**, while fully documenting the historical applet architecture for student viva examination.
