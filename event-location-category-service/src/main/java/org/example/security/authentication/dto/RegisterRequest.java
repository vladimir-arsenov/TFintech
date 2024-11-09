package org.example.security.authentication.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {

  @NotBlank(message = "username cannot be blank")
  private String username;

  @NotBlank(message = "password cannot be blank")
  private String password;

  private boolean rememberMe;
}