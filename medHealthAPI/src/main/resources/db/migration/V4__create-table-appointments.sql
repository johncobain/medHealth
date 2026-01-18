CREATE TABLE appointments (
    id SERIAL PRIMARY KEY,
    date TIMESTAMP NOT NULL,
    doctor_id INT NOT NULL,
    patient_id INT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'SCHEDULED',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    FOREIGN KEY (doctor_id) REFERENCES doctors(id),
    FOREIGN KEY (patient_id) REFERENCES patients(id)
)