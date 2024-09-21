package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Schedule;

import java.util.List;

public interface ScheduleService {
    Schedule saveSchedule(Schedule schedule);
    List<Schedule> getAllSchedules();
    List<Schedule> findByPet(Long petId);
    List<Schedule> findByEmployees(Long employeeId);
    List<Schedule> getScheduleByCustomerId(Long customerId);
}
