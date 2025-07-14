INSERT INTO products (id, name, description, category_id, price, quantity, is_active, created_at, updated_at, uuid) VALUES
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
                (27, 'DJI Avata 2', 'FPV Drone, 4K Stabilized Video, Motion Controller', 10, 999.00, 10, 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID());
ALTER TABLE products AUTO_INCREMENT = 28;