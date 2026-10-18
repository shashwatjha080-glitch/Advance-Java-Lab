-- =====================================================================
-- ShopSphere E-Commerce System - Database Initialization Script
-- Target Database: MySQL 8.0+
-- Compatible with RTU Advanced Java / Enterprise Java Specification
-- =====================================================================

DROP DATABASE IF EXISTS shopsphere;
CREATE DATABASE shopsphere CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE shopsphere;

-- ---------------------------------------------------------------------
-- Table: users
-- Roles: 'ADMIN', 'CUSTOMER'
-- Status: 'ACTIVE', 'INACTIVE'
-- Default Admin: admin@shopsphere.com / admin123
-- Default Customer: john.doe@example.com / customer123
-- Passwords stored as SHA-256 hashes
-- ---------------------------------------------------------------------
CREATE TABLE users (
    user_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    mobile VARCHAR(20),
    role VARCHAR(20) NOT NULL DEFAULT 'CUSTOMER',
    status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_user_email (email),
    INDEX idx_user_role (role)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Table: addresses
-- ---------------------------------------------------------------------
CREATE TABLE addresses (
    address_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    address_line VARCHAR(255) NOT NULL,
    city VARCHAR(100) NOT NULL,
    state VARCHAR(100) NOT NULL,
    pincode VARCHAR(20) NOT NULL,
    address_type VARCHAR(20) DEFAULT 'HOME',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    INDEX idx_address_user (user_id)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Table: categories
-- ---------------------------------------------------------------------
CREATE TABLE categories (
    category_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description TEXT,
    status BOOLEAN DEFAULT TRUE,
    INDEX idx_category_status (status)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Table: products
-- ---------------------------------------------------------------------
CREATE TABLE products (
    product_id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    name VARCHAR(255) NOT NULL,
    brand VARCHAR(100),
    description TEXT,
    price DECIMAL(10,2) NOT NULL,
    discount DECIMAL(5,2) DEFAULT 0.00,
    stock INT NOT NULL DEFAULT 0,
    image_url VARCHAR(500),
    status BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (category_id) REFERENCES categories(category_id) ON DELETE RESTRICT,
    INDEX idx_product_category (category_id),
    INDEX idx_product_status (status),
    INDEX idx_product_price (price)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Table: cart
-- ---------------------------------------------------------------------
CREATE TABLE cart (
    cart_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_cart_user_product (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Table: wishlist
-- ---------------------------------------------------------------------
CREATE TABLE wishlist (
    wishlist_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE KEY uk_wishlist_user_product (user_id, product_id),
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Table: orders
-- Order Status: 'PLACED', 'CONFIRMED', 'SHIPPED', 'DELIVERED', 'CANCELLED'
-- Payment Status: 'PAID', 'PENDING', 'FAILED'
-- ---------------------------------------------------------------------
CREATE TABLE orders (
    order_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    address_id INT,
    total_amount DECIMAL(10,2) NOT NULL,
    discount DECIMAL(10,2) DEFAULT 0.00,
    payment_method VARCHAR(50) DEFAULT 'CREDIT_CARD',
    payment_status VARCHAR(50) DEFAULT 'PENDING',
    order_status VARCHAR(50) DEFAULT 'PLACED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE RESTRICT,
    FOREIGN KEY (address_id) REFERENCES addresses(address_id) ON DELETE SET NULL,
    INDEX idx_orders_user (user_id),
    INDEX idx_orders_status (order_status)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Table: order_items
-- ---------------------------------------------------------------------
CREATE TABLE order_items (
    order_item_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10,2) NOT NULL,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE RESTRICT,
    INDEX idx_items_order (order_id)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Table: payments
-- ---------------------------------------------------------------------
CREATE TABLE payments (
    payment_id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    payment_method VARCHAR(50) NOT NULL,
    transaction_reference VARCHAR(100) NOT NULL,
    amount DECIMAL(10,2) NOT NULL,
    payment_status VARCHAR(50) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Table: reviews
-- ---------------------------------------------------------------------
CREATE TABLE reviews (
    review_id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    product_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 5),
    review_text TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE CASCADE,
    FOREIGN KEY (product_id) REFERENCES products(product_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Table: coupons
-- ---------------------------------------------------------------------
CREATE TABLE coupons (
    coupon_id INT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    discount_type VARCHAR(20) NOT NULL DEFAULT 'PERCENTAGE', -- 'PERCENTAGE', 'FLAT'
    discount_value DECIMAL(10,2) NOT NULL,
    minimum_order DECIMAL(10,2) DEFAULT 0.00,
    maximum_discount DECIMAL(10,2) DEFAULT 1000.00,
    expiry_date DATE NOT NULL,
    status BOOLEAN DEFAULT TRUE,
    INDEX idx_coupon_code (code)
) ENGINE=InnoDB;

-- ---------------------------------------------------------------------
-- Table: coupon_usage
-- ---------------------------------------------------------------------
CREATE TABLE coupon_usage (
    usage_id INT AUTO_INCREMENT PRIMARY KEY,
    coupon_id INT NOT NULL,
    user_id INT NOT NULL,
    order_id INT NOT NULL,
    used_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (coupon_id) REFERENCES coupons(coupon_id) ON DELETE RESTRICT,
    FOREIGN KEY (user_id) REFERENCES users(user_id) ON DELETE RESTRICT,
    FOREIGN KEY (order_id) REFERENCES orders(order_id) ON DELETE CASCADE
) ENGINE=InnoDB;

-- =====================================================================
-- SEED DATA
-- SHA-256 of "admin123"    = 240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9
-- SHA-256 of "customer123" = 5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8
-- =====================================================================

INSERT INTO users (name, email, password, mobile, role, status) VALUES
('ShopSphere Admin', 'admin@shopsphere.com', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', '9876543210', 'ADMIN', 'ACTIVE'),
('John Doe', 'john.doe@example.com', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', '9123456789', 'CUSTOMER', 'ACTIVE'),
('Priya Sharma', 'priya.sharma@example.com', '5e884898da28047151d0e56f8dc6292773603d0d6aabbdd62a11ef721d1542d8', '9811223344', 'CUSTOMER', 'ACTIVE');

INSERT INTO addresses (user_id, address_line, city, state, pincode, address_type) VALUES
(2, 'Flat 402, Royal Palms, MG Road', 'Bengaluru', 'Karnataka', '560001', 'HOME'),
(2, 'Tech Park Phase 2, Whitefield', 'Bengaluru', 'Karnataka', '560066', 'WORK'),
(3, 'House 12, Malviya Nagar', 'Jaipur', 'Rajasthan', '302017', 'HOME');

INSERT INTO categories (name, description, status) VALUES
('Electronics', 'Smartphones, Laptops, Audio Gear, and Wearables', TRUE),
('Fashion', 'Men and Women Apparels, Footwear, and Accessories', TRUE),
('Home & Kitchen', 'Modern Furniture, Smart Appliances, and Decor', TRUE),
('Books & Study', 'Engineering, Computer Science, Literature, and Exams', TRUE),
('Beauty & Wellness', 'Skincare, Fragrances, Grooming, and Nutrition', TRUE);

INSERT INTO products (category_id, name, brand, description, price, discount, stock, image_url, status) VALUES
-- Electronics
(1, 'ShopSphere ProBook 15 G4', 'SphereTech', 'Intel Core i7 13th Gen, 16GB DDR5 RAM, 1TB NVMe SSD, 15.6" IPS FHD 144Hz Display. Built for enterprise and software engineering.', 74999.00, 10.00, 25, 'https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=600&auto=format&fit=crop&q=80', TRUE),
(1, 'Aura ANC Wireless Headphones', 'SoundSphere', 'Hybrid Active Noise Cancellation, 40mm Neodymium Drivers, 50h Battery Life, Bluetooth 5.3 with multipoint pairing.', 6499.00, 15.00, 50, 'https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=600&auto=format&fit=crop&q=80', TRUE),
(1, 'UltraVision 4K OLED Smart Monitor', 'SphereTech', '32-inch 4K UHD OLED, HDR10+, 120Hz refresh rate, USB-C 90W power delivery, color accurate delta E < 1.', 39999.00, 8.00, 15, 'https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=600&auto=format&fit=crop&q=80', TRUE),
(1, 'NeoTrack Pro Smartwatch', 'AeroFit', 'AMOLED Always-On Display, PPG Heart Rate, SpO2, Sleep Tracking, 10-day battery life with IP68 water resistance.', 4499.00, 20.00, 80, 'https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=600&auto=format&fit=crop&q=80', TRUE),

-- Fashion
(2, 'Classic Oxford Slim-Fit Shirt', 'UrbanSphere', '100% Breathable Egyptian Cotton, spread collar, timeless tailoring suitable for corporate and formal wear.', 1799.00, 12.00, 60, 'https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=600&auto=format&fit=crop&q=80', TRUE),
(2, 'AeroStride Lightweight Running Shoes', 'AeroFit', 'Engineered mesh upper, responsive EVA cushioning midsole, non-slip rubber traction outsole.', 3299.00, 18.00, 45, 'https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=600&auto=format&fit=crop&q=80', TRUE),
(2, 'Voyager Vintage Leather Backpack', 'UrbanSphere', 'Handcrafted genuine top-grain leather with padded 15.6" laptop compartment and antique brass buckles.', 4999.00, 10.00, 30, 'https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=600&auto=format&fit=crop&q=80', TRUE),

-- Home & Kitchen
(3, 'Barista Express Espresso Machine', 'CaffèSphere', '15-Bar Italian Pump, precise thermo-block heating, stainless steel steam wand for velvety micro-foam latte.', 12499.00, 15.00, 20, 'https://images.unsplash.com/photo-1517668808822-9ebb02f2a0e6?w=600&auto=format&fit=crop&q=80', TRUE),
(3, 'Nordic Minimalist Ceramic Dining Set', 'ArtisanHome', '16-piece stoneware dinnerware set, microwave and dishwasher safe, matte reactive glaze finish.', 3799.00, 5.00, 40, 'https://images.unsplash.com/photo-1615486511484-92e172cc4fe0?w=600&auto=format&fit=crop&q=80', TRUE),
(3, 'ErgoComfort Memory Foam Pillow', 'ArtisanHome', 'Orthopedic contour cooling gel memory foam pillow with breathable hypoallergenic bamboo cover.', 1499.00, 25.00, 70, 'https://images.unsplash.com/photo-1584100936595-c0654b55a2e2?w=600&auto=format&fit=crop&q=80', TRUE),

-- Books & Study
(4, 'Advanced Java Programming & Distributed Systems', 'RTU Academic Press', 'Comprehensive handbook covering Swing MVC, Socket Programming, RMI, JNDI, Servlets, JSP and Enterprise J2EE architecture.', 899.00, 10.00, 120, 'https://images.unsplash.com/photo-1532012164546-f432f2e3777a?w=600&auto=format&fit=crop&q=80', TRUE),
(4, 'Designing Data-Intensive Applications', 'OReilly Media', 'The definitive guide to distributed databases, reliable transactions, streaming architectures and scalability.', 1650.00, 15.00, 55, 'https://images.unsplash.com/photo-1544716278-ca5e3f4abd8c?w=600&auto=format&fit=crop&q=80', TRUE),

-- Beauty & Wellness
(5, 'Botanical Radiance Vitamin C Serum', 'DermaSphere', 'Pure 20% L-Ascorbic Acid with Hyaluronic Acid and Ferulic Acid for antioxidant brightening and even skin tone.', 999.00, 20.00, 95, 'https://images.unsplash.com/photo-1620916566398-39f1143ab7be?w=600&auto=format&fit=crop&q=80', TRUE),
(5, 'Artisan Sandalwood & Amber Eau De Parfum', 'SphereAroma', 'Exquisite 100ml fragrance featuring warm notes of Mysore sandalwood, amber, cardamom, and fresh bergamot.', 2999.00, 10.00, 35, 'https://images.unsplash.com/photo-1592945403244-b3fbafd7f539?w=600&auto=format&fit=crop&q=80', TRUE);

INSERT INTO coupons (code, discount_type, discount_value, minimum_order, maximum_discount, expiry_date, status) VALUES
('WELCOME10', 'PERCENTAGE', 10.00, 500.00, 500.00, '2027-12-31', TRUE),
('FESTIVE20', 'PERCENTAGE', 20.00, 2000.00, 1500.00, '2027-12-31', TRUE),
('FLAT500', 'FLAT', 500.00, 3000.00, 500.00, '2027-12-31', TRUE);

-- Initial sample order for demonstration
INSERT INTO orders (user_id, address_id, total_amount, discount, payment_method, payment_status, order_status) VALUES
(2, 1, 7499.00, 500.00, 'CREDIT_CARD', 'PAID', 'DELIVERED'),
(2, 2, 6499.00, 0.00, 'UPI', 'PAID', 'SHIPPED');

INSERT INTO order_items (order_id, product_id, quantity, price) VALUES
(1, 2, 1, 5524.15),
(1, 5, 1, 1583.12),
(2, 2, 1, 5524.15);

INSERT INTO payments (order_id, payment_method, transaction_reference, amount, payment_status) VALUES
(1, 'CREDIT_CARD', 'TXN_SP_908123891', 7499.00, 'SUCCESS'),
(2, 'UPI', 'UPI_SP_554819203', 6499.00, 'SUCCESS');

INSERT INTO reviews (user_id, product_id, rating, review_text) VALUES
(2, 1, 5, 'Superb build quality! Handled my Java enterprise development and Docker workloads without breaking a sweat.'),
(3, 2, 5, 'Noise cancellation is outstanding. Very comfortable for long study hours and conference calls.'),
(2, 5, 4, 'Very crisp fabric and perfect fitting. Highly recommended.');
