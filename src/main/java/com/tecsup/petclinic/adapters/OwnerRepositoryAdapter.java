package com.tecsup.petclinic.adapters;

import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.ports.OwnerDataPort;
import com.tecsup.petclinic.repositories.OwnerRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador que conecta OwnerServiceImpl con OwnerRepository.
 * Esta clase vive en adapters/ para mantener la separación de capas.
 */
@Component
public class OwnerRepositoryAdapter implements OwnerDataPort {

    private final OwnerRepository ownerRepository;

    public OwnerRepositoryAdapter(OwnerRepository ownerRepository) {
        this.ownerRepository = ownerRepository;
    }

    @Override
    public Optional<Owner> findById(Integer id) {
        return ownerRepository.findById(id);
    }

    @Override
    public List<Owner> findByLastName(String lastName) {
        return ownerRepository.findByLastName(lastName);
    }

    @Override
    public List<Owner> findByCity(String city) {
        return ownerRepository.findByCity(city);
    }

    @Override
    public boolean existsById(Integer id) {
        return ownerRepository.existsById(id);
    }

    @Override
    public void deleteById(Integer id) {
        ownerRepository.deleteById(id);
    }

    @Override
    public Owner save(Owner owner) {
        return ownerRepository.save(owner);
    }

    @Override
    public List<Owner> findAll() {
        return ownerRepository.findAll();
    }
}
