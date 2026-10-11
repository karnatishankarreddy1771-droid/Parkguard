package com.anurag.parking.controller;

import com.anurag.parking.entity.*;
import com.anurag.parking.service.ParkingService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/admin/parking")
@PreAuthorize("hasRole('ADMIN')")
public class ParkingController {
    private final ParkingService service;
    public ParkingController(ParkingService s){service=s;}

    @GetMapping("/slots") public List<ParkingSlot> slots(){return service.slots();}
    @GetMapping("/slots/available") public List<ParkingSlot> available(){return service.available();}
    @PostMapping("/slots") public ParkingSlot saveSlot(@jakarta.validation.Valid @RequestBody ParkingSlot s){return service.saveSlot(s);}
    @PutMapping("/slots/{id}") public ParkingSlot updateSlot(@PathVariable Long id,@jakarta.validation.Valid @RequestBody ParkingSlot s){return service.updateSlot(id,s);}
    @DeleteMapping("/slots/{id}") public String deleteSlot(@PathVariable Long id){return service.deleteSlot(id);}

    @GetMapping("/vehicles") public List<Vehicle> vehicles(){return service.vehicles();}
    @PostMapping("/vehicles") public Vehicle saveVehicle(@jakarta.validation.Valid @RequestBody Vehicle v){return service.saveVehicle(v);}
    @PutMapping("/vehicles/{id}") public Vehicle updateVehicle(@PathVariable Long id,@jakarta.validation.Valid @RequestBody Vehicle v){return service.updateVehicle(id,v);}
    @DeleteMapping("/vehicles/{id}") public String deleteVehicle(@PathVariable Long id){return service.deleteVehicle(id);}

    @GetMapping("/visitors") public List<Visitor> visitors(){return service.visitors();}
    @PostMapping("/visitors") public Visitor saveVisitor(@jakarta.validation.Valid @RequestBody Visitor v){return service.saveVisitor(v);}
    @PutMapping("/visitors/{id}/entry") public Visitor entry(@PathVariable Long id){return service.entry(id);}
    @PutMapping("/visitors/{id}/exit") public Visitor exit(@PathVariable Long id){return service.exit(id);}
    @DeleteMapping("/visitors/{id}") public String deleteVisitor(@PathVariable Long id){return service.deleteVisitor(id);}
}
