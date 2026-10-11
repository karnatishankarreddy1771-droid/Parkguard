package com.anurag.parking.entity;
import com.anurag.parking.enums.SlotStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
@Entity
public class ParkingSlot {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @NotBlank @Size(max=40) @Column(unique=true) private String slotNumber;
    @NotBlank @Size(max=80) private String floor;
    @Enumerated(EnumType.STRING) private SlotStatus status = SlotStatus.AVAILABLE;
    @Size(max=120) private String assignedTo;
    @Size(max=24) private String vehicleNumber;
    public ParkingSlot(){}
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getSlotNumber(){return slotNumber;} public void setSlotNumber(String v){slotNumber=v;}
    public String getFloor(){return floor;} public void setFloor(String v){floor=v;}
    public SlotStatus getStatus(){return status;} public void setStatus(SlotStatus v){status=v;}
    public String getAssignedTo(){return assignedTo;} public void setAssignedTo(String v){assignedTo=v;}
    public String getVehicleNumber(){return vehicleNumber;} public void setVehicleNumber(String v){vehicleNumber=v;}
}
