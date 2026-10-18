# ShopSphere — RTU Advanced Java Enterprise E-Commerce System

ShopSphere is a full-featured, multi-tiered Java Enterprise E-Commerce Management System engineered for **RTU Advanced Java / Java Enterprise laboratory evaluations**, college capstone demonstrations, and practical software engineering practice.

The project combines a responsive customer-facing web store, an administrator web control panel, a standalone **Java Swing desktop management application**, and distributed **Java Socket Networking** and **Java RMI** subsystems.

---

## 0. Project Status

- [x] Multi-module Maven build — **BUILD SUCCESS** (5/5 modules, JUnit `Tests run: 4, Failures: 0, Errors: 0`)
- [x] MySQL schema imported — `shopsphere` DB (12 tables, 3 users, 14 products, 5 categories)
- [x] Deployed to Apache Tomcat 10.1.59 and smoke-tested end-to-end on `http://localhost:8080/shopsphere/`
- [ ] Public GitHub repository — pending

### Quick Start
```powershell
# 1. Import the database
mysql -u root -p < "database/shopsphere.sql"

# 2. Point the app at your MySQL instance
#    edit web/src/main/resources/db.properties  (or set SHOPSPHERE_DB_USER / SHOPSPHERE_DB_PASSWORD)

# 3. Build everything
.\mvnw.cmd clean package

# 4. Deploy the WAR and open the storefront
Copy-Item "web\target\shopsphere.war" "<TOMCAT_HOME>\webapps\"
#    -> http://localhost:8080/shopsphere/
```

---

## 1. Technology Stack

- **Core Platform:** Java SE 17+ (JDK 26 tested)
- **Web Container:** Apache Tomcat 10.1+ (verified on 10.1.59; Jakarta EE / Servlet 6.x)
- **Web Framework:** Jakarta Servlet API 6.1.0, JSP, Expression Language (EL), JSTL 3.0
- **Database:** MySQL 8.0+
- **Persistence:** JDBC 4.2 with `PreparedStatement`, transaction handling, and connection pooling
- **Desktop GUI:** Java Swing & AWT (MVC Architecture with Pluggable Look and Feel)
- **Networking:** `java.net.ServerSocket` and `java.net.Socket` (multi-threaded TCP streaming)
- **Distributed Computing:** Java RMI (Remote Method Invocation) with RMI Registry
- **Serialization:** Java Object Serialization (`Serializable`, `ObjectOutputStream`, `ObjectInputStream`)
- **Security:** SHA-256 cryptographic password digest hashing
- **Build System:** Apache Maven 3.6+ (Multi-Module parent POM with Maven Wrapper `mvnw`)

---

## 2. Project Modules & Directory Layout

```
ShopSphere/
├── pom.xml                               # Root multi-module Maven configuration
├── README.md                             # Master project documentation & user guide
├── PROGRESS.md                           # Development and verification checklist
├── .gitignore                            # Source control exclusions
├── mvnw / mvnw.cmd                       # Maven Wrapper executables
├── database/
│   ├── shopsphere.sql                    # Production MySQL schema, indexes & seed data
│   └── README.md                         # Database import & configuration instructions
├── docs/
│   ├── system-architecture.md            # Mermaid diagrams & layered design
│   ├── database-design.md                # ER Diagram & complete table data dictionaries
│   ├── rtu-syllabus-mapping.md           # Exhaustive RTU syllabus topic-to-file matrix
│   ├── test-cases.md                     # Comprehensive test scenarios & verification results
│   ├── viva-questions.md                 # College viva questions and model answers
│   └── project-report.md                 # Formal academic capstone project report
├── web/                                  # Web Application Module (WAR)
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/shopsphere/
│       │   ├── controller/               # Customer & Admin Servlet Controllers
│       │   ├── service/                  # Business Logic Layer (Auth, Checkout, Products)
│       │   ├── dao/                      # JDBC Data Access Objects
│       │   ├── model/                    # JavaBean Domain Models (Serializable)
│       │   ├── filter/                   # AuthFilter, AdminFilter, I18nFilter
│       │   ├── listener/                 # AppListener (ServletContext & HttpSession)
│       │   └── util/                     # DBUtil, PasswordUtil, ValidationUtil
│       ├── resources/
│       │   ├── db.properties             # Database connection credentials
│       │   ├── messages_en.properties    # English localization bundle
│       │   └── messages_hi.properties    # Hindi localization bundle
│       └── webapp/
│           ├── index.jsp                 # Storefront Homepage & Hero Banner
│           ├── products.jsp              # Catalog with search, filter, sort & pagination
│           ├── product-details.jsp       # Product specs, reviews, and Add to Cart
│           ├── login.jsp & register.jsp  # Customer authentication forms
│           ├── cart.jsp                  # Shopping cart & coupon calculator
│           ├── checkout.jsp              # Address selection, payment & order submission
│           ├── order-success.jsp         # Order confirmation & receipt
│           ├── orders.jsp & details.jsp  # Order history & visual tracking timeline
│           ├── profile.jsp & wishlist.jsp# Customer account & saved items
│           ├── jsp-demo.jsp              # Syllabus demo of Declarations, Scriptlets & EL
│           ├── jstl-sql-demo.jsp         # Syllabus demo of JSTL SQL Tags
│           ├── jstl-xml-demo.jsp         # Syllabus demo of JSTL XML Tags & XPath
│           ├── admin-login.jsp           # Dedicated Admin Login Portal
│           ├── admin/                    # Admin Dashboard, Products, Categories, Orders
│           ├── fragments/                # Header, Navbar, Footer, Alerts, Sidebar
│           ├── WEB-INF/
│           │   ├── web.xml               # Servlet 6.0 deployment descriptor
│           │   └── tags/                 # Custom Tag Files: productCard.tag, rating.tag
│           ├── css/style.css             # Modern custom CSS stylesheet
│           └── js/main.js                # Interactive JavaScript helpers
├── swing-admin/                          # Desktop Java Swing Administration (JAR)
│   └── src/main/java/com/shopsphere/admin/
│       ├── AdminMain.java                # Entry point with Pluggable Look & Feel
│       ├── dao/AdminDAO.java             # Direct JDBC data access for desktop
│       └── view/                         # LoginFrame, Dashboard, ProductFrame, OrderFrame...
├── network/                              # TCP Socket Client-Server Module (JAR)
│   └── src/main/java/com/shopsphere/network/
│       ├── server/InventoryServer.java   # ServerSocket on port 8888
│       ├── client/InventoryClient.java   # Socket client demonstrating serialized DTOs
│       └── model/ProductDTO.java         # Serializable Data Transfer Object
└── rmi/                                  # Distributed Java RMI Module (JAR)
    └── src/main/java/com/shopsphere/rmi/
        ├── remote/InventoryService.java  # java.rmi.Remote interface
        ├── server/RmiServerMain.java     # RMI Registry bootstrap on port 1099
        └── client/RmiClientMain.java     # RMI Client executing remote invocations
```

---

## 3. Database Setup & Initialization

### Step 1: Initialize Database
Ensure MySQL Server is running, then execute the initialization script:
```bash
mysql -u root -p < "d:/Java Project - ShopSphere/database/shopsphere.sql"
```
Or open `database/shopsphere.sql` inside **MySQL Workbench** and execute the script.

### Step 2: Configure Database Credentials
Adjust database credentials in `web/src/main/resources/db.properties` if your MySQL password differs from `root`:
```properties
db.url=jdbc:mysql://localhost:3306/shopsphere?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.user=root
db.password=YOUR_PASSWORD
```
You may also set environment variables:
`$env:SHOPSPHERE_DB_USER="root"` and `$env:SHOPSPHERE_DB_PASSWORD="yourpassword"`.

---

## 4. Pre-Seeded Demonstration Accounts

All passwords are automatically pre-hashed with SHA-256 digests:

| Portal | Email | Plaintext Password | Role |
|---|---|---|---|
| **Admin Web & Swing Portal** | `admin@shopsphere.com` | `admin123` | `ADMIN` |
| **Customer Storefront** | `john.doe@example.com` | `customer123` | `CUSTOMER` |
| **Customer Storefront** | `priya.sharma@example.com` | `customer123` | `CUSTOMER` |

---

## 5. Building the Complete System

Compile and package all 5 modules simultaneously using the included Maven Wrapper:
```bash
# Build parent project and all submodules
.\mvnw.cmd clean package

# Run automated unit tests
.\mvnw.cmd test
```
The build produces:
- `web/target/shopsphere.war` (Web application archive for Tomcat)
- `swing-admin/target/shopsphere-swing-admin-1.0.0.jar`
- `network/target/shopsphere-network-1.0.0.jar`
- `rmi/target/shopsphere-rmi-1.0.0.jar`

---

## 6. How to Run Each Module

### 6.1 Running the Web Application on Apache Tomcat 10.1
1. Copy `web/target/shopsphere.war` into Tomcat's `webapps/` folder:
   ```powershell
   Copy-Item "d:\Java Project - ShopSphere\web\target\shopsphere.war" -Destination "D:\apache-tomcat-10.1.59-windows-x64\apache-tomcat-10.1.59\webapps\"
   ```
2. Start Tomcat:
   ```powershell
   & "D:\apache-tomcat-10.1.59-windows-x64\apache-tomcat-10.1.59\bin\startup.bat"
   ```
3. Open your web browser and navigate to:
   - **Storefront:** [http://localhost:8080/shopsphere/](http://localhost:8080/shopsphere/)
   - **Admin Portal:** [http://localhost:8080/shopsphere/admin-login.jsp](http://localhost:8080/shopsphere/admin-login.jsp)
   - **JSP Demo:** [http://localhost:8080/shopsphere/jsp-demo.jsp](http://localhost:8080/shopsphere/jsp-demo.jsp)
   - **JSTL SQL Demo:** [http://localhost:8080/shopsphere/jstl-sql-demo.jsp](http://localhost:8080/shopsphere/jstl-sql-demo.jsp)
   - **JSTL XML Demo:** [http://localhost:8080/shopsphere/jstl-xml-demo.jsp](http://localhost:8080/shopsphere/jstl-xml-demo.jsp)

### 6.2 Running the Java Swing Desktop Administration Application
Run the packaged JAR with its dependencies:
```powershell
java -cp "swing-admin/target/shopsphere-swing-admin-1.0.0.jar;C:\Users\DELL\.m2\repository\com\mysql\mysql-connector-j\8.3.0\mysql-connector-j-8.3.0.jar" com.shopsphere.admin.AdminMain
```
Or via Maven:
```powershell
.\mvnw.cmd exec:java -pl swing-admin -Dexec.mainClass="com.shopsphere.admin.AdminMain"
```

### 6.3 Running the Java Socket Networking Demonstration
1. Start the Inventory Server in Terminal 1:
   ```powershell
   java -cp "network/target/shopsphere-network-1.0.0.jar;C:\Users\DELL\.m2\repository\com\mysql\mysql-connector-j\8.3.0\mysql-connector-j-8.3.0.jar" com.shopsphere.network.server.InventoryServer
   ```
2. In Terminal 2, run the Socket Client:
   ```powershell
   java -cp "network/target/shopsphere-network-1.0.0.jar" com.shopsphere.network.client.InventoryClient
   ```

### 6.4 Running the Java RMI Distributed Computing Demonstration
1. Start the RMI Server (which launches the internal RMI registry on port 1099) in Terminal 1:
   ```powershell
   java -cp "rmi/target/shopsphere-rmi-1.0.0.jar;C:\Users\DELL\.m2\repository\com\mysql\mysql-connector-j\8.3.0\mysql-connector-j-8.3.0.jar" com.shopsphere.rmi.server.RmiServerMain
   ```
2. In Terminal 2, run the RMI Client:
   ```powershell
   java -cp "rmi/target/shopsphere-rmi-1.0.0.jar" com.shopsphere.rmi.client.RmiClientMain
   ```

---

## 7. Demo Promo Coupons
Test the checkout discounts with these promo codes:
- `WELCOME10`: 10% discount on orders over ₹500
- `FESTIVE20`: 20% discount on orders over ₹2,000 (up to ₹1,500 off)
- `FLAT500`: Flat ₹500 discount on orders over ₹3,000

---

## 8. Verification & Test Cases
All 22 test scenarios across build verification, authentication, product browsing, shopping cart, transactional checkout, admin filtering, Java Swing, TCP Sockets, and Java RMI have been executed and verified. See [`docs/test-cases.md`](docs/test-cases.md) for detailed execution reports.
