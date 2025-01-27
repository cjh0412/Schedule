package com.example.schedule.repository;

import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import lombok.Data;

import javax.swing.*;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface ScheduleRepository {

    ScheduleResponseDto saveSchedule(Schedule schedule);
    List<ScheduleResponseDto> findAllSchedules(String updateAt, String author);
    Schedule findScheduleByIdOrElseThrow(Long id);
    int updateSchedule(Long id, String title, String contents, String author, String password);
    void deleteSchedule(Long id, String password);
    boolean validPassword(Long id, String password);
}
