package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import lombok.Data;

import java.util.Date;
import java.util.List;

public interface ScheduleService {
    ScheduleResponseDto saveSchedule(ScheduleRequestDto dto);
    List<ScheduleResponseDto> findAllSchedules(String updateAt, String author);
    ScheduleResponseDto findScheduleByIdOrElseThrow(Long id);
    ScheduleResponseDto updateSchedule(Long id, String title, String contents, String author, String password);
    void deleteSchedule(Long id, String password);
    void validPassword(Long id, String password);
}
