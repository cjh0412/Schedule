package com.example.schedule.service;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.entity.Schedule;
import com.example.schedule.repository.ScheduleRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

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
    public ScheduleResponseDto findScheduleByIdOrElseThrow(Long id) {
        Schedule schedule = repository.findScheduleByIdOrElseThrow(id);
        return new ScheduleResponseDto(schedule);
    }

    @Override
    public ScheduleResponseDto updateSchedule(Long id, String title, String contents, String author, String password) {

        if(title == null ||  contents == null ||  author == null ||  password == null ){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 요청값이 존재하지 않습니다.");
        }

        validPassword(id, password);

        int updateResult = repository.updateSchedule(id, title, contents, author, password);

        if(updateResult == 0){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "수정된 값이 존재하지 않습니다.");
        }

        // 수정된 값으로 재조회
        Schedule updatedSchedule  = repository.findScheduleByIdOrElseThrow(id);
        return new ScheduleResponseDto(updatedSchedule);
    }

    @Override
    public void deleteSchedule(Long id, String password) {
        validPassword(id, password);
        repository.deleteSchedule(id, password);
    }

    @Override
    public void validPassword(Long id, String password) {
        if(!repository.validPassword(id, password)){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "비밀번호가 일치하지 않습니다.");
        }
    }

}
