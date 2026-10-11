package com.anurag.parking.repository;

import com.anurag.parking.entity.Visitor;
import com.anurag.parking.enums.VisitorStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface VisitorRepository extends JpaRepository<Visitor,Long>{
    List<Visitor> findByStatus(VisitorStatus status);
}
