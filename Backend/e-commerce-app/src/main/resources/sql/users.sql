INSERT INTO users (id, created_at, updated_at, uuid, email, firstname, is_active, lastname, password, username, role_id, user_more_info_id) VALUES
                (1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID(), 'john@admin.com', 'John', 1, 'Doe', '$2a$11$A7Ng8DKYGZY5ODQoijdStO.6fqXUEeYAMMQ.gRQGRqWuZiHyAY7eq', 'JohnD', 1, NULL),
                (2, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID(), 'alice@seller.com', 'Alice', 1, 'Wonderland', '$2a$11$A7Ng8DKYGZY5ODQoijdStO.6fqXUEeYAMMQ.gRQGRqWuZiHyAY7eq', 'AliceW', 3, NULL),
                (3, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, UUID(), 'bob@customer.com', 'Bob', 1, 'Builder', '$2a$11$A7Ng8DKYGZY5ODQoijdStO.6fqXUEeYAMMQ.gRQGRqWuZiHyAY7eq', 'BobB', 2, NULL);
ALTER TABLE users AUTO_INCREMENT = 2;