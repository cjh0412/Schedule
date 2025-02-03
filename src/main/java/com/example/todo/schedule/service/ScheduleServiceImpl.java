package com.example.todo.schedule.service;

import com.example.todo.author.service.AuthorService;
import com.example.todo.schedule.dto.ScheduleRequestDto;
import com.example.todo.schedule.dto.ScheduleResponseDto;
import com.example.todo.author.entity.Author;
import com.example.todo.schedule.entity.Schedule;
import com.example.todo.exception.ErrorCode;
import com.example.todo.exception.ScheduleException;
import com.example.todo.schedule.repository.ScheduleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ScheduleServiceImpl implements ScheduleService{

    private final ScheduleRepository repository;
    private final AuthorService authorService;

    public ScheduleServiceImpl(ScheduleRepository repository, AuthorService authorService) {
        this.repository = repository;
        this.authorService = authorService;
    }

    @Override
    public ScheduleResponseDto saveSchedule(ScheduleRequestDto sDto) {
        Optional<Author> existingAuthor = authorService.findByAuthorId(
                sDto.getAuthorRequestDto().getName(),
                sDto.getAuthorRequestDto().getEmail());

        Long authorId = existingAuthor.map(Author::getId)
                .orElseGet(() -> authorService.createAuthor(
                        sDto.getAuthorRequestDto().getName(),
                        sDto.getAuthorRequestDto().getEmail()).getId());

        Schedule schedule = new Schedule(authorId, sDto.getTitle(), sDto.getContents(), sDto.getPassword());
        return repository.saveSchedule(schedule);
    }

    @Override
    public List<ScheduleResponseDto> findAllSchedules(String updateAt, String author, int page, int size) {
        return repository.findAllSchedules(updateAt, author, page, size);
    }

    @Override
    public ScheduleResponseDto findScheduleByIdOrElseThrow(Long id) {

        Schedule schedule = repository.findScheduleByIdOrElseThrow(id);
        return new ScheduleResponseDto(schedule);
    }

    @Override
    public ScheduleResponseDto updateSchedule(Long id, String title, String contents, String author, String password) {
        int updateResult = 0;
        validPassword(id, password);

        if(author != null){
            updateResult = authorService.updateAuthor(id, author);
        }

        if(title != null && contents != null){
            updateResult = repository.updateSchedule(id, title, contents, password);
        }

        if(updateResult == 0){
            throw new ScheduleException(ErrorCode.NOT_FOUNT_DATA);
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
            throw new ScheduleException(ErrorCode.PASSWORD_CHECK_FAIL);
        }
    }

    @Override
    public void isDeleted(Long id, String password) {
        if(!repository.validPassword(id, password)){
            throw new ScheduleException(ErrorCode.ALREADY_DELETE_DATE);
        }
    }

}
