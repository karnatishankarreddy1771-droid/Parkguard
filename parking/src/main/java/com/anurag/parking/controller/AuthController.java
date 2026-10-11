package com.anurag.parking.controller;

import com.anurag.parking.dto.LoginDTO;
import com.anurag.parking.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserService service;
    public AuthController(UserService service){this.service=service;}

    @PostMapping("/admin/login")
    public ResponseEntity<?> adminLogin(@Valid @RequestBody LoginDTO dto, HttpServletRequest request){
        return service.adminLogin(dto, request.getRemoteAddr());
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDTO dto, HttpServletRequest request){
        return service.login(dto, request.getRemoteAddr());
    }
}
