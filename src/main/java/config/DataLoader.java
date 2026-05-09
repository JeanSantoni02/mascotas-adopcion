package com.adopcion.mascotas.config;

import com.adopcion.mascotas.model.Pet;
import com.adopcion.mascotas.model.User;
import com.adopcion.mascotas.repository.PetRepository;
import com.adopcion.mascotas.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataLoader implements CommandLineRunner {
    
    @Autowired
    private UserRepository userRepository;
    
    @Autowired
    private PetRepository petRepository;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Override
    public void run(String... args) throws Exception {
        // Crear usuarios de prueba
        if (userRepository.count() == 0) {
            User admin = new User("admin", passwordEncoder.encode("admin123"), "admin@adopcion.com", "ADMIN");
            User user1 = new User("usuario1", passwordEncoder.encode("user123"), "user1@adopcion.com", "USER");
            User user2 = new User("usuario2", passwordEncoder.encode("user123"), "user2@adopcion.com", "USER");
            
            userRepository.save(admin);
            userRepository.save(user1);
            userRepository.save(user2);
            
            System.out.println("✅ Usuarios creados:");
            System.out.println("   - admin / admin123 (ADMIN)");
            System.out.println("   - usuario1 / user123 (USER)");
            System.out.println("   - usuario2 / user123 (USER)");
        }
        
        // Crear mascotas de prueba
        if (petRepository.count() == 0) {
            Pet pet1 = new Pet("Max", "Perro", "Labrador", 3, "Macho", "Santiago", "Perro muy amigable y juguetón");
            Pet pet2 = new Pet("Luna", "Gato", "Siames", 2, "Hembra", "Valparaíso", "Gata cariñosa y tranquila");
            Pet pet3 = new Pet("Rocky", "Perro", "Pastor Alemán", 4, "Macho", "Concepción", "Perro inteligente y protector");
            Pet pet4 = new Pet("Mia", "Gato", "Persa", 1, "Hembra", "Santiago", "Gatita juguetona y adorable");
            Pet pet5 = new Pet("Bobby", "Perro", "Beagle", 2, "Macho", "La Serena", "Muy activo y curioso");
            
            petRepository.save(pet1);
            petRepository.save(pet2);
            petRepository.save(pet3);
            petRepository.save(pet4);
            petRepository.save(pet5);
            
            System.out.println("✅ Mascotas de prueba creadas (5)");
        }
    }
}