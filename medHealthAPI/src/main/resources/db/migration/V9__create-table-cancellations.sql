CREATE TABLE cancellations (
    id SERIAL PRIMARY KEY,
    appointment_id INT NOT NULL,
    reason VARCHAR(256) NOT NULL,
    message VARCHAR(256),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (appointment_id) REFERENCES appointments(id) ON DELETE CASCADE
);