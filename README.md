# E-Commerce App

This is a full-stack E-Commerce web application built with:

-  **Spring Boot (Java)** for the backend (REST API)
-  **Angular** for the frontend (customer interface)
-  **MySQL** as the database

## Getting Started

You can run the application in **two ways**:

---

## Option 1: Run with Docker (Recommended)

#### Prerequisites:
- [Docker Desktop](https://www.docker.com/products/docker-desktop) installed
- Make sure your local MySQL service is **stopped** to avoid port conflicts:

<details>
<summary>How to stop MySQL on Windows</summary>

1. Press `Win + R` to open the Run dialog
2. Type `services.msc` and press Enter
3. Find **MySQL80** in the list
4. If it's running, right-click it and select **Stop**

</details>

---

### Run the App with Docker

In the root of the project (`E-Commerce-App/`), run from **Git Bash**:

```
cd backend/e-commerce-app
```
```
./gradlew build
```

```
cd ../..
```
```
docker compose up --build
```
---

### After the build
- Visit the frontend: http://localhost:4200
- Access the backend Swagger API docs: http://localhost:8080/swagger-ui/index.html

---

## Option 2: Run Locally Without Docker

#### Prerequisites:

- **Java 17+** installed
- **Node.js (v18+)** and **Angular CLI** installed
- **MySQL** service running locally
- A database named `ecommerce_app` created in your MySQL instance
- Proper MySQL credentials configured

#### 1. Set Up the Database
Start MySQL and create the database

Update the properties file:

```properties
# backend/e-commerce-app/src/main/resources/application-test.properties
spring.datasource.url=jdbc:mysql://localhost:3306/ecommerce_app
spring.datasource.username=user7
spring.datasource.password=12345
```

#### 2. Run the Backend

Go to: `# backend/e-commerce-app/src/main/resources/application-test.properties`, and follow the instructions

```properties
## COMMENT AT CREATE, UNCOMMENT AT FIRST UPDATE AND COMMENT AGAIN AFTER THE FIRST UPDATE
#spring.sql.init.mode=always
#spring.sql.init.data-locations=classpath:sql/regions.sql, classpath:sql/roles.sql, 
# classpath:sql/categories.sql, classpath:sql/products.sql, classpath:sql/users.sql
```
Then do:

```
cd backend/e-commerce-app
./gradlew bootRun
```

The backend runs at: [http://localhost:8080](http://localhost:8080)

#### 3. Run the Frontend

```
cd frontend
npm install
ng serve
```

The frontend runs at: [http://localhost:4200](http://localhost:4200)

#### 4. Optional: API Docs via Swagger

Visit: [http://localhost:8080/swagger-ui/index.html](http://localhost:8080/swagger-ui/index.html)
