package com.adopcion.mascotas.controller;

import com.adopcion.mascotas.model.Pet;
import com.adopcion.mascotas.service.PetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class HomeController {
    
    @Autowired
    private PetService petService;
    
    @GetMapping("/")
    public String index() {
        return "index";
    }
    
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    
    @GetMapping("/catalog")
    public String catalog(Model model) {
        model.addAttribute("pets", petService.findAvailablePets());
        return "catalog";
    }
    
    @GetMapping("/search")
    public String search(@RequestParam(required = false) String species,
                        @RequestParam(required = false) String breed,
                        @RequestParam(required = false) Integer age,
                        @RequestParam(required = false) String gender,
                        @RequestParam(required = false) String location,
                        Model model) {
        model.addAttribute("pets", petService.searchPets(species, breed, age, gender, location));
        model.addAttribute("species", species);
        model.addAttribute("breed", breed);
        model.addAttribute("age", age);
        model.addAttribute("gender", gender);
        model.addAttribute("location", location);
        return "catalog";
    }
    
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
    
    @GetMapping("/pets/new")
    public String showPetForm(Model model) {
        model.addAttribute("pet", new Pet());
        return "pet-form";
    }
    
    @PostMapping("/pets/save")
    public String savePet(@ModelAttribute Pet pet) {
        petService.save(pet);
        return "redirect:/catalog";
    }
    
    @GetMapping("/pets/edit/{id}")
    public String editPet(@PathVariable Long id, Model model) {
        Pet pet = petService.findById(id);
        model.addAttribute("pet", pet);
        return "pet-form";
    }
    
    @GetMapping("/pets/delete/{id}")
    public String deletePet(@PathVariable Long id) {
        petService.delete(id);
        return "redirect:/catalog";
    }
}