# SeleniumDemo

This is a Java-based project that uses Selenium WebDriver for automating web applications for testing purposes. The project is built with Maven, which is a software project management and comprehension tool.

## Project Structure

The project is structured as follows:

- `pom.xml`: This is the Maven Project Object Model file. It contains information about the project and configuration details used by Maven to build the project.
- `src/test/java/demo/example/Main.java`: This is the main test script that contains the Selenium WebDriver code for automating the web application.

## Dependencies

The project uses the following dependencies:

- Selenium WebDriver: This is a collection of open-source APIs which are used to automate the testing of a web application.
- WebDriverManager: This library allows to automate the management of the drivers required by Selenium WebDriver.
- JUnit: This is a simple framework to write repeatable tests. It is used for testing and its assertion methods are used in this project.
- ExtentReports: This is a reporting library for automation testing. It creates interactive and detailed reports for your tests.

## How to Run

1. Clone the repository to your local machine.
2. Open the project in your preferred IDE (The project is set up with IntelliJ IDEA).
3. Ensure that you have Maven installed on your machine.
4. Run the `Main.java` file to execute the tests.

## Test Flow

The `Main.java` file contains the following test flow:

1. Initialize the report.
2. Set up the WebDriver.
3. Navigate to the site.
4. Perform login.
5. Verify login.
6. Search and select an item.
7. Handle multiple windows.
8. Perform checkout.
9. Handle alert.
10. Cleanup and quit the driver.

## Reporting

The project uses ExtentReports for reporting. After the tests are run, a detailed report is generated in the `target/ExtentReports` directory.

## Screenshots

Screenshots are taken at various stages of the test and are stored in the `target/Screenshots` directory.