-- Create Database
CREATE DATABASE IF NOT EXISTS user_service_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE user_service_db;

-- Users table (Students + Librarians with profile photos)
CREATE TABLE users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(255) UNIQUE NOT NULL,
    password VARCHAR(500) NOT NULL COMMENT 'BCrypt hashed password',
    name VARCHAR(200) NOT NULL,
    role ENUM('STUDENT', 'LIBRARIAN') NOT NULL,
    phone VARCHAR(20),
    address TEXT,
    student_id VARCHAR(50) COMMENT 'NULL for librarians',
    
    -- Profile Photo fields
    profile_photo LONGTEXT COMMENT 'Base64 encoded image data',
    photo_filename VARCHAR(255) COMMENT 'Original filename',
    
    active TINYINT(1) DEFAULT 1,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

-- Performance Indexes
CREATE INDEX idx_users_email ON users(email);
CREATE INDEX idx_users_role ON users(role);
CREATE INDEX idx_users_student_id ON users(student_id);
CREATE INDEX idx_users_active ON users(active);

-- Sample Data
INSERT INTO users (email, password, name, role, phone, address, student_id, photo_filename) VALUES
('admin@library.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Admin Librarian', 'LIBRARIAN', '9876543210', 'Library Admin Office', NULL, 'admin.jpg'),
('student001@college.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'John Doe', 'STUDENT', '1234567890', 'College Hostel Room 101', 'STU001', 'john.jpg'),
('student002@college.com', '$2a$10$92IXUNpkjO0rOQ5byMi.Ye4oKoEa3Ro9llC/.og/at2.uheWG/igi', 'Jane Smith', 'STUDENT', '0987654321', 'Girls Hostel Block A', 'STU002', 'jane.jpg');
