package com.example.todo.schedule.repository;

import com.example.todo.schedule.dto.ScheduleResponseDto;
import com.example.todo.schedule.entity.Schedule;

import java.util.List;

public interface ScheduleRepository {

    ScheduleResponseDto saveSchedule(Schedule schedule);
    List<ScheduleResponseDto> findAllSchedules(String updateAt, String author, int page, int size);
    Schedule findScheduleByIdOrElseThrow(Long id);
    int updateSchedule(Long id, String title, String contents,  String password);
    void deleteSchedule(Long id, String password);
    boolean validPassword(Long id, String password);
    int isDeleted(Long id, String password);
}
