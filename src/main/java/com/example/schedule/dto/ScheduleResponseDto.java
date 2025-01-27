package com.example.schedule.dto;

import com.example.schedule.entity.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ScheduleResponseDto {
    private Long id;
    private String author;
    private String title;
    private String contents;

    // 요구사항 명시, 보안을 위해 비밀번호 반환 제외
    public ScheduleResponseDto(Schedule schedule) {
        this.id = schedule.getId();
        this.author = schedule.getAuthor();
        this.title = schedule.getTitle();
        this.contents = schedule.getContents();
    }

}
