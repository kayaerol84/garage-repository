# Garage Reservation System

This project implements a basic garage reservation system. It supports scheduling appointments for various automotive services, managing mechanics' schedules, and querying available slots.

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

What you need to install the software:

- Java JDK 11 or later
- Gradle 6.3 or later (if not using the Gradle Wrapper)
- Your preferred IDE (Eclipse, IntelliJ IDEA, etc.)

### Installing

A step by step series of examples that tell you how to get a development environment running:

1. Clone the repository to your local machine:

   ```bash
   git clone https://github.com/kayaerol84/garage-reservation-system.git

2. Navigate into the project directory:

```bash
cd garage-reservation-system
```

3. If using the Gradle Wrapper, build the project:
```bash
./gradlew build
```
Or on Windows:
```bash 
gradlew.bat build
```

4. Run the application:
```bash
./gradlew bootRun
```
Or on Windows:
```bash
gradlew.bat bootRun
```
The server will start running on http://localhost:8080. 

The application will automatically create 2 Mechanics and Operating hours details of the Garage. You will be able to update by using the below-mentioned Admin endpoints.

## TODO
These could be added within the next stage of this project. 

* Handling concurrency when multiple requests trying to book the same time slot
* Customer information
* Background job to move previous appointments to an archival DB
* [Spring REST Docs](https://docs.spring.io/spring-restdocs/docs/current/reference/htmlsingle/)
* ArchUnit tests
* Logs

## Usage

### Access DB
Login to reservationdb with the details configured in application.properties
```bash
http://localhost:8080/h2-console
```
* **JDBC URL** : jdbc:h2:mem:reservationdb
* **User name**: sa
* **Password** : 

### API Endpoints
The application provides the following RESTful endpoints:

* **GET /api/reservations/slots**: Retrieve available slots for appointments. 
  * Parameters:
    * jobType (required): The type of job (GENERAL_CHECK, TIRE_REPLACEMENT, BROKEN_LAMP_CHANGE).
    * mechanicId (required): The ID of the mechanic.
    * date (required): The date to check for availability (format: yyyy-mm-dd).

  Example request:
  ```bash
  curl -X GET "http://localhost:8080/api/reservations/slots?jobType=TIRE_REPLACEMENT&mechanicId=1&date=2024-04-23"
  ```

* **POST /api/reservations/appointments**: Create a new appointment.

  * Body:
    ```json
    {
      "mechanicId": 1,
      "jobType": "GENERAL_CHECK",
      "startTime": "2024-04-23T10:00:00"
    }
    ```
  Example request:
  ```bash
  curl -X POST -H "Content-Type: application/json" -d 
  ' { "mechanicId": 1, "jobType": "GENERAL_CHECK", "startTime": "2024-04-23T10:00:00"}' 
  "http://localhost:8080/api/reservations/appointments"
  ```

### ADMIN Endpoints
* **POST** **/admin/garage/mechanics**: Creates a new mechanic
  * Body: 
  ```json
  {
    "name": "Third Mechanic",
    "offDays": [
        "TUESDAY",
        "WEDNESDAY"
    ]
  }
  ```
  
  Example request:
  
  ```bash
  curl --location 'http://localhost:8080/admin/garage/mechanics' \
  --header 'Content-Type: application/json' \
  --data '{
      "name": "Third Mechanic",
      "offDays": [
          "TUESDAY",
          "WEDNESDAY"
      ]
  }'
  ```
* **GET /admin/garage/operatingHours**: To check operating hours configuration

  Example Request: 
  ```bash
  curl --location 'http://localhost:8080/admin/garage/operatingHours'
  ```

* **POST /admin/garage/operatingHours**: To create a new operating hour definition for a specific date
  * Body:
  * ```json
    {
      "openTime": "00:00:00",
      "closeTime": "00:00:00",
      "specificDate": "2024-05-06"    
    }
    ```

  Example request:
  ```bash
  curl --location 'http://localhost:8080/admin/garage/operatingHours' \
  --header 'Content-Type: application/json' \
  --data '{
      "openTime": "00:00:00",
      "closeTime": "00:00:00",
      "specificDate": "2024-05-06"
  }'
  ```

* **PUT /admin/garage/operatingHours/{id}**: To update an existing operating hour definition for a Day
  * Body:
  * ```json
    {
      "openTime": "09:00:00",
      "closeTime": "18:00:00",
      "dayOfWeek": "MONDAY"
    }
    ```

  Example request:
  ```bash
  curl --location --request PUT 'http://localhost:8080/admin/garage/operatingHours/1' \
  --header 'Content-Type: application/json' \
  --data '{
      "openTime": "09:00:00",
      "closeTime": "18:00:00",
      "dayOfWeek": "MONDAY"
  }'
  ```

### Running Tests

To run the automated tests for this system, use:

```bash
./gradlew test
```
Or on Windows:

```bash
gradlew.bat test
```
**Built With**

* Spring Boot - The framework used
* Gradle - Dependency Management
