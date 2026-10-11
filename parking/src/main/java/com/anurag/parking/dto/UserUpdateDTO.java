package com.anurag.parking.dto;

import com.anurag.parking.enums.Role;
import jakarta.validation.constraints.Size;

public class UserUpdateDTO {
    @Size(max = 120)
    private String name;

    @Size(max = 32)
    private String phone;

    @Size(max = 40)
    private String flatNumber;

    @Size(min = 12, max = 128)
    private String password;

    private Role role;

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }
    public String getFlatNumber() { return flatNumber; }
    public void setFlatNumber(String flatNumber) { this.flatNumber = flatNumber; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public Role getRole() { return role; }
    public void setRole(Role role) { this.role = role; }
}
