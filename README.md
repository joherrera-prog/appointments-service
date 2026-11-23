# Appointment Scheduler

Appointment scheduling service for independent professionals.

The first real use case is a tattoo artist's calendar. Clients will view
available one-hour slots and book appointments with automatic confirmation.

This project is developed individually as the capstone for Module 3
Software Design OOD of the Grid Dynamics Java internship.

## Current Status

The project initialization includes:

- Java 21 configuration.
- Maven Wrapper using Maven 3.10.0.
- An application entry point.
- JUnit Jupiter and Maven Surefire configuration.
- Checkstyle, PMD and SpotBugs checks integrated into the verify phase.

Domain functionality and automated domain tests will be added in subsequent
pull requests.

## Requirements

- JDK 21.
- Internet access for the initial download of Maven and dependencies.

Set JAVA_HOME to the JDK 21 installation before running Maven Wrapper.

## Build and Verification

Compile the application:

```bash
./mvnw clean compile
```

Run tests:

```bash
./mvnw test
```

Run the complete verification lifecycle:

```bash
./mvnw clean verify
```

The verify command compiles the project, runs available tests, packages
the application and executes Checkstyle, PMD and SpotBugs.

On Windows, use mvnw.cmd instead of ./mvnw.

## Run the Application

After compilation:

```bash
java -cp target/classes com.appointments.App
```

The initialization entry point prints:

```text
Appointment Scheduler initialized.
```

## Planned User Stories

| ID | User story |
| --- | --- |
| US01 | As a client, I want to view available slots so that I can choose a suitable time. |
| US02 | As a client, I want to book an available slot so that I can secure my appointment. |
| US03 | As a client, I want to view my appointments so that I can check my bookings. |
| US04 | As a client, I want to reschedule my appointment so that I can attend at another time. |
| US05 | As a client, I want to cancel my appointment so that I can release the slot. |
| US06 | As a professional, I want to publish available slots so that clients can book appointments. |
| US07 | As a professional, I want to withdraw available slots so that I can adjust my availability. |
| US08 | As a professional, I want to view my calendar so that I can organize my work. |
| US09 | As a professional, I want to cancel a calendar appointment so that I can handle an unexpected event. |
| US10 | As a professional, I want to delete a cancelled record so that I can clean up my calendar. |

Acceptance criteria are recorded in the corresponding GitHub issues.

## Initial Business Rules

- Each slot starts on the hour and lasts one hour.
- Bookings require a published, available, future slot.
- Each professional can have at most one confirmed appointment per slot.
- Successful bookings are confirmed automatically.
- Clients manage their own appointments.
- Professionals manage their own availability and calendars.
- Cancellation retains the appointment record and releases the slot.
- Professionals may permanently delete cancelled appointments.
- Rescheduling preserves the original booking if the operation fails.
- The initial professional time zone is America/Mexico_City.

## Planned Architecture

The implementation will use plain Java and a layered design:

- Domain classes represent clients, professionals, slots and appointments.
- Services enforce business rules and ownership restrictions.
- Repository interfaces define storage and lookup operations.
- In-memory implementations store data using Java Collections.
- App demonstrates the application flow through main.

Appointments will support CRUD, listing and filtering. Two HashMap indexes
will provide expected average O(1) lookup by appointment ID and booking code.

## Delivery Plan

1. Project initialization.
2. Domain implementation.
3. Automated domain tests.

Each stage will be submitted through a separate pull request for mentor review.