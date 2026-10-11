package com.anurag.parking.service;

import com.anurag.parking.dto.*;
import com.anurag.parking.entity.User;
import com.anurag.parking.enums.Role;
import com.anurag.parking.repository.UserRepository;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {
    private final UserRepository repo;
    private final JwtService jwt;
    private final PasswordEncoder encoder;
    private final Map<String, LoginAttempts> loginAttempts = new ConcurrentHashMap<>();
    private static final int MAX_LOGIN_ATTEMPTS = 10;
    private static final long LOGIN_WINDOW_MS = 15 * 60 * 1000L;

    private static final class LoginAttempts { int count; long windowStart; }

    public UserService(UserRepository repo, JwtService jwt, PasswordEncoder encoder){
        this.repo=repo; this.jwt=jwt; this.encoder=encoder;
    }

    public List<User> all(){ return repo.findAll(); }

    /** Registration is intentionally NOT public. Admin-only controller calls this method. */
    public User register(RegisterDTO d){
        String normalizedEmail = d.getEmail().trim().toLowerCase(Locale.ROOT);
        if(repo.findByEmail(normalizedEmail)!=null)
            throw new IllegalArgumentException("Email already registered");

        Role role = parseRole(d.getRole());
        User u=new User();
        u.setName(d.getName());
        u.setEmail(normalizedEmail);
        u.setPhone(d.getPhone());
        u.setFlatNumber(d.getFlatNumber());
        u.setPassword(encoder.encode(d.getPassword()));
        u.setRole(role);
        return repo.save(u);
    }

    /** Only Admin login is exposed by the public web login. */
    public ResponseEntity<?> adminLogin(LoginDTO d, String clientIp){
        return authenticate(d, clientIp, true);
    }

    /** Role-aware login for Admin, Resident, and Security dashboards. */
    public ResponseEntity<?> login(LoginDTO d, String clientIp){
        return authenticate(d, clientIp, false);
    }

    private ResponseEntity<?> authenticate(LoginDTO d, String clientIp, boolean adminOnly){
        long now = System.currentTimeMillis();
        if (!loginAttempts.containsKey(clientIp) && loginAttempts.size() >= 10_000) {
            loginAttempts.entrySet().removeIf(entry -> now - entry.getValue().windowStart >= LOGIN_WINDOW_MS);
            if (loginAttempts.size() >= 10_000)
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Login service is busy. Try again later.");
        }
        LoginAttempts attempts = loginAttempts.compute(clientIp, (ip, current) -> {
            if (current == null || now - current.windowStart >= LOGIN_WINDOW_MS) {
                current = new LoginAttempts(); current.windowStart = now;
            }
            return current;
        });
        synchronized (attempts) {
            if (attempts.count >= MAX_LOGIN_ATTEMPTS)
                return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body("Too many login attempts. Try again later.");
            User u=repo.findByEmail(d.getEmail().trim().toLowerCase(Locale.ROOT));
            if(u==null || (adminOnly && u.getRole()!=Role.ADMIN) || !encoder.matches(d.getPassword(),u.getPassword())) {
                attempts.count++;
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
            }
            loginAttempts.remove(clientIp, attempts);
            return ResponseEntity.ok(new LoginResponseDTO(jwt.generateToken(u.getEmail()),u.getId(),u.getRole().name(),u.getEmail()));
        }
    }

    /** Used by Admin to issue a role-scoped access token for Security/Resident API clients. */
    public ResponseEntity<?> issueAccessToken(Long id){
        User u=repo.findById(id).orElse(null);
        if(u==null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(new LoginResponseDTO(jwt.generateToken(u.getEmail()),u.getId(),u.getRole().name(),u.getEmail()));
    }

    public User update(Long id,UserUpdateDTO data){
        User u=repo.findById(id).orElse(null);
        if(u==null) return null;
        if(data.getName()!=null) u.setName(data.getName());
        if(data.getPhone()!=null) u.setPhone(data.getPhone());
        if(data.getFlatNumber()!=null) u.setFlatNumber(data.getFlatNumber());
        if(data.getRole()!=null) u.setRole(data.getRole());
        if(data.getPassword()!=null && !data.getPassword().isBlank()) u.setPassword(encoder.encode(data.getPassword()));
        return repo.save(u);
    }

    public String delete(Long id){
        if(repo.existsById(id)){repo.deleteById(id);return "User deleted successfully";}
        return "User not found";
    }

    public User find(Long id){ return repo.findById(id).orElse(null); }

    private Role parseRole(String raw){
        if(raw==null || raw.isBlank()) return Role.RESIDENT;
        try { return Role.valueOf(raw.trim().toUpperCase()); }
        catch(Exception e){ throw new IllegalArgumentException("Role must be ADMIN, RESIDENT or SECURITY"); }
    }
}
