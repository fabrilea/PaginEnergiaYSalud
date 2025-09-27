-- Tabla de usuarios
CREATE TABLE usuario (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    dni VARCHAR(20) NOT NULL UNIQUE,
    nombre VARCHAR(100) NOT NULL
);

-- Tabla de rutinas
CREATE TABLE rutina (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    notas VARCHAR(255)
);

-- Tabla de ejercicios
CREATE TABLE ejercicio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255)
);

-- Relación Rutina ↔ Ejercicio
CREATE TABLE rutina_ejercicio (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    rutina_id BIGINT NOT NULL,
    ejercicio_id BIGINT NOT NULL,
    CONSTRAINT fk_rutina_ejercicio_rutina FOREIGN KEY (rutina_id) REFERENCES rutina(id),
    CONSTRAINT fk_rutina_ejercicio_ejercicio FOREIGN KEY (ejercicio_id) REFERENCES ejercicio(id)
);

-- Relación Usuario ↔ Rutina
CREATE TABLE usuario_rutina (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    rutina_id BIGINT NOT NULL,
    CONSTRAINT fk_usuario_rutina_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    CONSTRAINT fk_usuario_rutina_rutina FOREIGN KEY (rutina_id) REFERENCES rutina(id)
);

-- Historial de entrenamientos
CREATE TABLE historial (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_id BIGINT NOT NULL,
    rutina_id BIGINT NOT NULL,
    ejercicio_id BIGINT NOT NULL,
    peso_usado DOUBLE,
    repeticiones_realizadas INT,
    fecha DATE,
    CONSTRAINT fk_historial_usuario FOREIGN KEY (usuario_id) REFERENCES usuario(id),
    CONSTRAINT fk_historial_rutina FOREIGN KEY (rutina_id) REFERENCES rutina(id),
    CONSTRAINT fk_historial_ejercicio FOREIGN KEY (ejercicio_id) REFERENCES ejercicio(id)
);
