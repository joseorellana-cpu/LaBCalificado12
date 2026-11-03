package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.services.OwnerService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

/**
 * Controlador REST para la gestión de propietarios (Owners).
 * Proporciona endpoints CRUD y de búsqueda.
 */
@Slf4j
@RestController
@RequestMapping("/owners")
@RequiredArgsConstructor
public class OwnerController {

    private final OwnerService ownerService;

    // =========================================================================
    // Integrante A — Listado y consulta
    // =========================================================================

    /**
     * Lista todos los propietarios.
     * GET /owners → 200 OK con lista de JSON.
     */
    @GetMapping
    public List<OwnerDTO> findAll() {
        log.info("Listing all owners");
        return ownerService.findAll();
    }

    /**
     * Obtiene un propietario por su ID.
     * GET /owners/{id} → 200 OK con JSON o 404 Not Found.
     */
    @GetMapping("/{id}")
    public OwnerDTO findById(@PathVariable Integer id) throws OwnerNotFoundException {
        log.info("Finding owner by id: {}", id);
        return ownerService.findById(id);
    }

    // =========================================================================
    // Integrante B — Creación y búsqueda por apellido
    // =========================================================================

    /**
     * Crea un nuevo propietario.
     * POST /owners → 201 Created con el JSON del owner creado.
     */
    @PostMapping
    public ResponseEntity<OwnerDTO> create(@RequestBody OwnerDTO ownerDTO) {
        if (ownerDTO.getLastName() == null || ownerDTO.getLastName().isBlank()) {
            throw new IllegalArgumentException("Last name is required");
        }
        log.info("Creating new owner: {}", ownerDTO.getFirstName());
        OwnerDTO created = ownerService.create(ownerDTO);
        return ResponseEntity
                .created(URI.create("/owners/" + created.getId()))
                .body(created);
    }

    /**
     * Busca propietarios por apellido.
     * GET /owners?lastName=Davis → 200 OK con lista de JSON.
     */
    @GetMapping(params = "lastName")
    public List<OwnerDTO> findByLastName(@RequestParam String lastName) {
        log.info("Finding owners by lastName: {}", lastName);
        return ownerService.findByLastName(lastName);
    }

    // =========================================================================
    // Integrante C — Actualización y borrado
    // =========================================================================

    /**
     * Actualiza un propietario existente.
     * PUT /owners/{id} → 200 OK con JSON del owner actualizado.
     */
    @PutMapping("/{id}")
    public OwnerDTO update(@PathVariable Integer id, @RequestBody OwnerDTO ownerDTO) throws OwnerNotFoundException {
        log.info("Updating owner id: {}", id);
        ownerDTO.setId(id);
        return ownerService.update(ownerDTO);
    }

    /**
     * Elimina un propietario por su ID.
     * DELETE /owners/{id} → 204 No Content o 404 Not Found.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) throws OwnerNotFoundException {
        log.info("Deleting owner id: {}", id);
        ownerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
