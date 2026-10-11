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
@RequestMapping("/api/resident")
@PreAuthorize("hasRole('RESIDENT')")
public class ResidentController {
    private final ParkingService service;
    private final UserRepository users;
    public ResidentController(ParkingService service, UserRepository users){this.service=service;this.users=users;}

    @GetMapping("/slots") public List<ParkingSlot> slots(){return service.slots();}
    @GetMapping("/slots/available") public List<ParkingSlot> available(){return service.available();}
    @GetMapping("/visitors") public List<Visitor> visitors(){return service.visitors();}

    @GetMapping("/security-contact")
    public Object securityContact(){
        var security=users.findFirstByRole(Role.SECURITY);
        if(security==null) return java.util.Map.of();
        return java.util.Map.of("name",security.getName(),"email",security.getEmail(),"phone",security.getPhone()==null?"":security.getPhone());
    }

    /*
     * Deliberately no POST/PUT/DELETE endpoints.
     * Visitor details must be recorded by Admin under the strict
     * "Admin-only write" security requirement.
     */
}
