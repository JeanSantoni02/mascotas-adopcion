package com.adopcion.mascotas.controller;

import com.adopcion.mascotas.model.Pet;
import com.adopcion.mascotas.service.PetService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetControllerTest {

    @Mock
    private PetService petService;

    @InjectMocks
    private PetController petController;

    private Pet pet1;
    private Pet pet2;

    @BeforeEach
    void setUp() {
        pet1 = new Pet();
        pet1.setId(1L);
        pet1.setName("Max");
        pet1.setSpecies("Perro");
        pet1.setBreed("Labrador");
        pet1.setAge(3);
        pet1.setGender("Macho");
        pet1.setLocation("Santiago");
        pet1.setStatus("available");

        pet2 = new Pet();
        pet2.setId(2L);
        pet2.setName("Luna");
        pet2.setSpecies("Gato");
        pet2.setBreed("Siames");
        pet2.setAge(2);
        pet2.setGender("Hembra");
        pet2.setLocation("Valparaíso");
        pet2.setStatus("available");
    }

    @Test
    void getAllAvailablePets_ShouldReturnListOfPets() {
        List<Pet> expectedPets = Arrays.asList(pet1, pet2);
        when(petService.findAvailablePets()).thenReturn(expectedPets);

        List<Pet> result = petController.getAllAvailablePets();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(pet1, pet2);
        verify(petService).findAvailablePets();
    }

    @Test
    void getPetById_WhenIdExists_ShouldReturnPet() {
        when(petService.findById(1L)).thenReturn(pet1);

        ResponseEntity<Pet> response = petController.getPetById(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Max");
        verify(petService).findById(1L);
    }

    @Test
    void getPetById_WhenIdDoesNotExist_ShouldReturnNotFound() {
        when(petService.findById(99L)).thenReturn(null);

        ResponseEntity<Pet> response = petController.getPetById(99L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
        verify(petService).findById(99L);
    }

    @Test
    void searchPets_ShouldReturnFilteredList() {
        List<Pet> expectedPets = Arrays.asList(pet1);
        when(petService.searchPets("Perro", null, null, null, null)).thenReturn(expectedPets);

        List<Pet> result = petController.searchPets("Perro", null, null, null, null);

        assertThat(result).hasSize(1);
        verify(petService).searchPets("Perro", null, null, null, null);
    }

    @Test
    void createPet_ShouldReturnCreatedPet() {
        when(petService.save(any(Pet.class))).thenReturn(pet1);

        Pet result = petController.createPet(pet1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(petService).save(pet1);
    }

    @Test
    void updatePet_WhenIdExists_ShouldReturnUpdatedPet() {
        when(petService.update(eq(1L), any(Pet.class))).thenReturn(pet1);

        ResponseEntity<Pet> response = petController.updatePet(1L, pet1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        verify(petService).update(eq(1L), any(Pet.class));
    }

    @Test
    void updatePet_WhenIdDoesNotExist_ShouldReturnNotFound() {
        when(petService.update(eq(99L), any(Pet.class))).thenReturn(null);

        ResponseEntity<Pet> response = petController.updatePet(99L, pet1);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNull();
        verify(petService).update(eq(99L), any(Pet.class));
    }

    @Test
    void deletePet_ShouldReturnOk() {
        doNothing().when(petService).delete(1L);

        ResponseEntity<Void> response = petController.deletePet(1L);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        verify(petService).delete(1L);
    }
}