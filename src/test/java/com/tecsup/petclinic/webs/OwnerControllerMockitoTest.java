package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.OwnerDTO;
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
 * Integrante A — CRUD básico.
 */
@Slf4j
@WebMvcTest(OwnerController.class)
@Import(OwnerControllerMockitoTest.MockConfig.class)
public class OwnerControllerMockitoTest {

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
     * Integrante A: testCreateOwner → POST /owners → 201 Created.
     */
    @Test
    public void testCreateOwner() throws Exception {
        // Arrange
        OwnerDTO mockCreated = new OwnerDTO(1, "Carlos", "Rodriguez",
                "Av. Javier 456", "Lima", "987654321");
        when(ownerService.create(any(OwnerDTO.class))).thenReturn(mockCreated);

        String json = """
            {
                "firstName": "Carlos",
                "lastName": "Rodriguez",
                "address": "Av. Javier 456",
                "city": "Lima",
                "telephone": "987654321"
            }
            """;

        // Act & Assert
        mockMvc.perform(post("/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.firstName", equalTo("Carlos")))
                .andExpect(jsonPath("$.lastName", equalTo("Rodriguez")))
                .andExpect(jsonPath("$.address", equalTo("Av. Javier 456")))
                .andExpect(jsonPath("$.city", equalTo("Lima")))
                .andExpect(jsonPath("$.telephone", equalTo("987654321")));

        verify(ownerService, times(1)).create(any(OwnerDTO.class));
        log.info("testCreateOwner: SOLID S — una sola responsabilidad (crear owner)");
    }

    /**
     * Integrante A: testUpdateOwner → PUT /owners/{id} → 200 OK.
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
     * Integrante A: testDeleteOwner → DELETE /owners/{id} → 204 No Content.
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
}
