package com.adopcion.mascotas.service;

import com.adopcion.mascotas.model.Pet;
import com.adopcion.mascotas.repository.PetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PetService {
    
    @Autowired
    private PetRepository petRepository;
    
    public List<Pet> findAll() {
        return petRepository.findAll();
    }
    
    public List<Pet> findAvailablePets() {
        return petRepository.findByStatus("available");
    }
    
    public List<Pet> searchPets(String species, String breed, Integer age, 
                                String gender, String location) {
        return petRepository.searchPets(species, breed, age, gender, location);
    }
    
    public Pet findById(Long id) {
        return petRepository.findById(id).orElse(null);
    }
    
    public Pet save(Pet pet) {
        return petRepository.save(pet);
    }
    
    public Pet update(Long id, Pet petDetails) {
        Pet pet = findById(id);
        if (pet != null) {
            pet.setName(petDetails.getName());
            pet.setSpecies(petDetails.getSpecies());
            pet.setBreed(petDetails.getBreed());
            pet.setAge(petDetails.getAge());
            pet.setGender(petDetails.getGender());
            pet.setLocation(petDetails.getLocation());
            pet.setDescription(petDetails.getDescription());
            pet.setPhotoUrl(petDetails.getPhotoUrl());
            pet.setStatus(petDetails.getStatus());
            return petRepository.save(pet);
        }
        return null;
    }
    
    public void delete(Long id) {
        petRepository.deleteById(id);
    }
}