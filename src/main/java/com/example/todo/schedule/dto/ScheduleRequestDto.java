package com.example.todo.schedule.dto;

import com.example.todo.author.dto.AuthorRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScheduleRequestDto {
    @NotBlank(message = "제목을 작성해주세요")
    private String title;

    @NotBlank(message = "할일을 작성해주세요")
    @Size(max = 200, message = "할일은 200자를 초과할 수 없습니다.")
    private String contents;

    @NotBlank(message = "비밀번호를 입력해주세요")
    private String password;

    @Valid  // AuthorRequestDto의 유효성 검사
    private AuthorRequestDto authorRequestDto;
}
