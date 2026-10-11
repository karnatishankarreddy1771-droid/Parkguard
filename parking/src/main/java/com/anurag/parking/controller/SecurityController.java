package com.anurag.parking.controller;

import com.anurag.parking.entity.ParkingSlot;
import com.anurag.parking.entity.Visitor;
import com.anurag.parking.service.ParkingService;
import com.anurag.parking.repository.UserRepository;
import com.anurag.parking.enums.Role;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/security")
@PreAuthorize("hasRole('SECURITY')")
public class SecurityController {
    private final ParkingService service;
    private final UserRepository users;
    public SecurityController(ParkingService service, UserRepository users){this.service=service;this.users=users;}

    @GetMapping("/slots") public List<ParkingSlot> slots(){return service.slots();}
    @GetMapping("/slots/available") public List<ParkingSlot> available(){return service.available();}
    @GetMapping("/visitors") public List<Visitor> visitors(){return service.visitors();}
    @GetMapping("/visitors/expected") public List<Visitor> expected(){return service.expectedVisitors();}

    @GetMapping("/admin-contact")
    public Object adminContact(){
        var admin=users.findFirstByRole(Role.ADMIN);
        if(admin==null) return java.util.Map.of();
        return java.util.Map.of("name",admin.getName(),"email",admin.getEmail(),"phone",admin.getPhone()==null?"":admin.getPhone());
    }
}
