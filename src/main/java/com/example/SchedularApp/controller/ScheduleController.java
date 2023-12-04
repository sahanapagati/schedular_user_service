package com.example.SchedularApp.controller;

import com.example.SchedularApp.Repository.ScheduleRepository;
import com.example.SchedularApp.modal.Schedule;
import com.example.SchedularApp.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Schedules;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
public class ScheduleController
{
    @Autowired
    private ScheduleService scheduleService;

    @PostMapping("/add")
    public ResponseEntity<Schedule> scheduleTaskForUser(@RequestBody Schedule schedule, @RequestHeader("Authorization") String token)
    {
        scheduleService.scheduleTaskForUser(schedule,token);
        return new ResponseEntity<>(schedule, HttpStatus.OK);
    }

    @PostMapping("/update")
    public ResponseEntity<Schedule> editTaskForUser(@RequestBody Schedule schedule, @RequestHeader("Authorization") String token)
    {
        Schedule updated= scheduleService.editScheduleForUser(schedule,token);
        return new ResponseEntity<>(updated,HttpStatus.OK);
    }
    @PostMapping("/update_status")
    public ResponseEntity<Schedule> updater(@RequestParam Long schedule_id,@RequestBody String status, @RequestHeader("Authorization") String token)
    {
        Schedule updated= scheduleService.updateScheduleStatus(schedule_id,status,token);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<Schedule>> getTasksForDay(@RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,@RequestHeader("Authorization") String token)
    {
        List<Schedule> tasks = scheduleService.getTasksForDay(date,token);

        if (!tasks.isEmpty()) {
            return new ResponseEntity<>(tasks, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PostMapping("/delete_task")
    public ResponseEntity<String> deleteTask(@RequestParam Long schedule_id,@RequestHeader("Authorization") String token)
    {
        scheduleService.deleteTask(schedule_id,token);
        return new ResponseEntity<>("deleted successfully", HttpStatus.OK);
    }

}
