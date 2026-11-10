
# Online Examination System

## Project Overview

The Online Examination System is a backend application developed using Java and Spring Boot. It allows students to register, log in, view exams, attempt examinations, submit answers, and view their results.

An administrator can manage students, questions, exams, and examination records.

The application follows a monolithic architecture and uses MySQL for data storage.

## Technologies Used

- Java 17
- Spring Boot
- Spring Security
- Spring Data JPA
- Hibernate
- MySQL
- Maven
- REST APIs
- BCrypt Password Encryption
- Postman (API Testing)

## Features

### Student Module
- Student registration
- Student login
- View own profile
- Update own profile
- View available exams
- Start an examination
- Save answers
- Submit examination
- View examination results

### Admin Module
- Admin authentication
- View all students
- Create, update and delete questions
- Create, update and delete exams
- View all examination attempts
- Manage examination records

### Examination Module
- Start an exam
- Save student answers
- Track examination duration
- Automatically calculate scores on submission
- Generate examination results
- Prevent unauthorized access to another student's attempts

## Project Architecture

The application follows a layered architecture:

- Controller Layer: Handles REST API requests.
- Service Layer: Contains business logic.
- Repository Layer: Communicates with the database.
- Entity Layer: Represents database tables.
- DTO Layer: Transfers data between API and application.

## Database Configuration

1. Install MySQL Server.
2. Create a database:

```sql
CREATE DATABASE online_exam_db;
```

3. Configure your database credentials in:

`src/main/resources/application.properties`

Example:

```properties
spring.datasource.url=https://github.com/rimab05/Online-Examination-System
spring.datasource.username=root
spring.datasource.password=YOUR_PASSWORD

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true

server.port=8080
```

Replace `YOUR_PASSWORD` with your local MySQL password.

Do not commit actual database passwords to GitHub.

## How to Run the Project

1. Clone the repository:

```bash
git clone YOUR_GITHUB_REPOSITORY_URL
```

2. Open the project in IntelliJ IDEA.

3. Configure MySQL credentials in `application.properties`.

4. Ensure Java 17 and Maven are installed.

5. Run the main Spring Boot application:

`OnlineexaminationApplication.java`

6. The application will start at:

`http://localhost:8080`

## API Endpoints

### Student APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | /api/students/register | Register student |
| POST | /api/students/login | Student login |
| GET | /api/students | Get all students (Admin) |
| GET | /api/students/{username} | Get student profile |
| PUT | /api/students/{username}/profile | Update profile |

### Question APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | /api/questions | Create question (Admin) |
| GET | /api/questions | Get all questions |
| GET | /api/questions/{id} | Get question by ID |
| PUT | /api/questions/{id} | Update question (Admin) |
| DELETE | /api/questions/{id} | Delete question (Admin) |

### Exam APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | /api/exams | Create exam (Admin) |
| GET | /api/exams | Get all exams |
| GET | /api/exams/{id} | Get exam by ID |
| PUT | /api/exams/{id} | Update exam (Admin) |
| DELETE | /api/exams/{id} | Delete exam (Admin) |

### Exam Attempt APIs

| Method | Endpoint | Description |
|---|---|---|
| POST | /api/exam-attempts/start/{username}/{examId} | Start exam |
| GET | /api/exam-attempts/{username} | Get student's attempts |
| POST | /api/exam-attempts/{attemptId}/answers | Save answer |
| GET | /api/exam-attempts/{attemptId}/answers | View saved answers |
| POST | /api/exam-attempts/complete/{id} | Complete exam |
| GET | /api/exam-attempts/{attemptId}/result | Get exam result |
| GET | /api/exam-attempts | Get all attempts (Admin) |

## Security

- Spring Security with HTTP Basic authentication.
- BCrypt password encryption.
- Role-based access control using ADMIN and STUDENT roles.
- Students can access and update only their own profiles.
- Admin-only access for administrative operations.
- Correct answers are not exposed through student-facing question and answer responses.

## Testing

REST APIs were tested using Postman.

The following workflows were verified:

- Student registration and authentication.
- Student profile access and updates.
- Unauthorized profile access prevention.
- Admin access to student records.
- Exam creation and retrieval.
- Exam attempt creation.
- Answer submission.
- Exam completion and result calculation.

## Future Enhancements

- JWT-based authentication.
- Email notifications.
- Automatic exam submission after time expiry.
- Pagination and search.
- Detailed examination analytics.

## Author

Rima Bid
