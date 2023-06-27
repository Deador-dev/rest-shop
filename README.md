# Smartphone Store API
Welcome to the repository of the Smartphone Store API! This project aims to develop and implement an API using the Java Spring framework for managing smartphone sales in an online store.

## Features
- Product Management: Create, update, and delete categories, smartphones and orders.
- Order Processing: Handle customer orders, including placing and modifying orders. Users in personal cabinets can view and track their orders, including order status and delivery details.
- Search and Filtering: Users can search for specific smartphones or use filters to narrow down their options based on various criteria.
- User Authentication: Secure user registration and login functionality to ensure access control. Upon registration, a confirmation email is sent to the user's email address for account verification.
- Shopping Cart: Enable customers to add and remove items from their shopping cart before making a purchase.

## Technologies Used
- Java Spring: A powerful Java-based framework for building robust and scalable web applications.
- MySQL: A popular relational database management system for storing and retrieving data.
- JPA Repositories: The project utilizes JPA (Java Persistence API) Repositories for database access and management.
- Lombok: A Java library that helps reduce boilerplate code by providing annotations to generate common code constructs such as getters, setters, constructors, and more.
- Spring Mail Sender: The project incorporates Spring Mail Sender to handle email functionality, including sending confirmation emails to users during the account registration process.
- JWT: JSON Web Tokens for secure authentication and authorization.

## Getting Started
To get started with the Smartphone Store API, follow these steps:

1. Clone this repository to your local machine.
2. Set up the MySQL database and update the database configuration in application.properties file.
3. Configure your environment variables.
4. Build and run the API server.
5. Access the API endpoints through `http://localhost:8080/`.

Note: Make sure to configure the email settings in the application to enable email sending functionality.

## Development and Testing:
I am currently working on implementing support for running tests in Docker Testcontainers. This will ensure that tests run consistently in isolated Docker environments, improving the reliability and reproducibility of testing process.
