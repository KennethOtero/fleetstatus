Fleet Status
---
## Project Summary

Fleet Status will be a web-based application to track fleet status and provide real time information in regard to broken
or out-of-service aircraft, and can be accessed at any time by company stakeholders. This application is being developed
for DHL Express, a large international shipping company.  DHL is owned by a German company, Deutsche Post, and operates
several of its own airlines in different parts of the world. However in the United States, most of the flying is done by
DHL’s U.S. and Canadian Airline partners like ABX Air, Atlas, Amerijet, and Cargojet.  In some cases DHL owns the aircraft,
but those aircraft are still operated by DHL’s airline partners in what’s called a CMI agreement, where the DHL provides
the aircraft and the Airline provides the crew, maintenance and insurance. The Fleet Status application will be a flexible
tool that allows DHL Network Control to keep track of the aircraft status for the hundreds of aircraft operating in the
DHL Network even though they are being flown by up to 16 different Airlines.

## Team Members and Roles

- Project Manager: Steven Falconieri
- Developers:
    - Kenneth Otero
    - Yifan Bian
    - Haley Beyersdoerfer
    - Ghana Gautam
- Advisor: Vismaya Manchaiah

## Weekly Meeting

Mondays at 2:00 PM on Teams.

## Running the Application
Ensure that your MS SQL Server database connection is working before running the application. 

If you wish to access the application using
the remote Azure database, uncomment the following lines in `application.yml` (located in src > main > resources):
```yaml
# Azure database connection
spring:
  datasource:
    url: jdbc:sqlserver://dhlfleetstatus.database.windows.net;encrypt=true;trustServerCertificate=true;databaseName=dhl_fleetstatus
    username: fleetstatus_admin
    password: P@$$w0rd2!
```

If running the application using a local MS SQL Server database, adjust these settings to match your local configuration. In addition,
run the `DB_Reset_Script.sql` script (located in src > main > resources > database) to create the database schema and 
test data.
