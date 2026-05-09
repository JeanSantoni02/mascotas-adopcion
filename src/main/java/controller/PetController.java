package com.adopcion.mascotas.controller;

import com.adopcion.mascotas.model.Pet;
import com.adopcion.mascotas.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/pets")
@CrossOrigin(origins = "*")
public class PetController {
    
    @Autowired
    private PetService petService;
    
    // ========== ENDPOINTS PÚBLICOS ==========
    
    @GetMapping("/public/all")
    public List<Pet> getAllAvailablePets() {
        return petService.findAvailablePets();
    }
    
    @GetMapping("/public/search")
    public List<Pet> searchPets(
            @RequestParam(required = false) String species,
            @RequestParam(required = false) String breed,
            @RequestParam(required = false) Integer age,
            @RequestParam(required = false) String gender,
            @RequestParam(required = false) String location) {
        return petService.searchPets(species, breed, age, gender, location);
    }
    
    @GetMapping("/public/{id}")
    public ResponseEntity<Pet> getPetById(@PathVariable Long id) {
        Pet pet = petService.findById(id);
        return pet != null ? ResponseEntity.ok(pet) : ResponseEntity.notFound().build();
    }
    
    // ========== ENDPOINTS PRIVADOS (SOLO ADMIN) ==========
    
    @PostMapping("/private/create")
    @PreAuthorize("hasRole('ADMIN')")
    public Pet createPet(@RequestBody Pet pet) {
        return petService.save(pet);
    }
    
    @PutMapping("/private/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Pet> updatePet(@PathVariable Long id, @RequestBody Pet pet) {
        Pet updatedPet = petService.update(id, pet);
        return updatedPet != null ? ResponseEntity.ok(updatedPet) : ResponseEntity.notFound().build();
    }
    
    @DeleteMapping("/private/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deletePet(@PathVariable Long id) {
        petService.delete(id);
        return ResponseEntity.ok().build();
    }
}