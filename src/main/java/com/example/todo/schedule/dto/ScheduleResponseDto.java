package com.example.todo.schedule.dto;

import com.example.todo.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {
    private Long id;
    private Long authorId;
    private String title;
    private String contents;
    private String name;

    // 요구사항 명시, 보안을 위해 비밀번호 반환 제외
    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.authorId = schedule.getAuthorId();
        this.title = schedule.getTitle();
        this.contents = schedule.getContents();
        this.name = schedule.getName();
    }

}
