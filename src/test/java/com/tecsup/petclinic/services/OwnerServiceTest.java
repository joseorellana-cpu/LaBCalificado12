package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.entities.Owner;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.ports.OwnerDataPort;
import com.tecsup.petclinic.util.TObjectCreator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas unitarias de OwnerService con mocks — sin mencionar Repository.
 * OwnerServiceImpl depende de OwnerDataPort, no de OwnerRepository.
 */
@Slf4j
@SpringBootTest
public class OwnerServiceTest {

    @Autowired
    private OwnerService ownerService;

    @MockitoBean
    private OwnerDataPort ownerDataPort;

    // =========================================================================
    // Integrante A — CRUD básico
    // =========================================================================

    /**
     * Verifica que create guarde el Owner y devuelva el DTO con el ID generado.
     */
    @Test
    public void testCreateOwner() {
        // Arrange
        String FIRST_NAME = "Lucia";
        String LAST_NAME = "Ramirez";
        String ADDRESS = "Av. Arequipa 123";
        String CITY = "Lima";
        String TELEPHONE = "987654321";

        OwnerDTO newOwnerDTO = new OwnerDTO(null, FIRST_NAME, LAST_NAME, ADDRESS, CITY, TELEPHONE);
        Owner ownerSaved = new Owner(10L, FIRST_NAME, LAST_NAME, ADDRESS, CITY, TELEPHONE);

        Mockito.when(ownerDataPort.save(Mockito.any(Owner.class)))
                .thenReturn(ownerSaved);

        // Act
        OwnerDTO ownerCreated = ownerService.create(newOwnerDTO);

        // Assert
        assertNotNull(ownerCreated);
        assertNotNull(ownerCreated.getId());
        assertEquals(10, ownerCreated.getId().intValue());
        assertEquals(FIRST_NAME, ownerCreated.getFirstName());
        assertEquals(LAST_NAME, ownerCreated.getLastName());
        assertEquals(ADDRESS, ownerCreated.getAddress());
        assertEquals(CITY, ownerCreated.getCity());
        assertEquals(TELEPHONE, ownerCreated.getTelephone());

        // El mapper debe haber convertido el DTO a entidad antes de guardar
        ArgumentCaptor<Owner> captor = ArgumentCaptor.forClass(Owner.class);
        Mockito.verify(ownerDataPort, Mockito.times(1)).save(captor.capture());
        assertEquals(FIRST_NAME, captor.getValue().getFirstName());
        assertEquals(TELEPHONE, captor.getValue().getTelephone());
    }

    /**
     * Verifica que update guarde los datos modificados y los devuelva en el DTO.
     */
    @Test
    public void testUpdateOwner() {
        // Arrange
        Integer OWNER_ID = 3;
        String UP_FIRST_NAME = "Carlos";
        String UP_CITY = "Trujillo";

        OwnerDTO ownerToUpdate = new OwnerDTO(OWNER_ID, UP_FIRST_NAME, "Mendoza",
                "Junin 789", UP_CITY, "999555666");
        Owner ownerUpdated = new Owner(3L, UP_FIRST_NAME, "Mendoza",
                "Junin 789", UP_CITY, "999555666");

        Mockito.when(ownerDataPort.save(Mockito.any(Owner.class)))
                .thenReturn(ownerUpdated);

        // Act
        OwnerDTO ownerResult = ownerService.update(ownerToUpdate);

        // Assert
        assertNotNull(ownerResult);
        assertEquals(OWNER_ID, ownerResult.getId());
        assertEquals(UP_FIRST_NAME, ownerResult.getFirstName());
        assertEquals("Mendoza", ownerResult.getLastName());
        assertEquals(UP_CITY, ownerResult.getCity());

        // Se debe persistir la entidad con los datos actualizados
        ArgumentCaptor<Owner> captor = ArgumentCaptor.forClass(Owner.class);
        Mockito.verify(ownerDataPort, Mockito.times(1)).save(captor.capture());
        assertEquals(UP_FIRST_NAME, captor.getValue().getFirstName());
        assertEquals(UP_CITY, captor.getValue().getCity());
    }

    /**
     * Verifica que delete elimine el Owner cuando el ID existe.
     */
    @Test
    public void testDeleteOwner() {
        // Arrange
        Integer OWNER_ID = 5;

        Mockito.when(ownerDataPort.existsById(OWNER_ID)).thenReturn(true);
        Mockito.doNothing().when(ownerDataPort).deleteById(OWNER_ID);

        // Act & Assert
        assertDoesNotThrow(() -> ownerService.delete(OWNER_ID));

        Mockito.verify(ownerDataPort, Mockito.times(1)).existsById(OWNER_ID);
        Mockito.verify(ownerDataPort, Mockito.times(1)).deleteById(OWNER_ID);
    }

    // =========================================================================
    // Integrante C — Casos de error
    // =========================================================================

    /**
     * Verifica que findById lance OwnerNotFoundException cuando el ID no existe.
     */
    @Test
    public void testFindOwnerById_NotFound() {
        // Arrange
        Mockito.when(ownerDataPort.findById(999))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OwnerNotFoundException.class, () -> {
            ownerService.findById(999);
        });
    }

    /**
     * Verifica que delete lance OwnerNotFoundException cuando el ID no existe.
     */
    @Test
    public void testDeleteOwner_NotFound() {
        // Arrange
        Mockito.when(ownerDataPort.existsById(999)).thenReturn(false);

        // Act & Assert
        assertThrows(OwnerNotFoundException.class, () -> {
            ownerService.delete(999);
        });
    }

    /**
     * Verifica que findByLastName retorne lista vacía cuando no hay coincidencias.
     */
    @Test
    public void testFindByLastName_Empty() {
        // Arrange
        Mockito.when(ownerDataPort.findByLastName("Inexistente"))
                .thenReturn(List.of());

        // Act
        List<OwnerDTO> result = ownerService.findByLastName("Inexistente");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // =========================================================================
    // Integrante B (Johan) — Búsquedas y consultas
    // =========================================================================

    /**
     * Verifica que findById devuelva el Owner correcto cuando existe.
     */
    @Test
    public void testFindOwnerById() {
        // Arrange
        Owner ownerExpected = TObjectCreator.getOwner();
        Mockito.when(ownerDataPort.findById(1))
                .thenReturn(Optional.of(ownerExpected));

        // Act
        OwnerDTO owner;
        try {
            owner = ownerService.findById(1);
        } catch (OwnerNotFoundException e) {
            fail(e.getMessage());
            return;
        }

        // Assert
        log.info("Expected: " + ownerExpected);
        log.info("Returned: " + owner);
        assertEquals(ownerExpected.getId().intValue(), owner.getId());
        assertEquals(ownerExpected.getFirstName(), owner.getFirstName());
        assertEquals(ownerExpected.getLastName(), owner.getLastName());
    }

    /**
     * Verifica que findByLastName devuelva los Owners con el apellido indicado.
     */
    @Test
    public void testFindByLastName() {
        // Arrange
        String FIND_LAST_NAME = "Davis";
        List<Owner> ownersExpected = TObjectCreator.getOwnersForFindByLastName();
        Mockito.when(ownerDataPort.findByLastName(FIND_LAST_NAME))
                .thenReturn(ownersExpected);

        // Act
        List<OwnerDTO> owners = ownerService.findByLastName(FIND_LAST_NAME);

        // Assert
        assertEquals(ownersExpected.size(), owners.size());
        owners.forEach(owner -> assertEquals(FIND_LAST_NAME, owner.getLastName()));
    }

    /**
     * Verifica que findByCity devuelva los Owners de la ciudad indicada.
     */
    @Test
    public void testFindByCity() {
        // Arrange
        String FIND_CITY = "Madison";
        List<Owner> ownersExpected = TObjectCreator.getOwnersForFindByCity();
        Mockito.when(ownerDataPort.findByCity(FIND_CITY))
                .thenReturn(ownersExpected);

        // Act
        List<OwnerDTO> owners = ownerService.findByCity(FIND_CITY);

        // Assert
        assertEquals(ownersExpected.size(), owners.size());
        owners.forEach(owner -> assertEquals(FIND_CITY, owner.getCity()));
    }
}
