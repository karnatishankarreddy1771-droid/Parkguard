package com.anurag.parking.repository;
import com.anurag.parking.entity.User;
import com.anurag.parking.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User,Long>{
    User findByEmail(String email);
    User findFirstByRole(Role role);
}
