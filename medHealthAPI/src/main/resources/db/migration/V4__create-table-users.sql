CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    person_id INT NOT NULL UNIQUE,
    password VARCHAR(72) NOT NULL,
    password_reset_token VARCHAR(255),
    password_reset_expires TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (person_id) REFERENCES persons(id) ON DELETE CASCADE
);

INSERT INTO users (person_id, password)
VALUES (1, '$2a$10$3jv.H5kMwLvMAY0XDHJP2.9PdpmXejSOLL5ihOShv1Tg9/dagT.HC');

