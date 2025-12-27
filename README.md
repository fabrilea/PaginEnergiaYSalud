# 📘 PaginEnergiaYSalud

Aplicación web desarrollada como proyecto académico, orientada a la gestión y visualización de información relacionada con energía y salud.
El proyecto está construido con **Java** y tecnologías web, siguiendo una estructura clara y organizada, con posibilidad de despliegue mediante **Docker**.



## 🚀 Tecnologías utilizadas

* **Java**
* **Maven**
* **HTML / CSS / JavaScript**
* **Docker**
* **Git / GitHub**
* **Vercel** (para despliegue)



## 📂 Estructura del proyecto

```
PaginEnergiaYSalud/
│
├── src/
│   ├── main/
│   │   ├── java/        # Lógica de la aplicación
│   │   ├── resources/   # Archivos de configuración
│   │   └── webapp/      # Frontend (HTML, CSS, JS)
│
├── Dockerfile
├── pom.xml
└── README.md
```



## ⚙️ Funcionalidades principales

* Visualización de información relacionada con energía y salud
* Navegación entre distintas secciones del sitio
* Separación clara entre frontend y backend
* Preparado para ejecución local o mediante contenedores
* Estructura escalable para futuras funcionalidades



## ▶️ Cómo ejecutar el proyecto

### Opción 1: Ejecución local (Java / Maven)

1. Clonar el repositorio:

   ```bash
   git clone https://github.com/fabrilea/PaginEnergiaYSalud.git
   cd PaginEnergiaYSalud
   ```

2. Compilar el proyecto:

   ```bash
   mvn clean package
   ```

3. Ejecutar la aplicación:

   ```bash
   mvn spring-boot:run
   ```

4. Acceder desde el navegador:

   ```
   http://localhost:8080
   ```



### Opción 2: Ejecutar con Docker

1. Construir la imagen:

   ```bash
   docker build -t pagin-energia-salud .
   ```

2. Ejecutar el contenedor:

   ```bash
   docker run -p 8080:8080 pagin-energia-salud
   ```

3. Acceder desde el navegador:

   ```
   http://localhost:8080
   ```



## 🧠 Objetivo del proyecto

Este proyecto fue desarrollado con fines educativos y prácticos, con el objetivo de:

* Aplicar conceptos de desarrollo backend en Java
* Practicar estructuración de proyectos reales
* Integrar frontend y backend en una misma aplicación
* Trabajar con despliegue y contenedores



## 📌 Estado del proyecto

🟢 Funcional y operativo
🛠️ Abierto a mejoras y ampliaciones futuras



## 👨‍💻 Autor

**Fabrizio Leali**
Desarrollador Backend / Full Stack Jr

📎 GitHub: [https://github.com/fabrilea](https://github.com/fabrilea)

