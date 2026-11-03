package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Pruebas de integración para la entidad Owner.
 * Utiliza la base de datos H2 real cargada con data.sql, asegurando que
 * las interacciones con la capa de persistencia funcionen correctamente.
 */
@SpringBootTest
public class OwnerServiceQueryIntegrationTest {

    // Inyección de la interfaz OwnerService para verificar la integración del servicio con la base de datos.
    @Autowired
    private OwnerService ownerService;

    /**
     * Verifica la correcta obtención de un propietario existente por su ID único.
     */
    @Test
    public void testFindOwnerById() throws OwnerNotFoundException {
        // Arrange
        Integer ownerId = 1;

        // Act
        OwnerDTO owner = ownerService.findById(ownerId);

        // Assert
        assertEquals(ownerId, owner.getId());
        assertEquals("George", owner.getFirstName());
        assertEquals("Franklin", owner.getLastName());
        assertEquals("Madison", owner.getCity());
    }

    /**
     * Verifica la búsqueda de propietarios filtrando por apellido.
     * Se espera que la consulta devuelva múltiples resultados si coinciden.
     */
    @Test
    public void testFindByLastName() {
        // Arrange
        String lastName = "Davis";
        int expectedSize = 2;

        // Act
        List<OwnerDTO> owners = ownerService.findByLastName(lastName);

        // Assert
        assertEquals(expectedSize, owners.size());
        assertTrue(owners.stream().allMatch(o -> lastName.equals(o.getLastName())));
    }

    /**
     * Verifica la búsqueda de propietarios filtrando por ciudad.
     * Se asegura que todos los resultados devueltos correspondan a la ciudad solicitada.
     */
    @Test
    public void testFindByCity() {
        // Arrange
        String city = "Madison";
        int expectedSize = 4;

        // Act
        List<OwnerDTO> owners = ownerService.findByCity(city);

        // Assert
        assertEquals(expectedSize, owners.size());
        assertTrue(owners.stream().allMatch(o -> city.equals(o.getCity())));
    }
}