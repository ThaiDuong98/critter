package com.udacity.jdnd.course3.critter.service.iml;

import com.udacity.jdnd.course3.critter.entity.Pet;
import com.udacity.jdnd.course3.critter.exception.NotFoundException;
import com.udacity.jdnd.course3.critter.repository.CustomerRepository;
import com.udacity.jdnd.course3.critter.repository.PetRepository;
import com.udacity.jdnd.course3.critter.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class PetServiceImpl implements PetService {
    private final CustomerRepository customerRepository;
    private final PetRepository petRepository;

    @Override
    public Pet savePet(Pet pet){
        return petRepository.save(pet);
    }

    @Override
    public Pet findPetById(Long petId){
        return petRepository.findById(petId).orElseThrow(NotFoundException::new);
    }

    @Override
    public List<Pet> getAllPets(){
        return (List<Pet>) petRepository.findAll();
    }

    @Override
    public List<Pet> getPetsByOwnerId(Long ownerId){
        return (List<Pet>) petRepository.findAllPetByOwnerId(ownerId);
    }

    @Override
    public Pet getPetById(Long petId){
        return petRepository.findById(petId).orElseThrow(NotFoundException::new);
    }
}
