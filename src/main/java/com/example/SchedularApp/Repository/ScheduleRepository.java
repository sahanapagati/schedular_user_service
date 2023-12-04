package com.example.SchedularApp.Repository;

import com.example.SchedularApp.modal.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule,Long> {
    Optional<Schedule> findByStartTime(LocalDateTime startTime);

    List<Schedule> findByStartTimeBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}

