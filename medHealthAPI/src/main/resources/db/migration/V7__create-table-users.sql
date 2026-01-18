CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    password VARCHAR(72) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

INSERT INTO users (username, email, password)
    VALUES (
        'admin',
        'admin@admin.com',
        '$2a$10$3jv.H5kMwLvMAY0XDHJP2.9PdpmXejSOLL5ihOShv1Tg9/dagT.HC'
    );

