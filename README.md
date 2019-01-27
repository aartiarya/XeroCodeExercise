# XeroCodeExercise
This repository is a coding exercise given by Xero Team. The requirement is to automate a test case where user can add the ANZ-AU bank account to xero application.

Getting Started
========================================================================
Before you copy this project and start running the test suite on your local machine for testing and evaluation purpose, please make sure you have the below software system under Dependencies installed on your machine. The Test cases has been developed and tested on MacOS Mojave system 10.14.2

Dependencies
=======================================================================

1. Java JDK installed
     java -version
   java version "1.8.0_201"
   Java(TM) SE Runtime Environment (build 1.8.0_201-b09)
   Java HotSpot(TM) 64-Bit Server VM (build 25.201-b09, mixed mode)
    
2. Eclipse IDE for Enterprise Java Developers
    Version 2018-12 (4.10.0)

3. Apache Maven 3.6.0 installed.

4. Selenium Standalone Server 3.141.59

5. Chrome Driver 2.45

This is how the project framework structure looks like
========================================================================

├── src   
│   └── test
│       ├── java
│       │   ├── helpers
│       │   │   └── DateFunctions.java
|       |   |   └── EnvironmentProperties.java
|       |   |   └── GenericTest.java
|       |   |   └── PageFunctions.java  
|       |   |   └── WebDriver.java
│       │   ├── SeleniumObjects
│       │   │   └── LoginPage.java
|       |   |   └── XeroDashboardPage.java
│       │   └── SeleniumTests
|       |   |    └── AddBankAccountToXero.java
|       |   ├── XeroFunctions
│       │   │   └── XeroFunctions.java
|       |   | 
│       └── resources
│       |    └── env.properties
|       |    └── properties.test-env
|       |    └── xero.yml
│       └── target
│              └── AutomationReports
|              └── properties.test-env
|                    └──ExtentReport
|                    └──ScreenshotInPDF
├── pom.xml

AddBankAccountToXero.java
=====================================================================

This test class is created to login to Xero account via two factor authentication and Add bank account details of specific user.
It has four steps:
1. Login to Xero
2. Perform two factor authentication
3. Add ANZ(AU) Bank account to user 'Aarti Arya'
4. Verify account is added successfully.

I recommend you to use JUnit to run this test.

Reporting
=====================================================================

Extent Reporting is used for validating the test results
1. Extent html report is generated under src/target/properties.test-env folder
2. A PDF report is generated under src/target/properties.test-env folder
