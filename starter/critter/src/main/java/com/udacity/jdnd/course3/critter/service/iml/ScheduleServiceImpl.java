package com.udacity.jdnd.course3.critter.service.iml;

import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Employee;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.entity.Schedule;
import com.udacity.jdnd.course3.critter.exception.ResourceNotFoundException;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.EmployeeRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.repository.ScheduleRepository;
import com.udacity.jdnd.course3.critter.service.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final CustomerRepository customerRepository;
    private final PetRepository petRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public Schedule saveSchedule(Schedule schedule){
        return scheduleRepository.save(schedule);
    }

    @Override
    public List<Schedule> getAllSchedules(){
        return (List<Schedule>) scheduleRepository.findAll();
    }

    @Override
    public List<Schedule> findByPet(Long petId){
        Pet pet = petRepository.findById(petId).orElseThrow(() -> new ResourceNotFoundException());
        return (List<Schedule>) scheduleRepository.findByPets(pet);
    }

    @Override
    public List<Schedule> findByEmployees(Long employeeId){
        Employee employee = employeeRepository.findById(employeeId).orElseThrow(() -> new ResourceNotFoundException());
        return (List<Schedule>) scheduleRepository.findByEmployees(employee);
    }

    @Override
    public List<Schedule> getScheduleByCustomerId(Long customerId){
        Optional<Customer> optionalCustomer = customerRepository.findById(customerId);
        if(!optionalCustomer.isPresent()){
            throw new ResourceNotFoundException();
        }else {
            List<Pet> pets = optionalCustomer.get().getPets();
            List<Schedule> schedules = new ArrayList<>();

            for (Pet pet : pets) {
                schedules.addAll(scheduleRepository.findByPets(pet));
            }
            return schedules;
        }
    }
}
