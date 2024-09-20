package com.udacity.jdnd.course3.critter.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Nationalized;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "customer")
@Data
@NoArgsConstructor
@EqualsAndHashCode
public class Customer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Nationalized
    private String name;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "owner", cascade = CascadeType.ALL, targetEntity = Pet.class)
    private List<Pet> pets;

    private String phoneNumber;
    private String notes;

    public void addPet(Pet pet) {
        pets.add(pet);
    }

}
