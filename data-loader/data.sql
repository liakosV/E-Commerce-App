INSERT IGNORE INTO regions (id, name) VALUES
                                   (1,'ΑΝΑΤΟΛΙΚΗΣ ΜΑΚΕΔΟΝΙΑΣ ΚΑΙ ΘΡΑΚΗΣ'),
                                   (2,'ΑΤΤΙΚΗΣ'),
                                   (3,'ΒΟΡΕΙΟΥ ΑΙΓΑΙΟΥ'),
                                   (4,'ΔΥΤΙΚΗΣ ΕΛΛΑΔΑΣ'),
                                   (5,'ΔΥΤΙΚΗΣ ΜΑΚΕΔΟΝΙΑΣ'),
                                   (6,'ΗΠΕΙΡΟΥ'),
                                   (7,'ΘΕΣΣΑΛΙΑΣ'),
                                   (8,'ΙΟΝΙΩΝ ΝΗΣΩΝ'),
                                   (9,'ΚΕΝΤΡΙΚΗΣ ΜΑΚΕΔΟΝΙΑΣ'),
                                   (10,'ΚΡΗΤΗΣ'),
                                   (11,'ΝΟΤΙΟΥ ΑΙΓΑΙΟΥ'),
                                   (12,'ΠΕΛΟΠΟΝΝΗΣΟΥ'),
                                   (13,'ΣΤΕΡΕΑΣ ΕΛΛΑΔΑΣ');
ALTER TABLE regions AUTO_INCREMENT = 14;

INSERT IGNORE INTO roles (id, name, description) VALUES
                        (1, 'ADMIN', 'Administrator with full access to manage the platform'),
                        (2, 'CUSTOMER', 'Registered user who can browse and purchase products'),
                        (3, 'SELLER', 'Vendor with access to manage product listings and orders');
ALTER TABLE roles AUTO_INCREMENT = 4;

INSERT IGNORE INTO categories (id, name) VALUES
                            (1, 'Laptops'),
                            (2, 'Phones'),
                            (3, 'Tablets'),
                            (4, 'Smartwatches'),
                            (5, 'Headphones'),
                            (6, 'Accessories'),
                            (7, 'Gaming'),
                            (8, 'Networking'),
                            (9, 'Storage'),
                            (10, 'Drones');
ALTER TABLE categories AUTO_INCREMENT = 11;

INSERT IGNORE INTO products (id, name, description, category_id, price, quantity, is_active, created_at, updated_at, uuid) VALUES
                (1, 'Apple MacBook Air M3', '13.6-inch Retina Display, 256GB SSD, 8GB RAM, macOS', 1, 1099.99, 25, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (2, 'HP Envy x360', '15.6" Touchscreen, AMD Ryzen 7, 512GB SSD, 16GB RAM', 1, 899.99, 30, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (3, 'Lenovo IdeaPad 3', '14" FHD Display, Intel Core i5, 256GB SSD, 8GB RAM', 1, 549.99, 40, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),

                (4, 'iPhone 15 Pro', '6.1" OLED Display, A17 Pro Chip, 128GB, iOS 17', 2, 999.99, 50, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (5, 'Samsung Galaxy S24', '6.6" AMOLED, 256GB, Snapdragon 8 Gen 3, Android 14', 2, 899.99, 45, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (6, 'Xiaomi Redmi Note 13 Pro', '6.67" AMOLED, 128GB, Android 14', 2, 349.99, 60, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),

                (7, 'iPad Air M2', '10.9-inch Liquid Retina, 128GB, Wi-Fi, iPadOS', 3, 599.99, 35, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (8, 'Samsung Galaxy Tab S9 FE', '10.9" LCD, 128GB, Android 14', 3, 449.99, 28, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (9, 'Lenovo Tab M10', '10.1" HD Display, 64GB Storage, Android', 3, 199.99, 50, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),

                (10, 'Apple Watch Series 9', '45mm GPS, Blood Oxygen, Fitness, watchOS', 4, 429.00, 30, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (11, 'Samsung Galaxy Watch 6', '44mm AMOLED, Wear OS, Health Monitoring', 4, 349.99, 25, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (12, 'Fitbit Versa 4', 'Fitness Smartwatch, Heart Rate, Sleep Tracking', 4, 229.99, 35, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),

                (13, 'Sony WH-1000XM5', 'Wireless Noise-Cancelling Headphones, Bluetooth 5.2', 5, 399.99, 30, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (14, 'Apple AirPods Pro (2nd Gen)', 'ANC, Spatial Audio, MagSafe Case', 5, 249.99, 40, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (15, 'Bose QuietComfort 45', 'Over-Ear, Wireless, Noise-Canceling, 24h Battery', 5, 329.99, 20, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),

                (16, 'Anker PowerCore 20000', '20,000mAh Portable Charger, USB-C, Fast Charging', 6, 59.99, 80, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (17, 'Logitech MX Master 3S', 'Ergonomic Wireless Mouse, USB-C, Silent Clicks', 6, 99.99, 60, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (18, 'Ugreen USB-C Hub 9-in-1', 'HDMI, Ethernet, USB 3.0, PD 100W, SD/TF', 6, 69.99, 55, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),

                (19, 'Razer Blade 16', '16" 240Hz, RTX 4080, Intel i9, 32GB RAM, 1TB SSD', 7, 3199.00, 10, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (20, 'ASUS ROG Strix G17', '17.3" 144Hz, AMD Ryzen 9, RTX 4070, 16GB RAM', 7, 1899.00, 15, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (21, 'Elgato Stream Deck MK.2', '15 Customizable LCD Keys, Stream Control', 7, 149.99, 20, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),

                (22, 'TP-Link Archer AX11000', 'Tri-Band Wi-Fi 6 Router, 10Gbps Speed, Gaming Mode', 8, 349.99, 18, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (23, 'Netgear Nighthawk AX5400', 'Dual-Band Wi-Fi 6, 6 Streams, OFDMA, Beamforming', 8, 199.99, 25, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),

                (24, 'Samsung T7 Portable SSD 2TB', 'USB 3.2 Gen2, Up to 1050MB/s, Shock Resistant', 9, 189.99, 30, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (25, 'Western Digital Black SN850X 1TB', 'PCIe Gen4 NVMe, Up to 7300MB/s', 9, 129.99, 35, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),

                (26, 'DJI Mini 4 Pro', 'Lightweight Drone, 4K Camera, Obstacle Avoidance, 34min Flight', 10, 759.00, 12, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (27, 'DJI Avata 2', 'FPV Drone, 4K Stabilized Video, Motion Controller', 10, 999.00, 10, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (28, 'Dell XPS 13', '13.4" UHD+ Touch, Intel i7, 16GB RAM, 512GB SSD', 1, 1299.99, 20, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (29, 'Acer Swift 3', '14" FHD, AMD Ryzen 5, 8GB RAM, 512GB SSD', 1, 699.99, 35, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),

                (30, 'Google Pixel 8', '6.2" OLED, 128GB, Tensor G3, Android 14', 2, 799.99, 40, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (31, 'OnePlus 12', '6.8" AMOLED, 256GB, Snapdragon 8 Gen 3', 2, 849.99, 30, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),

                (32, 'Amazon Fire HD 10', '10.1" FHD, 64GB, Android-based Fire OS', 3, 149.99, 50, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (33, 'Microsoft Surface Pro 9', '13" Touch, i5, 8GB, 256GB SSD, Windows 11', 3, 1099.99, 20, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),

                (34, 'Garmin Venu 3', 'AMOLED, GPS, Fitness & Sleep Tracking', 4, 449.99, 25, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (35, 'Amazfit GTR 4', '1.43" AMOLED, GPS, Health Monitoring', 4, 199.99, 40, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),

                (36, 'Sennheiser Momentum 4', 'Wireless Over-Ear Headphones, ANC', 5, 379.99, 15, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (37, 'JBL Tune 760NC', 'Over-Ear, Noise-Canceling, Bluetooth', 5, 129.99, 50, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),

                (38, 'Kingston USB-C Docking Station', 'Dual Monitor, Gigabit Ethernet, SD Reader', 6, 89.99, 30, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (39, 'Razer Basilisk V3', 'Wired Gaming Mouse, RGB, 26K DPI', 6, 69.99, 45, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),

                (40, 'Corsair K95 RGB Platinum XT', 'Mechanical Keyboard, Cherry MX Blue', 7, 199.99, 25, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (41, 'Blue Yeti X Microphone', 'USB, Cardioid, Omnidirectional, Streaming Mic', 7, 169.99, 30, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),

                (42, 'ASUS RT-AX88U', 'Dual-Band WiFi 6 Router, 6000 Mbps, AiMesh', 8, 299.99, 20, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (43, 'Google Nest WiFi Pro', 'Wi-Fi 6E Mesh Router, 2-Pack', 8, 329.99, 15, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),

                (44, 'Crucial X9 Pro 1TB', 'Portable SSD, USB-C, Up to 1050MB/s', 9, 119.99, 40, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (45, 'Samsung 990 Pro 2TB', 'PCIe 4.0 NVMe, Up to 7450MB/s', 9, 239.99, 20, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),

                (46, 'Autel EVO Nano+', '4K RYYB Sensor, Obstacle Avoidance, 28min Flight', 10, 799.00, 12, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (47, 'Ryze Tello', 'Mini Drone with 720p Camera and DJI Tech', 10, 99.99, 40, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),

                (48, 'Dell Inspiron 15', '15.6" FHD, Intel i5, 8GB RAM, 512GB SSD', 1, 649.99, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (49, 'Google Pixel Tablet', '11" Display, Tensor G2, 128GB, Android 14', 3, 499.99, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (50, 'Beats Studio Pro', 'Wireless Noise-Canceling Headphones, Spatial Audio', 5, 349.99, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (51, 'TP-Link Deco XE75 Pro', 'Wi-Fi 6E Mesh System, Tri-Band, 3-Pack', 8, 399.99, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID()),
                (52, 'DJI Air 3 Fly More Combo', 'Dual Camera Drone, 46min Flight, 4K HDR', 10, 1099.00, 0, 0, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID());
ALTER TABLE products AUTO_INCREMENT = 53;

INSERT IGNORE INTO users (id, created_at, updated_at, uuid, email, firstname, is_active, lastname, password, username, role_id, user_more_info_id) VALUES
                (1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID(), 'john@admin.com', 'John', 1, 'Doe', '$2a$11$A7Ng8DKYGZY5ODQoijdStO.6fqXUEeYAMMQ.gRQGRqWuZiHyAY7eq', 'JohnD', 1, NULL),
                (2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID(), 'alice@seller.com', 'Alice', 1, 'Wonderland', '$2a$11$A7Ng8DKYGZY5ODQoijdStO.6fqXUEeYAMMQ.gRQGRqWuZiHyAY7eq', 'AliceW', 3, NULL),
                (3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID(), 'bob@customer.com', 'Bob', 1, 'Builder', '$2a$11$A7Ng8DKYGZY5ODQoijdStO.6fqXUEeYAMMQ.gRQGRqWuZiHyAY7eq', 'BobB', 2, NULL);
ALTER TABLE users AUTO_INCREMENT = 2;