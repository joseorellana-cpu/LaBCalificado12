package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración del CRUD básico de OwnerService (Integrante A).
 * Se usa la BD H2 real (sin mocks); @Transactional hace rollback tras cada test,
 * por lo que ningún test comparte estado con otro.
 * Solo se trabaja con OwnerService — sin mencionar Repository.
 */
@SpringBootTest
@Transactional
class OwnerServiceCrudIntegrationTest {

    @Autowired
    private OwnerService ownerService;

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
        assertEquals("Lucia", created.getFirstName());
        assertEquals("Ramirez", created.getLastName());
        assertEquals("Av. Arequipa 123", created.getAddress());
        assertEquals("Lima", created.getCity());
        assertEquals("987654321", created.getTelephone());
    }

    /**
     * L (LSP) + I (ISP): las aserciones solo usan el contrato de OwnerService.
     */
    @Test
    void testUpdateOwner() {
        // Arrange — crea un owner existente a través del service
        OwnerDTO initialOwner = new OwnerDTO(null, "Carlos", "Mendoza", "Junin 789", "Madison", "999555666");
        OwnerDTO created = ownerService.create(initialOwner);
        Integer ownerId = created.getId();

        OwnerDTO changes = new OwnerDTO(ownerId, "Carlos", "Mendoza", "Pizarro 321", "Trujillo", "999000111");

        // Act
        OwnerDTO updated = ownerService.update(changes);

        // Assert
        assertEquals(ownerId, updated.getId());
        assertEquals("Pizarro 321", updated.getAddress());
        assertEquals("Trujillo", updated.getCity());
        assertEquals("999000111", updated.getTelephone());
    }

    /**
     * O (OCP): el test se extiende con nuevos datos/escenarios sin tocar código de producción.
     */
    @Test
    void testDeleteOwner() throws OwnerNotFoundException {
        // Arrange — crea un owner a través del service
        OwnerDTO initialOwner = new OwnerDTO(null, "Ana", "Torres", "Pizarro 321", "Madison", "999777888");
        OwnerDTO created = ownerService.create(initialOwner);
        Integer ownerId = created.getId();

        // Act & Assert — verificamos que delete no lance excepción
        assertDoesNotThrow(() -> ownerService.delete(ownerId));
    }
}
