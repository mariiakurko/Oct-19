# Property Management System

## Project Description

This is a Java Property Management System that I created for my DMS project. The program uses a graphical user interface (GUI) built with Java Swing and an SQLite database to store property and reservation information.

The main purpose of this project is to allow users to manage properties, update their information, and create guest reservations.

## Features

- Display all properties in a table.
- Add new properties to the database.
- Update existing property information.
- Remove properties that do not have reservations.
- Create guest reservations.
- Prevent overlapping reservations for the same property.
- Allow same-day check-out and check-in.
- Validate user input and display error messages.
- Save property and reservation information in SQLite.

## Technologies Used

- Java 21
- Java Swing for the GUI
- SQLite for database storage
- SQLite JDBC driver
- JUnit for testing
- Visual Studio Code for development

## How to Run the Project

1. Install Java 21 or a compatible Java version.
2. Download the project ZIP and extract it.
3. Open a Terminal in the extracted project folder, where the `PropertyManagementSystem` and `lib` folders are located.
4. Run this command:

   `java -jar PropertyManagementSystem/PropertyManagementSystem.jar`

5. The Property Management System window should open.
6. Click **Connect Database**.
7. Enter this database path:

   `PropertyManagementSystem/properties.db`

8. Click **OK** and then **Display Properties** to view the property records.

**Important:** Keep the `PropertyManagementSystem` and `lib` folders in their original locations. The JAR uses the SQLite JDBC driver from the `lib` folder.

## Database

The project includes a sample SQLite database named `properties.db`, populated with at least 20 property records.

The database stores property information and guest reservations. Changes made through the application are saved to SQLite.

## Input Validation

The application checks for invalid property IDs, nightly rates, bedroom counts, missing information, and incorrect reservation dates.

It also prevents users from creating overlapping reservations or deleting properties that have reservations.

## Testing

I tested the main functions of the application, including adding, displaying, updating, and removing properties, creating reservations, checking overlapping dates, and verifying that database changes are saved.

## Project Files

- `PropertyManagementSystem/src/` — Java source code
- `PropertyManagementSystem/test/` — Test files
- `PropertyManagementSystem/properties.db` — SQLite database
- `PropertyManagementSystem/properties.txt` — Sample property records
- `PropertyManagementSystem/PropertyManagementSystem.jar` — Runnable application
- `lib/` — Required libraries

## Author

Mariia Svereniak

DMS Project — Valencia College