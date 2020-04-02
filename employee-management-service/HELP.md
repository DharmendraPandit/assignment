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
      1. POST: http://localhost:9090/api/employees/create (user creation)
           payload:
           {
            "name":"Dharmendra",
            "age": 30,
            "salary": 50000
           }
      2. GET: http://localhost:9090/api/employees/search?name=dharmendra (user search base on name)
      3. GET: http://localhost:9090/api/employees/search?age=30 (user search base on age)
      4. POST: http://localhost:9090/api/employees/create/list (user creation in bulk)
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

Assumption about Payroll Service:
 1. base url: http://localhost:9091/payroll
 2. employee create url: POST: http://localhost:9091/payroll/create
    payload:
        {
           "name": "Dharmendra",
           "age": 30,
           "salary": 50000
        }
 3. employee search url: GET: http://localhost:9091/payroll/search?name=Dharmendra/http://localhost:9091/payroll/search?age=30
 4. employee create in bulk url: POST: http://localhost:9091/payroll/create/list
    payload:
        [
           {
               "name": "Dharmendra",
               "age": 30,
               "salary": 50000
           }
       ]
