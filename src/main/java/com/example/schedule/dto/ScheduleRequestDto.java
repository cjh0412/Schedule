package com.example.schedule.dto;

import lombok.Getter;

import java.util.Date;

@Getter
public class ScheduleRequestDto {
    private String author;
    private String title;
    private String contents;
    private String password;


}
