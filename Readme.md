Monthly Finance Manager

Setup (MySQL):
1. Create DB and user (run sql/schema.sql):

   -- In MySQL as root:
   CREATE DATABASE finance_db;
   CREATE USER 'finance_user'@'localhost' IDENTIFIED BY 'finance_password';
   GRANT ALL PRIVILEGES ON finance_db.* TO 'finance_user'@'localhost';
   FLUSH PRIVILEGES;

2. Run the SQL schema in `sql/schema.sql` to create tables.

Run the application:
- Build: mvn package
- Run: java -jar target/Monthly-Finance-Manager-1.0-SNAPSHOT.jar

Configuration:
- You can override DB connection using environment variables:
  FINANCE_DB_URL, FINANCE_DB_USER, FINANCE_DB_PASSWORD

Notes:
- The app uses JDBC with try-with-resources and PreparedStatements.
- Java 17 is required.

