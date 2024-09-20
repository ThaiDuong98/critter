package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Schedule;

import java.util.List;

public interface ScheduleService {
    Schedule saveSchedule(Schedule schedule);
    List<Schedule> getAllSchedules();
    List<Schedule> getScheduleByPetId(Long petId);
    List<Schedule> getScheduleByEmployeeId(Long employeeId);
    List<Schedule> getScheduleByCustomerId(Long customerId);
}
