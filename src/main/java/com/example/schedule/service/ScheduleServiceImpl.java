package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.repository.ScheduleRepository;
import org.springframework.stereotype.Service;
import java.util.List;

// todo exception 처리하기
@Service
public class ScheduleServiceImpl implements ScheduleService{

    private ScheduleRepository repository;

    public ScheduleServiceImpl(ScheduleRepository repository) {
        this.repository = repository;
    }

    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto dto) {
        Schedule schedule = new Schedule(dto.getAuthor(), dto.getTitle(), dto.getContents(), dto.getPassword());
        return repository.saveSchedule(schedule);
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules(String updateAt, String author) {
        return repository.findAllSchedules(updateAt, author);
    }

    @Override
    public ScheduleResponseDto findScheduleById(Long id) {
        Schedule schedule = repository.findScheduleById(id).orElse(null);
        return new ScheduleResponseDto(schedule);
    }

    @Override
    public int updateSchedule(Long id, String title, String contents, String author, String password) {
        return repository.updateSchedule(id, title, contents, author, password);
    }

    @Override
    public void deleteSchedule(Long id, String password) {
        repository.deleteSchedule(id, password);
    }
}
