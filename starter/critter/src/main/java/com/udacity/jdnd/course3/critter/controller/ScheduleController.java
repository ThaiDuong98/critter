package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.dto.ScheduleDTO;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.exception.ResourceNotFoundException;
import com.udacity.jdnd.course3.critter.service.EmployeeService;
import com.udacity.jdnd.course3.critter.service.PetService;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Handles web requests related to Schedules.
 */
@RestController
@RequestMapping("/schedule")
@RequiredArgsConstructor
public class ScheduleController {
    private final ScheduleService scheduleService;
    private final PetService petService;
    private final EmployeeService employeeService;

    @PostMapping
    public ScheduleDTO createSchedule(@RequestBody ScheduleDTO scheduleDTO) {
        try{
            return convertEntityToScheduleDTO(scheduleService.saveSchedule(convertScheduleDTOToEntity(scheduleDTO)));
        }catch(UnsupportedOperationException e){
            throw new UnsupportedOperationException();
        }
    }

    @GetMapping
    public List<ScheduleDTO> getAllSchedules() {
        try{
            return scheduleService.getAllSchedules().stream()
                    .map(this::convertEntityToScheduleDTO)
                    .collect(Collectors.toList());
        }catch (UnsupportedOperationException e){
            throw new UnsupportedOperationException();
        }
    }

    @GetMapping("/pet/{petId}")
    public List<ScheduleDTO> getScheduleForPet(@PathVariable long petId) {
        try{
            return scheduleService.findByPet(petId).stream()
                    .map(this::convertEntityToScheduleDTO)
                    .collect(Collectors.toList());
        }catch (UnsupportedOperationException e){
            throw new UnsupportedOperationException();
        }
    }

    @GetMapping("/employee/{employeeId}")
    public List<ScheduleDTO> getScheduleForEmployee(@PathVariable long employeeId) {
        try{
            return scheduleService.findByEmployees(employeeId).stream()
                    .map(this::convertEntityToScheduleDTO)
                    .collect(Collectors.toList());
        }catch (UnsupportedOperationException e){
            throw new UnsupportedOperationException();
        }
    }

    @GetMapping("/customer/{customerId}")
    public List<ScheduleDTO> getScheduleForCustomer(@PathVariable long customerId) {
        try{
            return scheduleService.getScheduleByCustomerId(customerId).stream()
                    .map(this::convertEntityToScheduleDTO)
                    .collect(Collectors.toList());
        }catch (UnsupportedOperationException e){
            throw new UnsupportedOperationException();
        }
    }

    private ScheduleDTO convertEntityToScheduleDTO(Schedule schedule) {
        ScheduleDTO scheduleDTO = new ScheduleDTO();
        BeanUtils.copyProperties(schedule, scheduleDTO);

        scheduleDTO.setActivities(schedule.getActivities());

        List<Long> petIds = schedule.getPets().stream()
                .map(Pet::getId)
                .collect(Collectors.toList());

        scheduleDTO.setPetIds(petIds);

        List<Long> employeeIds = schedule.getEmployees().stream()
                .map(Employee::getId)
                .collect(Collectors.toList());

        scheduleDTO.setEmployeeIds(employeeIds);

        return scheduleDTO;
    }

    private Schedule convertScheduleDTOToEntity(ScheduleDTO scheduleDTO) {
        ModelMapper modelMapper = new ModelMapper();
        Schedule schedule = modelMapper.map(scheduleDTO, Schedule.class);

        // Set activities
        schedule.setActivities(scheduleDTO.getActivities());

        // Employee IDs to Employee Entities
        List<Employee> employees = scheduleDTO.getEmployeeIds().stream()
                .map(employeeService::getEmployeeById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (employees.size() != scheduleDTO.getEmployeeIds().size()) {
            throw new ResourceNotFoundException("Employees not found. Please try again.");
        }

        schedule.setEmployees(employees);

        // Map Pet IDs to Pet Entities
        List<Pet> pets = scheduleDTO.getPetIds().stream()
                .map(petService::getPetById)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if (pets.size() != scheduleDTO.getPetIds().size()) {
            throw new ResourceNotFoundException("Pet not found. Please try again.");
        }

        schedule.setPets(pets);

        return schedule;
    }

}
