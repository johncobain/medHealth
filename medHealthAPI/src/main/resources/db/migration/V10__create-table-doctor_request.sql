CREATE TABLE doctor_request (
    id SERIAL PRIMARY KEY,
    full_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    phone VARCHAR(18) NOT NULL,
    cpf VARCHAR(14) NOT NULL,

    crm VARCHAR(14) NOT NULL,
    specialty VARCHAR(50) NOT NULL,

    address_state VARCHAR(255) NOT NULL,
    address_city VARCHAR(255) NOT NULL,
    address_neighborhood VARCHAR(255) NOT NULL,
    address_street VARCHAR(255) NOT NULL,
    address_number VARCHAR(255),
    address_complement VARCHAR(255),
    address_zip_code VARCHAR(255) NOT NULL,

    status VARCHAR(20) DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);