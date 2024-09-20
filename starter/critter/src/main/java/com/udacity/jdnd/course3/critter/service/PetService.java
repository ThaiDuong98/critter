package com.udacity.jdnd.course3.critter.service;

import com.udacity.jdnd.course3.critter.entity.Pet;

import java.util.List;

public interface PetService {
    Pet savePet(Pet pet);
    Pet findPetById(Long petId);
    List<Pet> getAllPets();
    List<Pet> getPetsByOwnerId(Long ownerId);
    Pet getPetById(Long petId);
}
