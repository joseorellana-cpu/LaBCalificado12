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

import static org.hamcrest.Matchers.equalTo;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas del OwnerController con servicio mockeado.
 * Integrante C — Casos de error.
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
     * Integrante C: testFindOwnerKO → GET /owners/666 → 404 Not Found.
     */
    @Test
    public void testFindOwnerKO() throws Exception {
        // Arrange
        when(ownerService.findById(666)).thenThrow(new OwnerNotFoundException("Owner not found with id: 666"));

        // Act & Assert
        mockMvc.perform(get("/owners/666")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(ownerService, times(1)).findById(666);
        log.info("testFindOwnerKO: SOLID I — usa solo findById, métodos mínimos de la interfaz");
    }

    /**
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

    /**
     * Integrante C: testCreateOwner_BadRequest → cuerpo inválido (sin lastName) → 400 Bad Request.
     */
    @Test
    public void testCreateOwner_BadRequest() throws Exception {
        // Arrange — JSON sin lastName
        String json = """
            {
                "firstName": "Test",
                "address": "Test",
                "city": "Test",
                "telephone": "123"
            }
            """;

        // Act & Assert
        mockMvc.perform(post("/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest());

        verify(ownerService, never()).create(any(OwnerDTO.class));
        log.info("testCreateOwner_BadRequest: SOLID O — extensible a otros campos requeridos");
    }
}
