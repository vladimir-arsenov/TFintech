package org.example.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UserDto {
    @NotNull
    @NotEmpty
    private String login;
    
    @NotNull
    @NotEmpty
    private String password;
    
    @NotNull
    @NotEmpty
    private String email;
}