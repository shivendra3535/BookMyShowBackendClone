🎬 BookMyShow Clone – Movie Ticket Booking System

This project is a BookMyShow-inspired movie ticket booking system built with Spring Boot (backend) and React.js (frontend). It provides users with a complete end-to-end flow for browsing movies, checking available shows, selecting seats, and booking tickets. The aim of the project was to gain real-world hands-on experience with full-stack application development, involving proper REST APIs, database relationships, and a frontend integration with error handling.

The system includes multiple features such as user registration and login, movie browsing by genre and language, viewing show timings, checking seat availability in real-time, booking tickets with payment options, and maintaining proper validations on both the backend and frontend. The backend is powered by Spring Boot with Spring Data JPA (Hibernate) for ORM and MySQL as the relational database. The frontend is built using React.js with React Router for navigation and Axios for API calls.

The API design is clean and follows a RESTful structure. For users, there are endpoints to create, update, delete, and fetch users. Movies can be searched by ID, title, genre, or language. Shows can be created, updated, deleted, and fetched by either ID or movie ID. Each show has its own associated seats, which can be fetched and updated individually. The booking module is the most crucial part, where a user can create a new booking by selecting available seats for a specific show. The booking request validates seat availability and calculates the total ticket price before confirming the booking. Bookings can also be canceled through the API.

For example, creating a booking requires a request like:

{
  "userId": 1,
  "showId": 2,
  "seatIds": [1, 2, 3],
  "paymentMethod": "UPI"
}


If successful, the response contains booking details such as the booked seats, total amount, and confirmation status.

Through this project, I learned how to design and implement REST APIs in Spring Boot, how to structure entities with proper foreign key relationships in MySQL, and how to use DTOs for clean data transfer between the backend and frontend. I also gained a solid understanding of handling backend validations, integrating the APIs with a React frontend, and managing CORS issues by using the @CrossOrigin annotation. On the frontend, I practiced working with React Router for page navigation, handling forms and validations, integrating with APIs using Axios, and displaying server responses and error messages gracefully.

During the development process, I faced multiple errors that significantly enhanced my debugging skills. At one point, I encountered ambiguous handler mapping errors in Spring Boot when trying to fetch movies by name, genre, and language. I solved this by restructuring my endpoints, such as using /api/movies/id/{id} and /api/movies/genre/{genre}, to avoid clashes. Another frequent issue was related to seat availability, where even available seats were marked unavailable due to incorrect status handling in the database. Fixing this required correcting the status field values (AVAILABLE, BOOKED) and updating the booking service logic. In React, I faced an “Element type is invalid” error due to confusion between default and named imports, which I fixed by aligning export and import correctly. I also faced foreign key constraint failures in MySQL when inserting data in the wrong order; the solution was to insert data following the hierarchy: movies → shows → seats → bookings. Additionally, I solved CORS errors between React and Spring Boot by allowing requests from http://localhost:3000 in my controllers.

This project gave me practical, hands-on exposure to full-stack development, starting from designing the database schema and entity relationships, to implementing API endpoints, to integrating the backend with a React frontend. I now feel more confident in developing production-grade applications, debugging runtime issues, and maintaining clean communication between client and server through JSON APIs.

Looking ahead, I plan to enhance this project further by adding JWT-based authentication for secure login and signup, integrating a real payment gateway, and building an admin dashboard to manage movies and shows. I also aim to deploy the project on AWS or Docker for real-world deployment experience.

Overall, this project was a complete learning package that strengthened my skills in Java, Spring Boot, MySQL, React.js, and API integration while also teaching me how to handle real-world issues such as ambiguous endpoints, CORS errors, database integrity, and frontend-backend synchronization.

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
