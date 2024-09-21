package com.udacity.jdnd.course3.critter.controller;

import com.udacity.jdnd.course3.critter.dto.PetDTO;
import com.udacity.jdnd.course3.critter.entity.Customer;
import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.service.CustomerService;
import com.udacity.jdnd.course3.critter.service.PetService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Handles web requests related to Pets.
 */
@RestController
@RequestMapping("/pet")
@RequiredArgsConstructor
public class PetController {
    private final PetService petService;
    private final CustomerService customerService;

    @PostMapping
    public PetDTO savePet(@RequestBody PetDTO petDTO) {
        try{
            Customer customer = new Customer();
            ModelMapper modelMapper = new ModelMapper();

            if (petDTO.getOwnerId() != 0) {
                customer = customerService.getCustomerById(petDTO.getOwnerId());
            }

            Pet pet = modelMapper.map(petDTO, Pet.class);
            pet.setOwner(customer);
            Pet savedPet = petService.savePet(pet);

            if (customer!= null) {
                customer.addPet(savedPet);
            }

            return convertPetEntityToDTO(savedPet);
        }catch(UnsupportedOperationException e){
            throw new UnsupportedOperationException();
        }
    }

    @GetMapping("/{petId}")
    public PetDTO getPet(@PathVariable long petId) {
        try{
            return convertPetEntityToDTO(petService.getPetById(petId));
        }catch(UnsupportedOperationException e){
            throw new UnsupportedOperationException();
        }
    }

    @GetMapping
    public List<PetDTO> getPets(){
        try{
            List<PetDTO> petsDTO = new ArrayList<>();
            List<Pet> pets = petService.getAllPets();

            pets.forEach(pet -> {
                petsDTO.add(convertPetEntityToDTO(pet));
            });

            return petsDTO;
        }catch(UnsupportedOperationException e){
            throw new UnsupportedOperationException();
        }
    }

    @GetMapping("/owner/{ownerId}")
    public List<PetDTO> getPetsByOwner(@PathVariable long ownerId) {
       try{
           List<PetDTO> petsDTO = new ArrayList<>();
           List<Pet> pets = petService.getPetsByOwnerId(ownerId);

           pets.forEach(pet -> {
               petsDTO.add(convertPetEntityToDTO(pet));
           });

           return petsDTO;
       }catch(UnsupportedOperationException e){
           throw new UnsupportedOperationException();
       }
    }

    private PetDTO convertPetEntityToDTO(Pet savedPet) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(savedPet, PetDTO.class);
    }
}
