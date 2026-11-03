package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.mappers.OwnerMapper;
import com.tecsup.petclinic.ports.OwnerDataPort;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Implementación del servicio de propietarios.
 * Esta clase se encuentra en la capa webs para separar la lógica de negocio
 * de la capa de presentación.
 * No depende de repositories — usa OwnerDataPort como abstracción.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class OwnerServiceImpl implements com.tecsup.petclinic.services.OwnerService {

    private final OwnerDataPort ownerDataPort;
    private final OwnerMapper ownerMapper;

    @Override
    public OwnerDTO findById(Integer id) throws OwnerNotFoundException {
        Optional<Owner> ownerOptional = ownerDataPort.findById(id);
        if (ownerOptional.isPresent()) {
            return ownerMapper.mapToDTO(ownerOptional.get());
        } else {
            throw new OwnerNotFoundException("Owner not found with id: " + id);
        }
    }

    @Override
    public List<OwnerDTO> findByLastName(String lastName) {
        return ownerDataPort.findByLastName(lastName).stream()
                .map(ownerMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OwnerDTO> findByCity(String city) {
        return ownerDataPort.findByCity(city).stream()
                .map(ownerMapper::mapToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) throws OwnerNotFoundException {
        if (ownerDataPort.existsById(id)) {
            ownerDataPort.deleteById(id);
        } else {
            throw new OwnerNotFoundException("Owner not found with id: " + id);
        }
    }

    @Override
    public OwnerDTO create(OwnerDTO ownerDTO) {
        if (ownerDTO.getLastName() == null || ownerDTO.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
        Owner owner = ownerMapper.mapToEntity(ownerDTO);
        Owner savedOwner = ownerDataPort.save(owner);
        return ownerMapper.mapToDTO(savedOwner);
    }

    @Override
    public OwnerDTO update(OwnerDTO ownerDto) {
        Owner owner = ownerMapper.mapToEntity(ownerDto);
        Owner updatedOwner = ownerDataPort.save(owner);
        return ownerMapper.mapToDTO(updatedOwner);
    }

    @Override
    public List<OwnerDTO> findAll() {
        return ownerDataPort.findAll().stream()
                .map(ownerMapper::mapToDTO)
                .collect(Collectors.toList());
    }
}
