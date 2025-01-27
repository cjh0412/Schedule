package com.example.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class Schedule {
    private Long id;
    private String author;
    private String title;
    private String contents;
    private String password;

    public Schedule(String author, String title, String contents, String password) {
        this.author = author;
        this.title = title;
        this.contents = contents;
        this.password = password;
    }


}