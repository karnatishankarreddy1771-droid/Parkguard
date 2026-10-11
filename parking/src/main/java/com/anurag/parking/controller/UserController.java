package com.anurag.parking.controller;

import com.anurag.parking.dto.RegisterDTO;
import com.anurag.parking.dto.UserUpdateDTO;
import com.anurag.parking.entity.User;
import com.anurag.parking.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class UserController {
    private final UserService service;
    public UserController(UserService s){service=s;}

    @GetMapping public List<User> all(){return service.all();}

    @PostMapping public User register(@jakarta.validation.Valid @RequestBody RegisterDTO d){return service.register(d);}

    @PutMapping("/{id}") public User update(@PathVariable Long id,@jakarta.validation.Valid @RequestBody UserUpdateDTO d){return service.update(id,d);}

    @DeleteMapping("/{id}") public String delete(@PathVariable Long id){return service.delete(id);}

    @PostMapping("/{id}/access-token")
    public Object issueAccessToken(@PathVariable Long id){return service.issueAccessToken(id).getBody();}
}
