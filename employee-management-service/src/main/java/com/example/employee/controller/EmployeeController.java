package com.example.employee.controller;

import com.example.employee.exception.EmployeeException;
import com.example.employee.model.Employee;
import com.example.employee.service.EmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService service;

    @ExceptionHandler(EmployeeException.class)
    void handleUserException(EmployeeException ex, HttpServletResponse response) throws IOException {
        response.sendError(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
    }

    @PostMapping("/create")
    private ResponseEntity<Employee> create(@RequestBody Employee employee) {
        return new ResponseEntity<>(service.create(employee), HttpStatus.CREATED);
    }

    @GetMapping("/search")
    private ResponseEntity<List<Employee>> search(@RequestParam(value = "name", required = false) String name,
                                                  @RequestParam(value = "age", required = false) Integer age) {
        return new ResponseEntity<>(service.search(name, age), HttpStatus.OK);
    }

    @PostMapping("/create/list")
    private ResponseEntity<Map<String, List<Employee>>> createEmployees(@RequestBody List<Employee> employeeList) {
        return new ResponseEntity<>(service.createEmployees(employeeList), HttpStatus.CREATED);
    }
}
