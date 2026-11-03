package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.ports.OwnerDataPort;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Pruebas de integración — Casos de error de OwnerService (Integrante C).
 *
 * Principio SOLID aplicado — Single Responsibility (S):
 *   Esta clase tiene UNA única responsabilidad: validar comportamientos
 *   de error de OwnerService. No verifica CRUD ni búsquedas, lo que
 *   mantiene el scope acotado y cada test independiente.
 *
 * Solo se trabaja con OwnerService — sin mencionar Repository.
 */
@SpringBootTest
class OwnerServiceErrorIntegrationTest {

    @Autowired
    private OwnerService ownerService;

    @MockitoBean
    private OwnerDataPort ownerDataPort;

    // =========================================================================
    // Integrante C — Casos de error
    // =========================================================================

    /**
     * Verifica que findById lance OwnerNotFoundException cuando el ID no existe.
     *
     * SOLID — Single Responsibility (S):
     *   El test solo valida un comportamiento de error (excepción por ID inexistente),
     *   sin mezclar aserciones de éxito ni otros casos.
     */
    @Test
    void testFindOwnerById_NotFound() {
        // Arrange
        org.mockito.Mockito.when(ownerDataPort.findById(999))
                .thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(OwnerNotFoundException.class, () -> {
            ownerService.findById(999);
        });
    }

    /**
     * Verifica que delete lance OwnerNotFoundException cuando el ID no existe.
     *
     * SOLID — Liskov Substitution (L):
     *   El mock de OwnerDataPort es un doble intercambiable;
     *   el test no depende de la implementación real del repositorio.
     */
    @Test
    void testDeleteOwner_NotFound() {
        // Arrange
        org.mockito.Mockito.when(ownerDataPort.existsById(999))
                .thenReturn(false);

        // Act & Assert
        assertThrows(OwnerNotFoundException.class, () -> {
            ownerService.delete(999);
        });
    }

    /**
     * Verifica que findByLastName retorne lista vacía cuando no hay coincidencias.
     *
     * SOLID — Open/Closed (O):
     *   La capa de servicio se extiende (nuevos escenarios de búsqueda vacía)
     *   sin requerir cambios en el código de producción ni en este test.
     */
    @Test
    void testFindByLastName_Empty() {
        // Arrange
        org.mockito.Mockito.when(ownerDataPort.findByLastName("Inexistente"))
                .thenReturn(List.of());

        // Act
        List<OwnerDTO> result = ownerService.findByLastName("Inexistente");

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
