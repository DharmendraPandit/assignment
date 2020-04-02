package com.example.employee.gateway;

import com.example.employee.exception.EmployeeException;
import com.example.employee.exception.EmployeeMessages;
import com.example.employee.model.Employee;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class PayrollGateway {

    final Logger log = LoggerFactory.getLogger(PayrollGateway.class);

    @Value("${payroll.url:http://localhost:9091}")
    private String payrollUrl;

    @Autowired
    private RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "fallbackCreate")
    public Employee create(Employee employee) {
        String createUrl = payrollUrl+ "/create";
        return restTemplate.postForObject(createUrl, employee, Employee.class);
    }

    public Employee fallbackCreate(Employee employee) {
        log.error("exception in side fallbackCreate(): {}", employee);
        throw new EmployeeException(HttpStatus.BAD_REQUEST.value(), EmployeeMessages.PAYROLL_ERROR.getMessage());
    }

    // commandProperties = {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "3000")}
    @HystrixCommand(fallbackMethod = "fallbackSearch")
    public List<Employee> search(Employee employee) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/json");
        HttpEntity<?> entity = new HttpEntity<>(headers);
        StringBuilder searchUrl = new StringBuilder(payrollUrl);
        if(employee.getName() != null) {
            searchUrl.append("/search?name=").append(employee.getName());
        } else {
            searchUrl.append("/search?age=").append(employee.getAge());
        }
        ResponseEntity<Employee[]> response = restTemplate.exchange(searchUrl.toString(), HttpMethod.GET, entity, Employee[].class);
        if(response.getBody() != null) {
            return Arrays.asList(response.getBody());
        }
        return null;
    }

    public List<Employee> fallbackSearch(Employee employee) {
        log.error("exception in side fallbackSearch(): {}", employee);
        throw new EmployeeException(HttpStatus.BAD_REQUEST.value(), EmployeeMessages.PAYROLL_ERROR.getMessage());
    }

    @HystrixCommand(fallbackMethod = "fallbackCreateEmployees")
    public Map<String, List<Employee>> createEmployees(List<Employee> employeeList) {
        String createUrl = payrollUrl+ "/create/list";
        ParameterizedTypeReference<Map<String, List<Employee>>> responseType = new ParameterizedTypeReference<Map<String, List<Employee>>>() {};
        HttpHeaders headers = new HttpHeaders();
        headers.add("content-type", "application/json");
        HttpEntity<List<Employee>> entity = new HttpEntity<>(employeeList, headers);
        Map<String, List<Employee>> employeeResponse = restTemplate.exchange(createUrl, HttpMethod.POST, entity, responseType).getBody();
        if(employeeResponse != null) {
            return employeeResponse;
        }
        return null;
    }

    public Map<String, List<Employee>> fallbackCreateEmployees(List<Employee> employeeList) {
        log.error("exception in side fallbackCreate(): {}", employeeList);
        throw new EmployeeException(HttpStatus.BAD_REQUEST.value(), EmployeeMessages.PAYROLL_ERROR.getMessage());
    }
}
