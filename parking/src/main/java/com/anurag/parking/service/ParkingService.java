package com.anurag.parking.service;

import com.anurag.parking.entity.*;
import com.anurag.parking.enums.*;
import com.anurag.parking.repository.*;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ParkingService {
    private final ParkingSlotRepository slots;
    private final VehicleRepository vehicles;
    private final VisitorRepository visitors;

    public ParkingService(ParkingSlotRepository s,VehicleRepository v,VisitorRepository x){
        slots=s;vehicles=v;visitors=x;
    }

    public List<ParkingSlot> slots(){return slots.findAll();}
    public List<ParkingSlot> available(){return slots.findByStatus(SlotStatus.AVAILABLE);}

    public ParkingSlot saveSlot(ParkingSlot s){
        if(s.getStatus()==null)s.setStatus(SlotStatus.AVAILABLE);
        return slots.save(s);
    }

    public ParkingSlot updateSlot(Long id,ParkingSlot d){
        ParkingSlot s=slots.findById(id).orElse(null);
        if(s==null)return null;
        s.setSlotNumber(d.getSlotNumber());
        s.setFloor(d.getFloor());
        if(d.getStatus()!=null)s.setStatus(d.getStatus());
        s.setAssignedTo(d.getAssignedTo());
        s.setVehicleNumber(d.getVehicleNumber());
        return slots.save(s);
    }

    public String deleteSlot(Long id){
        if(slots.existsById(id)){slots.deleteById(id);return "Parking slot deleted";}
        return "Slot not found";
    }

    public List<Vehicle> vehicles(){return vehicles.findAll();}
    public Vehicle saveVehicle(Vehicle v){return vehicles.save(v);}

    public Vehicle updateVehicle(Long id,Vehicle d){
        Vehicle v=vehicles.findById(id).orElse(null);
        if(v==null)return null;
        v.setOwnerName(d.getOwnerName());
        v.setOwnerEmail(d.getOwnerEmail());
        v.setVehicleNumber(d.getVehicleNumber());
        v.setModel(d.getModel());
        v.setType(d.getType());
        return vehicles.save(v);
    }

    public String deleteVehicle(Long id){
        if(vehicles.existsById(id)){vehicles.deleteById(id);return "Vehicle deleted";}
        return "Vehicle not found";
    }

    public List<Visitor> visitors(){return visitors.findAll();}
    public List<Visitor> expectedVisitors(){return visitors.findByStatus(VisitorStatus.EXPECTED);}

    public Visitor saveVisitor(Visitor v){
        if(v.getStatus()==null)v.setStatus(VisitorStatus.EXPECTED);
        return visitors.save(v);
    }

    public Visitor entry(Long id){
        Visitor v=visitors.findById(id).orElse(null);
        if(v!=null){v.setStatus(VisitorStatus.ENTERED);v.setEntryTime(LocalDateTime.now());visitors.save(v);}
        return v;
    }

    public Visitor exit(Long id){
        Visitor v=visitors.findById(id).orElse(null);
        if(v!=null){v.setStatus(VisitorStatus.EXITED);v.setExitTime(LocalDateTime.now());visitors.save(v);}
        return v;
    }

    public String deleteVisitor(Long id){
        if(visitors.existsById(id)){visitors.deleteById(id);return "Visitor deleted";}
        return "Visitor not found";
    }
}
