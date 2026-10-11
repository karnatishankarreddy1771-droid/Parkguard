package com.anurag.parking.entity;
import com.anurag.parking.enums.VehicleType;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
@Entity
public class Vehicle {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @NotBlank @Size(max=120) private String ownerName;
    @Email @Size(max=254) private String ownerEmail;
    @NotBlank @Size(max=24) @Column(unique=true) private String vehicleNumber;
    @Size(max=80) private String model;
    @Enumerated(EnumType.STRING) private VehicleType type;
    public Vehicle(){}
    public Long getId(){return id;} public void setId(Long id){this.id=id;}
    public String getOwnerName(){return ownerName;} public void setOwnerName(String v){ownerName=v;}
    public String getOwnerEmail(){return ownerEmail;} public void setOwnerEmail(String v){ownerEmail=v;}
    public String getVehicleNumber(){return vehicleNumber;} public void setVehicleNumber(String v){vehicleNumber=v;}
    public String getModel(){return model;} public void setModel(String v){model=v;}
    public VehicleType getType(){return type;} public void setType(VehicleType v){type=v;}
}
