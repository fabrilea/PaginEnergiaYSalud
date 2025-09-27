-- Usuarios
INSERT INTO usuario (id, dni, nombre) VALUES (1, '11111111', 'Juan Pérez');
INSERT INTO usuario (id, dni, nombre) VALUES (2, '22222222', 'María López');

-- Rutinas
INSERT INTO rutina (id, nombre, notas) VALUES (1, 'Rutina Fuerza', 'Enfocada en hipertrofia');
INSERT INTO rutina (id, nombre, notas) VALUES (2, 'Rutina Cardio', 'Resistencia aeróbica');

-- Ejercicios
INSERT INTO ejercicio (id, nombre, descripcion) VALUES (1, 'Press banca', 'Pecho con barra');
INSERT INTO ejercicio (id, nombre, descripcion) VALUES (2, 'Sentadillas', 'Piernas con barra');
INSERT INTO ejercicio (id, nombre, descripcion) VALUES (3, 'Cinta', 'Correr en cinta');

-- Relación Rutina ↔ Ejercicio
INSERT INTO rutina_ejercicio (id, rutina_id, ejercicio_id) VALUES (1, 1, 1);
INSERT INTO rutina_ejercicio (id, rutina_id, ejercicio_id) VALUES (2, 1, 2);
INSERT INTO rutina_ejercicio (id, rutina_id, ejercicio_id) VALUES (3, 2, 3);

-- Relación Usuario ↔ Rutina
INSERT INTO usuario_rutina (id, usuario_id, rutina_id) VALUES (1, 1, 1);
INSERT INTO usuario_rutina (id, usuario_id, rutina_id) VALUES (2, 2, 2);
