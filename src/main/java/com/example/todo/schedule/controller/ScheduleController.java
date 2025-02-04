package com.example.todo.schedule.controller;

import com.example.todo.schedule.dto.ScheduleRequestDto;
import com.example.todo.schedule.dto.ScheduleResponseDto;
import com.example.todo.schedule.service.ScheduleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/schedules")
public class ScheduleController {

    private ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(
            @RequestBody @Valid ScheduleRequestDto dto){
        return new ResponseEntity<>(scheduleService.saveSchedule(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findAllSchedules(
            @RequestParam(required = false) String updateAt
            , @RequestParam(required = false) String author
            , @RequestParam(defaultValue = "1") int page
            , @RequestParam(defaultValue = "10") int size){

        return new ResponseEntity<>(scheduleService.findAllSchedules(updateAt, author, page, size), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ScheduleResponseDto> optionalFindScheduleById(@PathVariable Long id){
        return new ResponseEntity<>(scheduleService.findScheduleByIdOrElseThrow(id), HttpStatus.OK);
    }

    @PatchMapping("{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(
            @PathVariable Long id
            , @RequestBody ScheduleRequestDto dto) {
        return new ResponseEntity<>(scheduleService.updateSchedule(id, dto.getTitle(), dto.getContents(), dto.getAuthorRequestDto().getName(), dto.getPassword()), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id, @RequestParam String password){
        scheduleService.isDeleted(id, password);
        scheduleService.deleteSchedule(id, password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}