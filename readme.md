# JasperCLI

Is a simple CLI to generate reports from JasperReports (*.jrxml) to PDF, XLSX and base64, this is a little project made with Maven, just need compile with **mvn install**.

I made this code to get reports with a PostgreSQL DB, so i have tested on it, but in theory you can use it with any DBMS, just need add the JDBC library dependecy on **pom.xml**.

## Requeriments

- Java JDK 1.8

## Usage

### Arguments

Argument | Description
---------|------------
-c | JDBC Connection String ex: **jdbc:postgresql://localhost:5432/mydb**
-u | (optional) User from Database (can be passed by connection string)
-p | (optional) Password from Database (can be passed by connection string)
-f | (Optional) define the output format, valid values PDF,XLSX,BASE64. If you don't define it, the output will be displayed at screen
-o | Output file, is the full filename where are you going to save the report (Base64 does not need output file, it will be shown in console output)
-jrxml | Jasper Reports file, is the filename of your JasperStudio file.
-params | Is a comma separated array where you define the parameters of your reports, for each parameter you must define the type(ex: java.sql.Date), the name and the value. 

## Example

```console
java -jar target/JasperCLI-0.1.jar -c jdbc:postgresql://localhost:5432/mydb -u myuser -p mypass -f pdf -o myreport.pdf -jrxml path/to/myfilejasper.jrxml -params [java.sql.date parameter1=01/01/2018, java.sql.date parameter2=31/12/2022]
```
