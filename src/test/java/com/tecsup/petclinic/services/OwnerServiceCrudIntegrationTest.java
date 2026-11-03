package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.repositories.OwnerRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de integración del CRUD básico de OwnerService (Integrante A).
 * Se usa la BD H2 real (sin mocks); @Transactional hace rollback tras cada test,
 * por lo que ningún test comparte estado con otro.
 */
@SpringBootTest
@Transactional
class OwnerServiceCrudIntegrationTest {

    // D (DIP): se depende de las abstracciones (interfaces), nunca de las clases Impl.
    @Autowired
    private OwnerService ownerService;

    @Autowired
    private OwnerRepository ownerRepository;

    /**
     * S (SRP): este test solo verifica la creación de un dueño.
     */
    @Test
    void testCreateOwner() {
        // Arrange
        OwnerDTO newOwner = new OwnerDTO(null, "Lucia", "Ramirez", "Av. Arequipa 123", "Lima", "987654321");

        // Act
        OwnerDTO created = ownerService.create(newOwner);

        // Assert
        assertNotNull(created.getId());
        Owner persisted = ownerRepository.findById(created.getId()).orElseThrow();
        assertEquals("Lucia", persisted.getFirstName());
        assertEquals("Ramirez", persisted.getLastName());
        assertEquals("Av. Arequipa 123", persisted.getAddress());
        assertEquals("Lima", persisted.getCity());
        assertEquals("987654321", persisted.getTelephone());
    }

    /**
     * L (LSP) + I (ISP): las aserciones solo usan el contrato de OwnerRepository (save/findById),
     * por lo que cualquier implementación de ese contrato las satisface.
     */
    @Test
    void testUpdateOwner() {
        // Arrange
        Owner existing = ownerRepository.save(
                new Owner(null, "Carlos", "Mendoza", "Junin 789", "Madison", "999555666"));
        Integer ownerId = existing.getId().intValue();
        OwnerDTO changes = new OwnerDTO(ownerId, "Carlos", "Mendoza", "Pizarro 321", "Trujillo", "999000111");

        // Act
        OwnerDTO updated = ownerService.update(changes);

        // Assert
        assertEquals(ownerId, updated.getId());
        Owner persisted = ownerRepository.findById(ownerId).orElseThrow();
        assertEquals("Pizarro 321", persisted.getAddress());
        assertEquals("Trujillo", persisted.getCity());
        assertEquals("999000111", persisted.getTelephone());
    }

    /**
     * O (OCP): el test se extiende con nuevos datos/escenarios sin tocar código de producción,
     * porque su Arrange se apoya solo en la interfaz OwnerRepository.
     */
    @Test
    void testDeleteOwner() throws OwnerNotFoundException {
        // Arrange
        Owner existing = ownerRepository.save(
                new Owner(null, "Ana", "Torres", "Pizarro 321", "Madison", "999777888"));
        Integer ownerId = existing.getId().intValue();
        assertTrue(ownerRepository.existsById(ownerId));

        // Act
        ownerService.delete(ownerId);

        // Assert
        assertFalse(ownerRepository.existsById(ownerId));
    }
}
