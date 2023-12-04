package com.example.SchedularApp.service;

import com.example.SchedularApp.Repository.ScheduleRepository;
import com.example.SchedularApp.Repository.User_SchedulesRepository;
import com.example.SchedularApp.dto.User;
import com.example.SchedularApp.modal.Schedule;
import com.example.SchedularApp.modal.User_Schedules;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Service
public class ScheduleService {
  @Autowired
  private ScheduleRepository scheduleRepository;

  @Autowired
  private User_SchedulesRepository user_schedulesRepository;

  @Autowired
  private WebClient webClient;

  public User getDataFromSchedule(String token) {
    return webClient.post()
            .uri("http://localhost:9001/api/data") // endpoint in ServiceB
            .header("Authorization", token)
            .retrieve()
            .bodyToMono(User.class).block();
  }

  //    public ResponseEntity<String> addNewSchedule(Schedule schedule)
//    {
//      scheduleRepository.save(schedule);
//      return ResponseEntity.ok(getDataFromSchedule()+"scheduled task successfully");
//    }
  public void scheduleTaskForUser(Schedule schedule, String token) {
    User user = getDataFromSchedule(token);
    scheduleRepository.save(schedule);
    User_Schedules user_schedules = User_Schedules.builder().us_id(user.getUser_id()).su_id(schedule.getSchedule_id()).build();
    user_schedulesRepository.save(user_schedules);
  }

  public Schedule editScheduleForUser(Schedule schedule, String token) {
    User user = getDataFromSchedule(token);
    Schedule exist = scheduleRepository.findByStartTime(schedule.getStartTime()).orElse(null);
    if (exist != null) {
      schedule.setSchedule_id(exist.getSchedule_id());
      scheduleRepository.save(schedule);
    } else {
      throw new RuntimeException("schedule doesn't exist");
    }
    return exist;
  }

  public Schedule updateScheduleStatus(Long schedule_id, String status, String token) {

    User user = getDataFromSchedule(token);
    Schedule schedule = scheduleRepository.findById(schedule_id).orElse(null);
    if (status != null) {
      schedule.setStatus(status);
    } else {
      throw new RuntimeException("schedule doesn't exist");
    }
    return schedule;
  }

  public List<Schedule> getTasksForDay(LocalDate date,String token) {
    User user = getDataFromSchedule(token);
    LocalDateTime startOfDay = date.atStartOfDay();
    LocalDateTime endOfDay = date.plusDays(1).atStartOfDay().minusNanos(1);

    return scheduleRepository.findByStartTimeBetween(startOfDay, endOfDay);
  }

  public String deleteTask(Long schedule_id, String token) {
    User user = getDataFromSchedule(token);
    if (scheduleRepository.findById(schedule_id).isPresent()) {
      scheduleRepository.deleteById(schedule_id);
      return "deleted";
    }
      return "not deleted";
  }
}
