package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.UserDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/v1/auth")
public class AuthController {

    @PostMapping("/user/register")
    public void registerUserAccount(@RequestBody @Valid UserDto userDto) {


    }
}
