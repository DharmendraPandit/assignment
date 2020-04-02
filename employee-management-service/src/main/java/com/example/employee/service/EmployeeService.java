package com.example.employee.service;

import com.example.employee.exception.EmployeeException;
import com.example.employee.exception.EmployeeMessages;
import com.example.employee.gateway.PayrollGateway;
import com.example.employee.model.Employee;
import com.example.employee.repository.EmployeeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class EmployeeService {

    final Logger log = LoggerFactory.getLogger(EmployeeService.class);
    @Autowired
    private EmployeeRepository repository;
    @Autowired
    private PayrollGateway gateway;

    public Employee create(Employee employee) {
        if (StringUtils.isEmpty(employee.getName()) || employee.getSalary() < 0 || employee.getAge() < 0) {
            throw new EmployeeException(HttpStatus.BAD_REQUEST.value(), EmployeeMessages.MANADATORY_FIELDS.getMessage());
        }
        List<Employee> employeeList = repository.findByNameIgnoreCaseContaining(employee.getName());
        if (!employeeList.isEmpty()) {
            employee.setName(employee.getName() + (employeeList.size() + 1));
        }
        Employee payrollResponse = gateway.create(employee);
        if (payrollResponse != null) {
            employee.setCreatedOn(new Date());
            return repository.save(employee);
        } else {
            throw new EmployeeException(HttpStatus.BAD_REQUEST.value(), EmployeeMessages.PAYROLL_ERROR.getMessage());
        }
    }

    public List<Employee> search(String name, Integer age) {
        if (StringUtils.isEmpty(name) && StringUtils.isEmpty(age)) {
            throw new EmployeeException(HttpStatus.BAD_REQUEST.value(), EmployeeMessages.SEARCH_FIELDS.getMessage());
        }
        List<Employee> payrollResponse = gateway.search(new Employee(name, age));
        if (payrollResponse != null && !payrollResponse.isEmpty()) {
            List<Employee> dbResult = Collections.EMPTY_LIST;
            if (name != null) {
                dbResult = repository.findByNameIgnoreCaseContaining(name);
            } else {
                dbResult = repository.findByAge(age);
            }
            Set<String> payrollEmpList = dbResult.stream().map(employee -> employee.getName()).collect(Collectors.toSet());
            return payrollResponse.stream().filter(element -> payrollEmpList.contains(element.getName())).collect(Collectors.toList());

        } else {
            throw new EmployeeException(HttpStatus.BAD_REQUEST.value(), EmployeeMessages.NOT_FOUND.getMessage());
        }
    }

    public Map<String, List<Employee>> createEmployees(List<Employee> employeeList) {
        if (employeeList == null || employeeList.isEmpty()) {
            throw new EmployeeException(HttpStatus.BAD_REQUEST.value(), EmployeeMessages.INVALID_INPUT.getMessage());
        }

        Map<String, List<Employee>> payrollResponse = gateway.createEmployees(employeeList);
        if (payrollResponse != null) {
            for (Map.Entry<String, List<Employee>> entry : payrollResponse.entrySet()) {
                if ("Success".equalsIgnoreCase(entry.getKey())) {
                    repository.saveAll(entry.getValue());
                }
            }
            return payrollResponse;
        } else {
            throw new EmployeeException(HttpStatus.BAD_REQUEST.value(), EmployeeMessages.PAYROLL_ERROR.getMessage());
        }
    }
}
