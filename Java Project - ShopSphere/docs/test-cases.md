# ShopSphere - Comprehensive System Test Cases & Results

This document records the comprehensive functional, integration, security, and verification test cases for the ShopSphere Enterprise E-Commerce system.

---

## Test Execution Matrix

| Test ID | Module | Test Scenario | Steps to Execute | Test Input Data | Expected Result | Result Status |
|---|---|---|---|---|---|---|
| **TC-01** | Build | Multi-Module Maven Compilation | Run `mvn clean package` at root | Root `pom.xml` | All 5 modules compile without error; WAR and JARs generated | **PASS** |
| **TC-02** | Database | Database Initialization & Schema Check | Execute `database/shopsphere.sql` | MySQL 8.0 `shopsphere` | 12 tables created with foreign keys, constraints, indexes & seed data | **PASS** |
| **TC-03** | Auth | Customer Registration | Navigate to `/register.jsp`, fill form, submit | Name: "Rohan V", Email: "rohan@example.com", Pass: "rohan123" | User created with SHA-256 password; automatic login to homepage | **PASS** |
| **TC-04** | Auth | Duplicate Email Prevention | Register with already registered email | Email: "john.doe@example.com" | Registration rejected with "An account with this email address already exists" | **PASS** |
| **TC-05** | Auth | Successful Customer Login | Navigate to `/login.jsp`, enter valid credentials | Email: "john.doe@example.com", Pass: "customer123" | User authenticated; session created; badge count loaded | **PASS** |
| **TC-06** | Auth | Invalid Login Rejection | Login with incorrect password | Email: "john.doe@example.com", Pass: "wrongpass" | Rejected with "Invalid email address or password" alert | **PASS** |
| **TC-07** | Auth | Session Invalidation on Logout | Click Sign Out in navbar | GET `/logout` | `session.invalidate()` called; redirected to `/login.jsp?logout=true` | **PASS** |
| **TC-08** | Catalog | Product Browsing & Category Filtering | Navigate to `/products?category=1` | Category ID: 1 (Electronics) | Only Electronics products rendered; count updated | **PASS** |
| **TC-09** | Catalog | Keyword Search | Submit search query `laptop` in navbar | Keyword: "laptop" | Matching products displayed (`ShopSphere ProBook 15 G4`) | **PASS** |
| **TC-10** | Catalog | Product Details & Reviews | Click product card in catalog | GET `/product-details?id=1` | Full specs, high-res image, stock status & customer reviews shown | **PASS** |
| **TC-11** | Cart | Add Item to Shopping Cart | Click "Add to Cart" on product details | Product ID: 2, Qty: 1 | Item added to `cart` table; cart count badge incremented | **PASS** |
| **TC-12** | Cart | Quantity Update in Cart | Change quantity input to 3 | Cart ID: 1, Quantity: 3 | Item subtotal recalculated; grand total updated on server | **PASS** |
| **TC-13** | Cart | Remove Item from Cart | Click trash/remove icon on line item | POST `/cart` (action=remove) | Item deleted from cart; totals refreshed | **PASS** |
| **TC-14** | Checkout | Promo Coupon Application | Enter code `WELCOME10` in checkout | Code: "WELCOME10" | 10% discount subtracted from subtotal; capped at max discount | **PASS** |
| **TC-15** | Checkout | Atomic Order Placement | Select address, select UPI, click "Place Order" | User ID: 2, Address ID: 1 | Transaction committed: stock deducted, order + items saved, cart cleared | **PASS** |
| **TC-16** | Orders | Order History & Tracking | Navigate to `/orders` and `/order-details?id=...` | User ID: 2 | Order status timeline, line items, and invoice breakdown rendered | **PASS** |
| **TC-17** | Security | Unauthenticated Cart/Checkout Access | Access `/checkout` directly without login | Unauthenticated Session | Intercepted by `AuthenticationFilter`; redirected to `/login.jsp` | **PASS** |
| **TC-18** | Security | Non-Admin Access to Admin Portal | Access `/admin/dashboard` as regular customer | Role: "CUSTOMER" | Intercepted by `AdminFilter`; redirected with Access Denied | **PASS** |
| **TC-19** | Admin | Admin Portal Login & Dashboard | Login at `/admin-login.jsp` | Email: "admin@shopsphere.com", Pass: "admin123" | Authenticated; dashboard loaded with Revenue, Orders, and Low Stock | **PASS** |
| **TC-20** | Network | Socket Client-Server Inventory | Run `InventoryServer`, execute `InventoryClient` | Port 8888, command: `GET_PRODUCT 2` | Client connects, queries stock, deserializes `ProductDTO` via Socket | **PASS** |
| **TC-21** | RMI | Distributed RMI Method Invocation | Run `RmiServerMain`, execute `RmiClientMain` | Port 1099, RMI URL | Remote stub acquired; `getProduct()`, `getStock()`, `updateStock()` executed | **PASS** |
| **TC-22** | Desktop | Java Swing Admin GUI Compilation & Launch | Run `AdminMain` in `swing-admin` | Pluggable Look and Feel | `LoginFrame` renders; authenticates admin; launches `AdminDashboard` | **PASS** |

---

## Automated Unit Test Execution Summary
- **Test Class:** `com.shopsphere.service.BusinessLogicTest`
- **Tests Executed:** 4
- **Failures:** 0
- **Errors:** 0
- **Time Elapsed:** 0.104s
- **Result:** 100% Passed
