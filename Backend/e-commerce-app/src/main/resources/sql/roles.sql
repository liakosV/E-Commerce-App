INSERT INTO roles (name, description) VALUES
                        ('ADMIN', 'Administrator with full access to manage the platform'),
                        ('CUSTOMER', 'Registered user who can browse and purchase products'),
                        ('SELLER', 'Vendor with access to manage product listings and orders'),
                        ('GUEST', 'Unauthenticated user with read-only access');
ALTER TABLE roles AUTO_INCREMENT = 5;