package com.tecsup.petclinic.ports;

import com.tecsup.petclinic.entities.Owner;

import java.util.List;
import java.util.Optional;

/**
 * Puerto de entrada para operaciones de lectura/escritura de Owners.
 * Esta abstracción permite que OwnerServiceImpl no dependa de ningún repository.
 */
public interface OwnerDataPort {

    Optional<Owner> findById(Integer id);

    List<Owner> findByLastName(String lastName);

    List<Owner> findByCity(String city);

    boolean existsById(Integer id);

    void deleteById(Integer id);

    Owner save(Owner owner);

    List<Owner> findAll();
}
