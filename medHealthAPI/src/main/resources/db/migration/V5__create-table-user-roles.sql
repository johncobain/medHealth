CREATE TABLE user_roles (
    user_id INT NOT NULL,
    roles_id INT NOT NULL,
    PRIMARY KEY (user_id, roles_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (roles_id) REFERENCES roles(id) ON DELETE CASCADE
);

INSERT INTO user_roles (user_id, roles_id) VALUES (1, 1);
