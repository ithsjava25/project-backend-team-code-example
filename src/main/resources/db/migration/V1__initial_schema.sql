CREATE TABLE users(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    first_name VARCHAR(50) NOT NULL,
    last_name VARCHAR(50) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    role VARCHAR(20) NOT NULL,
    active BOOLEAN DEFAULT TRUE,
    password_reset_required BOOLEAN DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP
);

CREATE TABLE s3(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    project_id BIGINT NOT NULL,
    file_key VARCHAR(100) NOT NULL,
    content_type VARCHAR(50) NOT NULL
);

CREATE TABLE project(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(150) NOT NULL,
    description VARCHAR(500),
    release_date DATE,
    category VARCHAR(10) NOT NULL,
    genre VARCHAR(50) NOT NULL,
    company_id BIGINT NOT NULL
);

CREATE TABLE project_employees(
    project_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,

    PRIMARY KEY (project_id, employee_id),
    FOREIGN KEY (project_id) REFERENCES project(id),
    FOREIGN KEY (employee_id) REFERENCES users(id)
);

CREATE TABLE task(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    task_type VARCHAR(20) NOT NULL,
    description VARCHAR(255),
    deadline DATETIME,
    status VARCHAR(10) NOT NULL,
    project_id BIGINT NOT NULL,
    user_id BIGINT
);

CREATE TABLE comment(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    content VARCHAR(300) NOT NULL,
    task_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    created_at TIMESTAMP NOT NULL
);

CREATE TABLE company(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    company_name VARCHAR(200) NOT NULL,
    -- Address --
    city VARCHAR(100) NOT NULL,
    street VARCHAR(255) NOT NULL,
    street_number VARCHAR(20) NOT NULL,
    zip_code VARCHAR(20) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE logger(
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    logger_action VARCHAR(255),
    message VARCHAR(255),
    user_id BIGINT NOT NULL,
    project_id BIGINT NOT NULL,
    entity_type VARCHAR(255),
    entity_id BIGINT NOT NULL
);

