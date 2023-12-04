package com.example.SchedularApp.Repository;

import com.example.SchedularApp.dto.User;
import com.example.SchedularApp.modal.User_Schedules;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface User_SchedulesRepository extends JpaRepository<User_Schedules,Long>
{

}
