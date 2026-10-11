package com.anurag.parking.entity;
import com.anurag.parking.enums.VisitorStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;
@Entity
public class Visitor {
    @Id @GeneratedValue(strategy=GenerationType.IDENTITY) private Long id;
    @NotBlank @Size(max=120) private String visitorName;
    @Size(max=32) private String phone;
    @Size(max=24) private String vehicleNumber;
    @NotBlank @Size(max=120) private String hostName;
    @NotBlank @Size(max=300) private String purpose;
    private LocalDateTime entryTime;
    private LocalDateTime exitTime;
    @Enumerated(EnumType.STRING) private VisitorStatus status = VisitorStatus.EXPECTED;
    public Visitor(){}
    public Long getId(){return id;} public void setId(Long v){id=v;}
    public String getVisitorName(){return visitorName;} public void setVisitorName(String v){visitorName=v;}
    public String getPhone(){return phone;} public void setPhone(String v){phone=v;}
    public String getVehicleNumber(){return vehicleNumber;} public void setVehicleNumber(String v){vehicleNumber=v;}
    public String getHostName(){return hostName;} public void setHostName(String v){hostName=v;}
    public String getPurpose(){return purpose;} public void setPurpose(String v){purpose=v;}
    public LocalDateTime getEntryTime(){return entryTime;} public void setEntryTime(LocalDateTime v){entryTime=v;}
    public LocalDateTime getExitTime(){return exitTime;} public void setExitTime(LocalDateTime v){exitTime=v;}
    public VisitorStatus getStatus(){return status;} public void setStatus(VisitorStatus v){status=v;}
}
