package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.exceptions.OwnerNotFoundException;
import com.tecsup.petclinic.services.OwnerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas del OwnerController con servicio mockeado.
 * Integrante C — Actualización y borrado.
 */
@Slf4j
@WebMvcTest(OwnerController.class)
@Import(OwnerControllerMockitoTest3.MockConfig.class)
public class OwnerControllerMockitoTest3 {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OwnerService ownerService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public OwnerService ownerService() {
            return mock(OwnerService.class);
        }
    }

    /**
     * S — Cada test tiene una sola responsabilidad: actualización con PUT.
     * Integrante C: testUpdateOwner → PUT /owners/{id} → 200 OK.
     */
    @Test
    public void testUpdateOwner() throws Exception {
        // Arrange
        OwnerDTO mockUpdated = new OwnerDTO(1, "Ana Actualizada", "Garcia Actualizada",
                "Calle A 999", "Trujillo", "999888777");
        when(ownerService.update(any(OwnerDTO.class))).thenReturn(mockUpdated);

        String json = """
            {
                "firstName": "Ana Actualizada",
                "lastName": "Garcia Actualizada",
                "address": "Calle A 999",
                "city": "Trujillo",
                "telephone": "999888777"
            }
            """;

        // Act & Assert
        mockMvc.perform(put("/owners/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("Ana Actualizada")))
                .andExpect(jsonPath("$.city", equalTo("Trujillo")));

        verify(ownerService, times(1)).update(any(OwnerDTO.class));
        log.info("testUpdateOwner: SOLID S — una sola responsabilidad (actualizar)");
    }

    /**
     * O — Las pruebas son extensibles sin modificar código de producción.
     * Integrante C: testDeleteOwner → DELETE /owners/{id} → 200 OK.
     */
    @Test
    public void testDeleteOwner() throws Exception {
        // Arrange
        doNothing().when(ownerService).delete(anyInt());

        // Act & Assert
        mockMvc.perform(delete("/owners/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        verify(ownerService, times(1)).delete(1);
        log.info("testDeleteOwner: SOLID O — extensible para otros IDs");
    }

    /**
     * D — Depende de abstracciones (OwnerService), no de implementaciones concretas.
     * Integrante C: testDeleteOwnerKO → DELETE /owners/1000 → 404 Not Found.
     */
    @Test
    public void testDeleteOwnerKO() throws Exception {
        // Arrange
        doThrow(new OwnerNotFoundException("Owner not found with id: 1000"))
                .when(ownerService).delete(1000);

        // Act & Assert
        mockMvc.perform(delete("/owners/1000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(ownerService, times(1)).delete(1000);
        log.info("testDeleteOwnerKO: SOLID D — dependencia en abstracción OwnerService");
    }
}
