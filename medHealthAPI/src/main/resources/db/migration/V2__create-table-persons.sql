CREATE TABLE persons (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    phone VARCHAR(18) NOT NULL,
    cpf VARCHAR(14) UNIQUE NOT NULL,
    address_id INT NOT NULL,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (address_id) REFERENCES addresses(id)
);

INSERT INTO addresses (state, city, neighborhood, street, number, zip_code)
VALUES ('BA', 'Salvador', 'Centro', 'Rua Admin', '1', '40000-000');

INSERT INTO persons (full_name, email, phone, cpf, address_id, status)
VALUES ('Administrador', 'admin@medhealth.com', '(71) 99999-9999', '000.000.000-00', 1, 'ACTIVE');