package com.example.employee.service;

import com.example.employee.exception.EmployeeException;
import com.example.employee.gateway.PayrollGateway;
import com.example.employee.model.Employee;
import com.example.employee.repository.EmployeeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.omg.CORBA.PUBLIC_MEMBER;

import java.util.*;

import static org.mockito.Mockito.when;

public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService service;

    @Mock
    private EmployeeRepository repository;
    @Mock
    private PayrollGateway gateway;
    private Employee employee;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        employee = create();
    }

    @Test
    public void createSuccess() {
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        when(gateway.create(employee)).thenReturn(employee);
        when(repository.findByNameIgnoreCaseContaining(employee.getName())).thenReturn(employeeList);
        when(repository.save(employee)).thenReturn(employee);
        Employee actualResult = service.create(employee);
        Assertions.assertEquals(actualResult.getName(), employee.getName());
        Assertions.assertEquals(actualResult.getAge(), employee.getAge());
    }

    @Test
    public void createInvalidInputName() {
        employee.setName(null);
        Assertions.assertThrows(EmployeeException.class, () -> {
            service.create(employee);
        });
    }

    @Test
    public void createInvalidInputAge() {
        employee.setAge(-10);
        Assertions.assertThrows(EmployeeException.class, () -> {
            service.create(employee);
        });
    }

    @Test
    public void createPayrollFailed() {
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        when(gateway.create(employee)).thenReturn(employee);
        when(repository.findByNameIgnoreCaseContaining(employee.getName())).thenReturn(employeeList);
        when(gateway.create(employee)).thenThrow(EmployeeException.class);
        Assertions.assertThrows(EmployeeException.class, () -> {
            service.create(employee);
        });
    }

    @Test
    public void searchSuccessForName() {
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        when(gateway.search(new Employee(employee.getName(), employee.getAge()))).thenReturn(employeeList);
        when(repository.findByNameIgnoreCaseContaining(employee.getName())).thenReturn(employeeList);
        List<Employee> actualResult = service.search(employee.getName(), employee.getAge());
        Assertions.assertEquals(actualResult.get(0).getName(), employee.getName());
        Assertions.assertEquals(actualResult.get(0).getAge(), employee.getAge());
    }

    @Test
    public void searchSuccessForAge() {
        List<Employee> employeeList = new ArrayList<>();
        Employee expectedResult = create();
        employeeList.add(expectedResult);
        employee.setName(null);
        when(gateway.search(new Employee(employee.getName(), employee.getAge()))).thenReturn(employeeList);
        when(repository.findByAge(employee.getAge())).thenReturn(employeeList);
        List<Employee> actualResult = service.search(employee.getName(), employee.getAge());
        Assertions.assertEquals(actualResult.get(0).getName(), expectedResult.getName());
        Assertions.assertEquals(actualResult.get(0).getAge(), expectedResult.getAge());
    }

    @Test
    public void searchInvalidInput() {
        employee.setName(null);
        employee.setAge(null);
        Assertions.assertThrows(EmployeeException.class, () -> {
            service.search(employee.getName(), employee.getAge());
        });
    }

    @Test
    public void searchPayrollFailed() {
        when(gateway.search(employee)).thenThrow(EmployeeException.class);
        Assertions.assertThrows(EmployeeException.class, () -> {
            service.search(employee.getName(), employee.getAge());
        });
    }

    @Test
    public void searchPayrollResponseNull() {
        when(gateway.search(employee)).thenReturn(null);
        Assertions.assertThrows(EmployeeException.class, () -> {
            service.search(employee.getName(), employee.getAge());
        });
    }

    @Test
    public void createEmployeesSuccess() {
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        Map<String, List<Employee>> expectedResult = new HashMap<>();
        expectedResult.put("success", employeeList);
        expectedResult.put("failure", employeeList);
        when(gateway.createEmployees(employeeList)).thenReturn(expectedResult);
        when(repository.saveAll(employeeList)).thenReturn(employeeList);
        Map<String, List<Employee>> actualResult = service.createEmployees(employeeList);
        Assertions.assertEquals(actualResult.get("success").get(0).getName(), expectedResult.get("success").get(0).getName());
    }

    @Test
    public void createEmployeesInvalidInput() {
        List<Employee> employeeList = new ArrayList<>();
        Assertions.assertThrows(EmployeeException.class, () -> {
            service.createEmployees(employeeList);
        });
    }

    @Test
    public void createEmployeesInvalidInput2() {
        Assertions.assertThrows(EmployeeException.class, () -> {
            service.createEmployees(null);
        });
    }

    private Employee create() {
        Employee employee = new Employee();
        employee.setId("123456");
        employee.setName("Dharmendra Pandit");
        employee.setAge(30);
        employee.setSalary(50000D);
        employee.setCreatedOn(new Date());
        return employee;
    }
}
