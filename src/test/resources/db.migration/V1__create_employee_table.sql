CREATE TABLE employee_service (
    id SERIAL PRIMARY KEY,
    employee_id VARCHAR(255) NOT NULL,
   employee_name VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL,
    position VARCHAR(255),
    phone_number VARCHAR(255),
    salary NUMERIC,
    hire_date DATE,
    status VARCHAR(255),
    CONSTRAINT uniqueEmailConstraint UNIQUE (email),
    CONSTRAINT uniqueEmployeeIdConstraints UNIQUE (employee_id)
);