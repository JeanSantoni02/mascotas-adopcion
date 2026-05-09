-- Crear base de datos
CREATE DATABASE IF NOT EXISTS mascotasdb;
USE mascotasdb;

-- Tabla de usuarios
CREATE TABLE IF NOT EXISTS users (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    email VARCHAR(100),
    role VARCHAR(20) NOT NULL DEFAULT 'USER',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Tabla de mascotas
CREATE TABLE IF NOT EXISTS pets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    species VARCHAR(50) NOT NULL,
    breed VARCHAR(100),
    age INT,
    gender VARCHAR(10),
    location VARCHAR(200),
    status VARCHAR(20) DEFAULT 'available',
    description TEXT,
    photo_url VARCHAR(500),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insertar usuarios (contraseñas encriptadas con BCrypt)
-- admin123, user123
INSERT INTO users (username, password, email, role) VALUES 
('admin', '$2a$10$N.ZqGj9aFzYqkXy5QzqQ.eZzQqXy5QzqQ.eZzQqXy5QzqQ.eZzQqXy', 'admin@mascotas.com', 'ADMIN'),
('usuario1', '$2a$10$N.ZqGj9aFzYqkXy5QzqQ.eZzQqXy5QzqQ.eZzQqXy5QzqQ.eZzQqXy', 'user1@mascotas.com', 'USER'),
('usuario2', '$2a$10$N.ZqGj9aFzYqkXy5QzqQ.eZzQqXy5QzqQ.eZzQqXy5QzqQ.eZzQqXy', 'user2@mascotas.com', 'USER');

-- Insertar mascotas de prueba
INSERT INTO pets (name, species, breed, age, gender, location, status, description) VALUES
('Max', 'Perro', 'Labrador', 3, 'Macho', 'Santiago', 'available', 'Perro muy amigable y juguetón'),
('Luna', 'Gato', 'Siames', 2, 'Hembra', 'Valparaíso', 'available', 'Gata cariñosa y tranquila'),
('Rocky', 'Perro', 'Pastor Alemán', 4, 'Macho', 'Concepción', 'available', 'Perro inteligente y protector'),
('Mia', 'Gato', 'Persa', 1, 'Hembra', 'Santiago', 'available', 'Gatita juguetona y adorable'),
('Bobby', 'Perro', 'Beagle', 2, 'Macho', 'La Serena', 'available', 'Muy activo y curioso');
