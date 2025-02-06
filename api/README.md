# Implement this API

>> Please refer to Implementation detail by Miles Davenport (at base of this page).

#### In this assessment you will be tasked with filling out the functionality of different methods that will be listed further down.

These methods will require some level of API interactions with Mock Employee API at http://localhost:8112/api/v1/employee.

Please keep the following in mind when doing this assessment:
* clean coding practices
* test driven development
* logging
* scalability

### Endpoints to implement

_See `com.reliaquest.api.controller.IEmployeeController` for details._

getAllEmployees()

    output - list of employees
    description - this should return all employees

getEmployeesByNameSearch(...)

    path input - name fragment
    output - list of employees
    description - this should return all employees whose name contains or matches the string input provided

getEmployeeById(...)

    path input - employee ID
    output - employee
    description - this should return a single employee

getHighestSalaryOfEmployees()

    output - integer of the highest salary
    description - this should return a single integer indicating the highest salary of amongst all employees

getTop10HighestEarningEmployeeNames()

    output - list of employees
    description - this should return a list of the top 10 employees based off of their salaries

createEmployee(...)

    body input - attributes necessary to create an employee
    output - employee
    description - this should return a single employee, if created, otherwise error

deleteEmployeeById(...)

    path input - employee ID
    output - name of the employee
    description - this should delete the employee with specified id given, otherwise error

### Testing
Please include proper integration and/or unit tests.

---
#### Implementation detail by Miles Davenport

Employee.java has been created.
+ This contains all the attributes for the employee.

CreateMockEmployeeInput.java and DeleteMockEmployeeInput.java have been copied from the Server project. 
+ These have been used in the createEmployee and deleteEmployeeById endpoints.

Integration tests are in *com.reliaquest.api.controller.EmployeeControllerTest* all endpoints are tested.

##### Assumptions

+ The "api" deleteEmployeeById endpoint uses a Request Parameter.  The DeleteMockEmployeeInput class is used in the "Server" API call.  
+ The "api" createEmployee endpoint uses the CreateMockEmployeeInput as a Request body.
+ I have used RestTemplate to implement the "api" endpoints (calling the Server endpoints).  I have more experience in using this.  I am aware that WebClient could increase performance. 

###### Testing

WebMvcTest and Mockito have been used to exercise endpoints.


###### Manual testing

Start the Server and api.  Run curl commands.

**gradle version**
```
$ gradle --version

------------------------------------------------------------
Gradle 8.7
------------------------------------------------------------

Build time:   2024-03-22 15:52:46 UTC
Revision:     650af14d7653aa949fce5e886e685efc9cf97c10

Kotlin:       1.9.22
Groovy:       3.0.17
Ant:          Apache Ant(TM) version 1.10.13 compiled on January 4 2023
JVM:          17.0.14 (Amazon.com Inc. 17.0.14+7-LTS)
OS:           Linux 6.8.0-52-generic amd64
```

**start the server**
```
$ gradle server:bootRun --warning-mode all
Starting a Gradle Daemon (subsequent builds will be faster)

> Task :server:bootRun
[snip]

<==========---> 80% EXECUTING [1m 53s]
> :server:bootRun
```

**start the api**
```
$ gradle api:bootRun --warning-mode all

> Task :api:bootRun

  .   ____          _            __ _ _
 /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
 \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
  '  |____| .__|_| |_|_| |_\__, | / / / /
 =========|_|==============|___/=/_/_/_/
 :: Spring Boot ::               (v3.2.10)

2025-02-06T16:19:48.073Z  INFO 174823 --- [employee-api] [           main] com.reliaquest.api.ApiApplication        : Starting ApiApplication using Java 17.0.14 with PID 174823 (/home/milesd-9510/workspace/interview/reliaquest/java-employee-challenge/api/build/classes/java/main started by milesd-9510 in /home/milesd-9510/workspace/interview/reliaquest/java-employee-challenge/api)
2025-02-06T16:19:48.074Z  INFO 174823 --- [employee-api] [           main] com.reliaquest.api.ApiApplication        : No active profile set, falling back to 1 default profile: "default"
2025-02-06T16:19:48.476Z  INFO 174823 --- [employee-api] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat initialized with port 8111 (http)
2025-02-06T16:19:48.482Z  INFO 174823 --- [employee-api] [           main] o.apache.catalina.core.StandardService   : Starting service [Tomcat]
2025-02-06T16:19:48.483Z  INFO 174823 --- [employee-api] [           main] o.apache.catalina.core.StandardEngine    : Starting Servlet engine: [Apache Tomcat/10.1.30]
2025-02-06T16:19:48.507Z  INFO 174823 --- [employee-api] [           main] o.a.c.c.C.[Tomcat].[localhost].[/]       : Initializing Spring embedded WebApplicationContext
2025-02-06T16:19:48.508Z  INFO 174823 --- [employee-api] [           main] w.s.c.ServletWebServerApplicationContext : Root WebApplicationContext: initialization completed in 411 ms
2025-02-06T16:19:48.686Z  INFO 174823 --- [employee-api] [           main] o.s.b.w.embedded.tomcat.TomcatWebServer  : Tomcat started on port 8111 (http) with context path ''
2025-02-06T16:19:48.691Z  INFO 174823 --- [employee-api] [           main] com.reliaquest.api.ApiApplication        : Started ApiApplication in 0.803 seconds (process running for 0.95)
<==========---> 80% EXECUTING [36s]
> :api:bootRun
```

###### Endpoints

**createEmployee**
```
$ curl --request POST \
  --url http://localhost:8111/ \
  --header 'content-type: application/json' \
  --data '{
    "name": "Percy Rolfson",
    "salary": 205555,
    "age": 63,
    "title": "Global Liaison"
}'
{"id":"99ddb9fd-f7c5-4be6-a6c8-0333f4f1d01a","employeeName":"Percy Rolfson","employeeSalary":"205555","employeeAge":63,"employeeTitle":"Global Liaison","employeeEmail":"fat_kyle@company.com"}
```

**deleteEmployeeById:**
```
$ curl --request DELETE \
  --url 'http://localhost:8111/?id=c13f778d-fff2-4f34-98bb-c2c078bf54b8' \
  --header 'content-type: application/json'
Mrs. Veronique Rippin Successfully processed request.

$ curl -I --request DELETE   --url 'http://localhost:8111/?id=c13f778d-fff2-4f34-98bb-c2c078bf54b8' --header 'content-type: application/json'
HTTP/1.1 404 
Content-Length: 0
Date: Thu, 06 Feb 2025 15:54:05 GMT
```

**getTop10HighestEarningEmployeeNames**

```
$ curl --request GET \
  --url http://localhost:8111/topTenHighestEarningEmployeeNames
["Ms. Ranee Steuber","Cody Cartwright V","Jae Schmeler","Cole Thiel","Marvin Blick","Clayton Borer","Deirdre Gerhold","Belkis Morissette MD","Carissa Strosin DDS","Samual Glover II"]
```

**getHighestSalaryOfEmployees**
```
$ curl --request GET \
--url http://localhost:8111/highestSalary
498521
```

**getEmployeeById**
```
$ curl --request GET \
  --url http://localhost:8111/ff9da60e-78dd-44b9-ab0b-4e4edf874075
{"id":"ff9da60e-78dd-44b9-ab0b-4e4edf874075","employeeName":"Oliver Rutherford Jr.","employeeSalary":"107460","employeeAge":46,"employeeTitle":"Design Representative","employeeEmail":"quolux@company.com"}
```

**getEmployeesByNameSearch**
```
$ curl --request GET \
  --url http://localhost:8111/search/Oliver
[{"id":"ff9da60e-78dd-44b9-ab0b-4e4edf874075","employeeName":"Oliver Rutherford Jr.","employeeSalary":"107460","employeeAge":46,"employeeTitle":"Design Representative","employeeEmail":"quolux@company.com"}]
```

**getAllEmployees()**
```
curl --request GET \
  --url http://localhost:8112/api/v1/employee
{"data":[{"id":"387abeff-3c5b-4cd4-a2e4-fc9b19af6f8f","employee_name":"Miss Ken Herzog","employee_salary":302330,"employee_age":49,"employee_title":"Customer Healthcare Designer","employee_email":"prodder@company.com"},.... [snip]]
```
end