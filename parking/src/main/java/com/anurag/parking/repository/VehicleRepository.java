package com.anurag.parking.repository;
import com.anurag.parking.entity.Vehicle;
import org.springframework.data.jpa.repository.JpaRepository;
public interface VehicleRepository extends JpaRepository<Vehicle,Long>{}
