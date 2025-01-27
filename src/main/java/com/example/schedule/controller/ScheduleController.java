package com.example.schedule.controller;

import com.example.schedule.dto.ScheduleRequestDto;
import com.example.schedule.dto.ScheduleResponseDto;
import com.example.schedule.service.ScheduleService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedules")
public class ScheduleController {

    private ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    @PostMapping
    public ResponseEntity<ScheduleResponseDto> createSchedule(@RequestBody ScheduleRequestDto dto){
        return new ResponseEntity<>(scheduleService.saveSchedule(dto), HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<ScheduleResponseDto>> findAllSchedules(
            @RequestParam(required = false) String updateAt
            , @RequestParam(required = false) String author){

        return new ResponseEntity<>(scheduleService.findAllSchedules(updateAt, author), HttpStatus.OK);
    }

    @GetMapping("{id}")
    public ResponseEntity<ScheduleResponseDto> optionalFindScheduleById(@PathVariable Long id){
        return new ResponseEntity<>(scheduleService.findScheduleByIdOrElseThrow(id), HttpStatus.OK);
    }

    @PatchMapping("{id}")
    public ResponseEntity<ScheduleResponseDto> updateSchedule(@PathVariable Long id, @RequestBody ScheduleRequestDto dto) {
        return new ResponseEntity<>(scheduleService.updateSchedule(id, dto.getTitle(), dto.getContents(), dto.getAuthor(), dto.getPassword()), HttpStatus.OK);
    }

    @PutMapping("{id}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable Long id, @RequestParam String password){
        scheduleService.deleteSchedule(id, password);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
