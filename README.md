# 🧑‍💼 Job Board API

A RESTful API built with **Spring Boot** for managing jobs, users, and applications. Includes authentication, admin statistics, and file upload support. Easily runnable via Docker.

## 🚀 Features

- 🔐 JWT Authentication (Admin, Employer, Candidate roles)
- 📄 CRUD for Jobs and Applications
- 🧾 Resume & Cover Letter upload (Multipart)
- 📊 Admin dashboard with platform stats
- 🧭 API documentation with Swagger (OpenAPI)
- 🐬 MySQL + JPA/Hibernate integration
- 🐳 Docker & Docker Compose support

## 🧰 Tech Stack

- Java 21, Spring Boot 3.4
- Spring Security, Spring Data JPA
- MySQL, Lombok, ModelMapper
- JWT (jjwt), Springdoc OpenAPI
- Docker, Docker Compose

## 🛠️ Getting Started

### ⚙️ Prerequisites
- Java 21
- Docker & Docker Compose

### 🐳 Run with Docker
```bash
docker compose up --build
