package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.enums.EmployeeSkill;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public interface EmployeeService {
    public Employee saveEmployee(Employee employee);
    public Employee getEmployeeById(Long employeeId);
    public void setAvailableEmployee(Set<DayOfWeek> availableDay, long employeeId);
    public List<Employee> findEmployeeForService(Set<EmployeeSkill> skills, LocalDate date);
}
