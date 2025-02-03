package com.example.todo.schedule.service;

import com.example.todo.schedule.dto.ScheduleRequestDto;
import com.example.todo.schedule.dto.ScheduleResponseDto;

import java.util.List;

public interface ScheduleService {
    ScheduleResponseDto saveSchedule(ScheduleRequestDto scheduleRequestDto);
    List<ScheduleResponseDto> findAllSchedules(String updateAt, String author, int page, int size);
    ScheduleResponseDto findScheduleByIdOrElseThrow(Long id);
    ScheduleResponseDto updateSchedule(Long id, String title, String contents, String author, String password);
    void deleteSchedule(Long id, String password);
    void validPassword(Long id, String password);
    void isDeleted(Long id, String password);
}
