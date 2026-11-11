# OrangeHRM Selenium Automation Framework

Automated UI tests for the OrangeHRM demo website, built with Java, Selenium WebDriver, and TestNG using the Page Object Model.

## Tech stack
- Java 17
- Selenium WebDriver 4
- TestNG
- Maven
- Extent Reports

## What is tested
- Valid login and Dashboard check
- Invalid login (negative testing)
- Data-driven invalid login with TestNG DataProvider (3 data sets)
- Navigation to PIM and Admin pages
- Logout

## Framework features
- Page Object Model (LoginPage, DashboardPage)
- BaseTest for browser setup and teardown
- Explicit and fluent waits
- config.properties for URL and credentials
- Screenshot on failure
- Extent HTML report
- testng.xml suite

## Project structure