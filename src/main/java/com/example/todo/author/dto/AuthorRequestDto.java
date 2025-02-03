package com.example.todo.author.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AuthorRequestDto {
    
    @NotBlank(message = "작성자 명을 입력해주세요")
    private String name;

    @NotBlank(message = "이메일 주소를 입력해주세요")
    @Email(message = "이메일 형식을 맞춰주세요")
    private String email;
}
