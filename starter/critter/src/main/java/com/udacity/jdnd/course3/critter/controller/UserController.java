package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.dto.CustomerDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeDTO;
import com.udacity.jdnd.course3.critter.dto.EmployeeRequestDTO;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Users.
 *
 * Includes requests for both customers and employees. Splitting this into separate user and customer controllers
 * would be fine too, though that is not part of the required scope for this class.
 */
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final CustomerService customerService;
    private final PetService petService;
    private final EmployeeService employeeService;

    @PostMapping("/customer")
    public CustomerDTO saveCustomer(@RequestBody CustomerDTO customerDTO){
        try{
            List<Pet> pets = Optional.ofNullable(customerDTO.getPetIds())
                    .orElse(Collections.emptyList())
                    .stream()
                    .map(petService::getPetById)
                    .collect(Collectors.toList());

            Customer customer = convertCustomerDTOToEntity(customerDTO);
            customer.setPets(pets);

            Customer savedCustomer = customerService.saveCustomer(customer);
            return convertCustomerEntityToDTO(savedCustomer);
        }catch(UnsupportedOperationException e){
            throw new UnsupportedOperationException();
        }
    }

    @GetMapping("/customer")
    public List<CustomerDTO> getAllCustomers(){
        try{
            return customerService.getAllCustomers().stream()
                    .map(this::convertCustomerEntityToDTO)
                    .collect(Collectors.toList());
        }catch (UnsupportedOperationException e){
            throw new UnsupportedOperationException();
        }
    }

    @GetMapping("/customer/pet/{petId}")
    public CustomerDTO getOwnerByPet(@PathVariable long petId){
        try{
            return convertCustomerEntityToDTO(customerService.getOwnByPetId(petId));
        }catch (UnsupportedOperationException e){
            throw new UnsupportedOperationException();
        }
    }

    @PostMapping("/employee")
    public EmployeeDTO saveEmployee(@RequestBody EmployeeDTO employeeDTO) {
        try{
            ModelMapper modelMapper = new ModelMapper();
            modelMapper.map(employeeDTO, Employee.class);

            Employee convertedEmployee = modelMapper.map(employeeDTO, Employee.class);
            Employee savedEmployee = employeeService.saveEmployee(convertedEmployee);
            employeeDTO.setId(savedEmployee.getId());
            return employeeDTO;
        }catch (UnsupportedOperationException e){
            throw new UnsupportedOperationException();
        }
    }

    @PostMapping("/employee/{employeeId}")
    public EmployeeDTO getEmployee(@PathVariable long employeeId) {
        try{
            ModelMapper modelMapper = new ModelMapper();
            Employee employee = employeeService.getEmployeeById(employeeId);

            return modelMapper.map(employee, EmployeeDTO.class);
        }catch (UnsupportedOperationException e){
            throw new UnsupportedOperationException();
        }
    }

    @PutMapping("/employee/{employeeId}")
    public void setAvailability(@RequestBody Set<DayOfWeek> daysAvailable, @PathVariable long employeeId) {
        try{
            employeeService.setAvailableEmployee(daysAvailable, employeeId);
        }catch (UnsupportedOperationException e){
            throw new UnsupportedOperationException();
        }
    }

    @GetMapping("/employee/availability")
    public List<EmployeeDTO> findEmployeesForService(@RequestBody EmployeeRequestDTO employeeDTO) {
        try{
            ModelMapper modelMapper = new ModelMapper();
            LocalDate daysAvailable = employeeDTO.getDate();
            List<Employee> employees = employeeService.findEmployeeForService(employeeDTO.getSkills(), daysAvailable);

            return employees.stream()
                    .map(employee -> modelMapper.map(employee, EmployeeDTO.class))
                    .collect(Collectors.toList());
        }catch (UnsupportedOperationException e){
            throw new UnsupportedOperationException();
        }
    }

    private Customer convertCustomerDTOToEntity(CustomerDTO customerDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Customer customer = modelMapper.map(customerDTO, Customer.class);

        Optional.ofNullable(customerDTO.getPetIds())
                .ifPresent(petIds -> petIds.forEach(petId ->
                        customer.addPet(petService.getPetById(petId))
                ));

        return customer;
    }

    private CustomerDTO convertCustomerEntityToDTO(Customer customer) {
        ModelMapper modelMapper = new ModelMapper();
        CustomerDTO customerDTO = modelMapper.map(customer, CustomerDTO.class);

        List<Long> petIds = customer.getPets().stream()
                .map(Pet::getId)
                .collect(Collectors.toList());

        customerDTO.setPetIds(petIds);
        return customerDTO;
    }
}
