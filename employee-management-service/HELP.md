# Employee Management assignment

Steps to run the maven base spring boot application:
  1. mvn install
  2. java -jar target/employee-management-service-0.0.1-SNAPSHOT.jar 
     or 
     mvn spring-boot:run
     or
     ./mvnw spring-boot:run
     
Swagger ui url:  GET: http://localhost:9090/api/swagger-ui.html

Actuator: GET: http://localhost:9090/api/actuator

Health status url: GET: http://localhost:9090/api/actuator/health

Contanarization:
1. $ docker build -t netent/employee-management-service .
2. $ docker run -d -p 9090:9090 netent/employee-management-service

 Mongo DB connection:
   1. mongodb://localhost:27017/netent-employees
 
 Service details:
   1. Port: 9090
   2. Context path: /api
   3. Service urls:
   POST: http://localhost:9090/api/employees/create (user creation)
   payload:
   {
   	"name":"Dharmendra",
   	"age": 30,
   	"salary": 50000
   }
   GET: http://localhost:9090/api/employees/search?name=dharmendra (user search base on name)
   GET: http://localhost:9090/api/employees/search?age=30 (user search base on age)
   POST: http://localhost:9090/api/employees/create/list (user creation in bulk)
   payload:
   [
       {
           "name": "Dharmendra",
           "age": 30,
           "salary": 50000
       }
   ]
   response:
   {
       "success": [
           {
               "id": "12345",
               "name": "Dharmendra",
               "age": 30,
               "salary": null,
               "createdOn": null
           }
       ]
   }