# ShopSphere Database Setup & Configuration Guide

## 1. Database Specifications
- **Database Engine:** MySQL 8.0+
- **Database Name:** `shopsphere`
- **Character Set:** `utf8mb4`
- **Collation:** `utf8mb4_unicode_ci`

## 2. Table Summary
| Table Name | Primary Key | Description |
|---|---|---|
| `users` | `user_id` | Authentication & profile (ADMIN, CUSTOMER) |
| `addresses` | `address_id` | Customer shipping & billing addresses |
| `categories` | `category_id` | Product categories with active status |
| `products` | `product_id` | Product inventory, pricing, discount, and stock |
| `cart` | `cart_id` | Persistent customer cart with quantities |
| `wishlist` | `wishlist_id` | Customer wishlist / saved items |
| `orders` | `order_id` | Customer orders, statuses, and totals |
| `order_items` | `order_item_id` | Line items belonging to an order |
| `payments` | `payment_id` | Mock payment transaction records |
| `reviews` | `review_id` | Product customer reviews (1-5 stars) |
| `coupons` | `coupon_id` | Promotional discount coupons (PERCENTAGE / FLAT) |
| `coupon_usage`| `usage_id` | Audit table recording coupon usage per order |

## 3. How to Initialize the Database

### Option A: Using MySQL Command Line Client
```bash
mysql -u root -p < "d:/Java Project - ShopSphere/database/shopsphere.sql"
```
Or interactively:
```sql
mysql -u root -p
source d:/Java Project - ShopSphere/database/shopsphere.sql;
```

### Option B: Using MySQL Workbench
1. Open **MySQL Workbench**.
2. Connect to your Local MySQL 8.0 instance.
3. Click **File -> Open SQL Script...** and select `database/shopsphere.sql`.
4. Click the **Execute (lightning bolt)** button to run all statements.
5. In the Navigator schemas panel, right-click and choose **Refresh All**. Verify `shopsphere` schema is populated with 12 tables and seed data.

## 4. Default Seed Credentials
All sample passwords are automatically pre-hashed with SHA-256 for maximum security:

| Role | Email | Plaintext Password | Hashed Password (SHA-256) |
|---|---|---|---|
| **Administrator** | `admin@shopsphere.com` | `admin123` | `240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9` |
| **Customer** | `john.doe@example.com` | `customer123` | `5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8` |
| **Customer** | `priya.sharma@example.com` | `customer123` | `5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8` |

## 5. Web Application Configuration
Database credentials can be adjusted in:
`web/src/main/resources/db.properties`

```properties
db.driver=com.mysql.cj.jdbc.Driver
db.url=jdbc:mysql://localhost:3306/shopsphere?useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=UTC
db.user=root
db.password=root
```
Environment variables `SHOPSPHERE_DB_USER` and `SHOPSPHERE_DB_PASSWORD` can also be used to override these settings without modifying files.
