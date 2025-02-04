package com.example.todo.schedule.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Column;

@AllArgsConstructor
@Getter
public class Schedule {
    private Long id;
    private Long authorId;
    private String title;
    private String contents;
    private String password;
    private String name;
    private int isDeleted;

    public Schedule(Long authorId, String title, String contents, String password) {
        this.authorId = authorId;
        this.title = title;
        this.contents = contents;
        this.password = password;
    }

    public Schedule(Long authorId, String title, String contents, String password, String name) {
        this.authorId = authorId;
        this.title = title;
        this.contents = contents;
        this.password = password;
        this.name = name;
    }


}