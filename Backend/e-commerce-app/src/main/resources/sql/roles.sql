INSERT INTO roles (id, name, description) VALUES
                        (1, 'ADMIN', 'Administrator with full access to manage the platform'),
                        (2, 'CUSTOMER', 'Registered user who can browse and purchase products'),
                        (3, 'SELLER', 'Vendor with access to manage product listings and orders');
ALTER TABLE roles AUTO_INCREMENT = 4;