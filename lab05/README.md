# Lab 05 - JDBC (with SQLite)
Paweł Idziak

## Info
The application represents simple connection to the database with using JDBC driver and SQLite (more precisely: https://mvnrepository.com/artifact/org.xerial/sqlite-jdbc). The database schema is hard-coded and creates the whole tables and some data (users, services) during startup. The application allows user (client) to make reservation in "car repair shop". 

- Avaliable services: changing wheel, chaging tires, 
- Each service has own price
- Price depends on wheel size
- Reservation operations: 
    - make - create field in DB, with status "open"
    - cancel - update field in DB (changing status to "canceled")
- Possibility of select hour of reservation

DB schema should looks like this:
![alt DB schema image](https://github.com/pawelidziak/Java-techniki-zaawansowane/blob/master/lab05/db_schema_image.png)
