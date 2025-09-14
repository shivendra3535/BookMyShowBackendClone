🎬 BookMyShow Clone – Movie Ticket Booking System
📌 Overview

This project is a BookMyShow-inspired movie ticket booking system, built with Spring Boot (backend) and React.js (frontend). It allows users to browse movies, view show timings, check seat availability, and make bookings.

The goal was to simulate a real-world full-stack application with proper REST APIs, database relations, and a clean frontend integration.

🚀 Features

User registration & login

Browse movies by genre/language

View shows & timings

Seat selection with availability status

Ticket booking with payment options

Backend validations (seat availability, duplicate booking prevention)

REST APIs with proper request/response DTOs

MySQL integration with foreign key relationships

🛠️ Tech Stack

Backend (Spring Boot)

Java

Spring Boot

Spring Data JPA (Hibernate)

MySQL

REST API

Frontend (React.js)

React Router

Axios (API calls)

Tailwind / CSS for styling

📂 API Endpoints (Backend @ http://localhost:8081)
Users

GET /api/users → Fetch all users

POST /api/users → Register user

Movies

GET /api/movies → Fetch all movies

GET /api/movies/{id} → Get movie by ID

Shows

GET /api/shows → Get all shows

GET /api/shows/{id} → Show details

Bookings

POST /api/bookings → Create a booking

GET /api/bookings/{id} → Get booking details

📖 What I Learned

During this project, I gained hands-on knowledge in:

Designing REST APIs in Spring Boot

Implementing DTOs & validation for clean API communication

Handling foreign key relationships in SQL (Users ↔ Bookings ↔ Shows ↔ Seats)

React basics: routing, forms, API integration, error handling

Importance of backend validation (e.g., seat availability checks before booking)

How frontend and backend communicate using JSON

🐞 Errors I Faced & Fixes

Ambiguous handler methods in Spring Boot

Error: "Ambiguous handler methods mapped..."

Fix: Used @GetMapping("/id/{id}"), @GetMapping("/genre/{genre}") instead of overlapping mappings.

Seat availability error during booking

Error: "Seat D2 is not available" even for free seats.

Fix: Corrected database status values (AVAILABLE, BOOKED) and updated booking logic.

React component import issue

Error: "Element type is invalid..."

Fix: Ensured proper default vs named exports/imports.

Foreign key constraint errors in MySQL

Cause: Inserting data without respecting foreign key relations.

Fix: Inserted movies → shows → seats → bookings in proper order.

CORS issue between React & Spring Boot

Error: Blocked requests due to CORS policy.

Fix: Used @CrossOrigin(origins = "http://localhost:3000") in controllers.

🧑‍💻 Hands-On Exposure

This project gave me practical exposure to:

Full-stack development (React + Spring Boot)

Database schema design & entity relationships

Implementing seat booking logic in a real-world scenario

Handling API errors gracefully in frontend

Debugging & fixing runtime exceptions in both frontend & backend

Structuring a project like a production-grade system

✅ Future Improvements

Add authentication (JWT-based login/signup)

Implement payment gateway integration

Admin dashboard for managing movies & shows

Deploy project on AWS / Docker

🔹 This project not only helped me improve technical skills but also gave me confidence in end-to-end application development, debugging, and deploying a production-ready system.

📌 API Endpoints – BookMyShow Clone

All APIs are served from:

Base URL: http://localhost:8081/api

👤 User APIs
1. Get all users

GET /api/users

2. Get user by ID

GET /api/users/{id}

3. Create new user (register)

POST /api/users
Sample JSON Request:

{
  "name": "Ravi Kumar",
  "email": "ravi@example.com",
  "password": "secure123"
}

4. Update user

PUT /api/users/{id}

5. Delete user

DELETE /api/users/{id}

🎬 Movie APIs
1. Get all movies

GET /api/movies

2. Get movie by ID

GET /api/movies/id/{id}

3. Get movie by name

GET /api/movies/name/{title}

4. Get movies by genre

GET /api/movies/genre/{genre}

5. Get movies by language

GET /api/movies/language/{language}

6. Create new movie

POST /api/movies
Sample JSON Request:

{
  "title": "RRR",
  "description": "Two legendary revolutionaries fight against the British Raj",
  "language": "Telugu",
  "genre": "Action",
  "durationMins": 180,
  "releaseDate": "2022-03-25",
  "posterUrl": "rrr_poster.jpg"
}

7. Update movie

PUT /api/movies/{id}

8. Delete movie

DELETE /api/movies/{id}

🎭 Show APIs
1. Get all shows

GET /api/shows

2. Get show by ID

GET /api/shows/{id}

3. Get shows for a movie

GET /api/shows/movie/{movieId}

4. Create new show

POST /api/shows
Sample JSON Request:

{
  "movieId": 1,
  "startTime": "2025-09-14T18:30:00",
  "endTime": "2025-09-14T21:30:00",
  "theatreName": "PVR Hyderabad"
}

5. Update show

PUT /api/shows/{id}

6. Delete show

DELETE /api/shows/{id}

🎟️ Seat APIs
1. Get seats for a show

GET /api/seats/show/{showId}

2. Update seat status

PUT /api/seats/{id}

📝 Booking APIs
1. Get all bookings

GET /api/bookings

2. Get booking by ID

GET /api/bookings/{id}

3. Create a booking

POST /api/bookings
Sample JSON Request:

{
  "userId": 1,
  "showId": 2,
  "seatIds": [1, 2, 3],
  "paymentMethod": "UPI"
}


Sample Success Response:

{
  "bookingId": 101,
  "userId": 1,
  "showId": 2,
  "seatsBooked": ["A1", "A2", "A3"],
  "totalAmount": 600.0,
  "status": "CONFIRMED"
}

4. Cancel booking

DELETE /api/bookings/{id}
