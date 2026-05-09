package com.adopcion.mascotas.service;

import com.adopcion.mascotas.model.Pet;
import com.adopcion.mascotas.repository.PetRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {

    @Mock
    private PetRepository petRepository;

    @InjectMocks
    private PetService petService;

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
        pet1.setDescription("Perro amigable");

        pet2 = new Pet();
        pet2.setId(2L);
        pet2.setName("Luna");
        pet2.setSpecies("Gato");
        pet2.setBreed("Siames");
        pet2.setAge(2);
        pet2.setGender("Hembra");
        pet2.setLocation("Valparaíso");
        pet2.setStatus("available");
        pet2.setDescription("Gata cariñosa");
    }

    @Test
    void findAll_ShouldReturnAllPets() {
        List<Pet> expectedPets = Arrays.asList(pet1, pet2);
        when(petRepository.findAll()).thenReturn(expectedPets);

        List<Pet> result = petService.findAll();

        assertThat(result).hasSize(2);
        assertThat(result).containsExactly(pet1, pet2);
        verify(petRepository).findAll();
    }

    @Test
    void findAll_WhenNoPets_ShouldReturnEmptyList() {
        when(petRepository.findAll()).thenReturn(Arrays.asList());

        List<Pet> result = petService.findAll();

        assertThat(result).isEmpty();
        verify(petRepository).findAll();
    }

    @Test
    void findAvailablePets_ShouldReturnOnlyAvailablePets() {
        List<Pet> expectedPets = Arrays.asList(pet1, pet2);
        when(petRepository.findByStatus("available")).thenReturn(expectedPets);

        List<Pet> result = petService.findAvailablePets();

        assertThat(result).hasSize(2);
        verify(petRepository).findByStatus("available");
    }

    @Test
    void findAvailablePets_WhenNoAvailablePets_ShouldReturnEmptyList() {
        when(petRepository.findByStatus("available")).thenReturn(Arrays.asList());

        List<Pet> result = petService.findAvailablePets();

        assertThat(result).isEmpty();
        verify(petRepository).findByStatus("available");
    }

    @Test
    void findById_WhenIdExists_ShouldReturnPet() {
        when(petRepository.findById(1L)).thenReturn(Optional.of(pet1));

        Pet result = petService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getName()).isEqualTo("Max");
        verify(petRepository).findById(1L);
    }

    @Test
    void findById_WhenIdDoesNotExist_ShouldReturnNull() {
        when(petRepository.findById(99L)).thenReturn(Optional.empty());

        Pet result = petService.findById(99L);

        assertThat(result).isNull();
        verify(petRepository).findById(99L);
    }

    @Test
    void save_ShouldReturnSavedPet() {
        when(petRepository.save(any(Pet.class))).thenReturn(pet1);

        Pet result = petService.save(pet1);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        verify(petRepository).save(pet1);
    }

    @Test
    void update_WhenIdExists_ShouldUpdateAndReturnPet() {
        Pet updatedDetails = new Pet();
        updatedDetails.setName("Max Actualizado");
        updatedDetails.setSpecies("Perro");
        updatedDetails.setBreed("Labrador");
        updatedDetails.setAge(4);
        updatedDetails.setGender("Macho");
        updatedDetails.setLocation("Santiago Centro");
        updatedDetails.setDescription("Perro muy amigable");
        updatedDetails.setStatus("adopted");

        when(petRepository.findById(1L)).thenReturn(Optional.of(pet1));
        when(petRepository.save(any(Pet.class))).thenReturn(pet1);

        Pet result = petService.update(1L, updatedDetails);

        assertThat(result).isNotNull();
        verify(petRepository).findById(1L);
        verify(petRepository).save(any(Pet.class));
    }

    @Test
    void update_WhenIdDoesNotExist_ShouldReturnNull() {
        Pet updatedDetails = new Pet();
        when(petRepository.findById(99L)).thenReturn(Optional.empty());

        Pet result = petService.update(99L, updatedDetails);

        assertThat(result).isNull();
        verify(petRepository).findById(99L);
    }

    @Test
    void delete_ShouldCallRepositoryDelete() {
        doNothing().when(petRepository).deleteById(1L);

        petService.delete(1L);

        verify(petRepository).deleteById(1L);
    }

    @Test
    void searchPets_ShouldReturnFilteredPets() {
        List<Pet> expectedPets = Arrays.asList(pet1);
        when(petRepository.searchPets("Perro", null, null, null, null)).thenReturn(expectedPets);

        List<Pet> result = petService.searchPets("Perro", null, null, null, null);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getSpecies()).isEqualTo("Perro");
        verify(petRepository).searchPets("Perro", null, null, null, null);
    }

    @Test
    void searchPets_WithAllFilters_ShouldReturnFilteredPets() {
        List<Pet> expectedPets = Arrays.asList(pet1);
        when(petRepository.searchPets("Perro", "Labrador", 3, "Macho", "Santiago"))
            .thenReturn(expectedPets);

        List<Pet> result = petService.searchPets("Perro", "Labrador", 3, "Macho", "Santiago");

        assertThat(result).hasSize(1);
        verify(petRepository).searchPets("Perro", "Labrador", 3, "Macho", "Santiago");
    }

    @Test
    void searchPets_WhenNoMatch_ShouldReturnEmptyList() {
        when(petRepository.searchPets("Gato", null, null, null, null)).thenReturn(Arrays.asList());

        List<Pet> result = petService.searchPets("Gato", null, null, null, null);

        assertThat(result).isEmpty();
        verify(petRepository).searchPets("Gato", null, null, null, null);
    }
}