package com.adopcion.mascotas.repository;

import com.adopcion.mascotas.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByStatus(String status);
    
    @Query("SELECT p FROM Pet p WHERE " +
           "(:species IS NULL OR p.species = :species) AND " +
           "(:breed IS NULL OR p.breed = :breed) AND " +
           "(:age IS NULL OR p.age = :age) AND " +
           "(:gender IS NULL OR p.gender = :gender) AND " +
           "(:location IS NULL OR p.location LIKE %:location%) AND " +
           "p.status = 'available'")
    List<Pet> searchPets(@Param("species") String species,
                        @Param("breed") String breed,
                        @Param("age") Integer age,
                        @Param("gender") String gender,
                        @Param("location") String location);
}