package com.udacity.jdnd.course3.critter.service.iml;

import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.enums.EmployeeSkill;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class EmployeeServiceImpl implements EmployeeService {
    private final EmployeeRepository employeeRepository;

    @Override
    public Employee saveEmployee(Employee employee){
        return employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeById(Long employeeId){
        return employeeRepository.findById(employeeId).orElse(null);
    }

    @Override
    public void setAvailableEmployee(Set<DayOfWeek> availableDay, long employeeId){
        Employee employee = getEmployeeById(employeeId);
        if(employee != null){
            employee.setDaysAvailable(availableDay);
            employeeRepository.save(employee);
        }
    }

    @Override
    public List<Employee> findEmployeeForService(Set<EmployeeSkill> skills, LocalDate date){
        List<Employee> availableEmployees = new ArrayList<>();
        List<Employee> employees = employeeRepository.findEmployeeByAvailableDays(date.getDayOfWeek());

        for (Employee employee : employees) {
            if (employee.getSkills().containsAll(skills)) {
                availableEmployees.add(employee);
            }
        }
        return availableEmployees;
    }
}
