package com.example.employee.repository;

import com.example.employee.model.Employee;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, String> {
    public List<Employee> findByNameIgnoreCaseContaining(String name);
    public List<Employee> findByAge(Integer age);
}
