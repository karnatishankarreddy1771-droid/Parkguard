package com.anurag.parking.repository;
import com.anurag.parking.entity.ParkingSlot;
import com.anurag.parking.enums.SlotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ParkingSlotRepository extends JpaRepository<ParkingSlot,Long>{
    List<ParkingSlot> findByStatus(SlotStatus status);
}
