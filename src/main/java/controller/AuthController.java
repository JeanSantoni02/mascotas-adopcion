package com.adopcion.mascotas.controller;

import com.adopcion.mascotas.config.JwtUtil;
import com.adopcion.mascotas.model.User;
import com.adopcion.mascotas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> login(@RequestBody Map<String, String> loginRequest) {
        String username = loginRequest.get("username");
        String password = loginRequest.get("password");
        
        User user = userRepository.findByUsername(username).orElse(null);
        Map<String, String> response = new HashMap<>();
        
        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            String token = jwtUtil.generateToken(user.getUsername());
            response.put("token", token);
            response.put("username", user.getUsername());
            response.put("role", user.getRole());
            response.put("message", "Login exitoso");
            return ResponseEntity.ok(response);
        } else {
            response.put("error", "Credenciales inválidas");
            return ResponseEntity.status(401).body(response);
        }
    }
}
