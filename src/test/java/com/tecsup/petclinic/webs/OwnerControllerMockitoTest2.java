package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.services.OwnerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.everyItem;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.never;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas del OwnerController con servicio mockeado.
 * Integrante B — Creación.
 */
@Slf4j
@WebMvcTest(OwnerController.class)
@Import(OwnerControllerMockitoTest2.MockConfig.class)
public class OwnerControllerMockitoTest2 {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OwnerService ownerService;

    @BeforeEach
    void setUp() {
        org.mockito.Mockito.reset(ownerService);
    }

    @TestConfiguration
    static class MockConfig {
        @Bean
        public OwnerService ownerService() {
            return mock(OwnerService.class);
        }
    }

    /**
     * S — Cada test tiene una sola responsabilidad: creación exitosa.
     * Integrante B: testCreateOwner → POST /owners → 201 Created y jsonPath de los campos enviados.
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
     * O — Extensible: nuevos campos en el JSON no requieren cambiar el test.
     * Integrante B: testCreateOwner_BadRequest → cuerpo inválido (sin lastName) → 400 Bad Request.
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

    /**
     * D — Depende de abstracciones (OwnerService) y no de implementaciones concretas.
     * Integrante B: testFindByLastName → GET /owners?lastName=Davis → 2 resultados.
     */
    @Test
    public void testFindByLastName() throws Exception {
        // Arrange
        OwnerDTO d1 = new OwnerDTO(1, "Jaime", "Davis", "Calle Falsa 123", "Santiago", "999111222");
        OwnerDTO d2 = new OwnerDTO(2, "Maria", "Davis", "Av. Libertad 456", "Lima", "999333444");
        when(ownerService.findByLastName("Davis")).thenReturn(List.of(d1, d2));

        // Act & Assert
        mockMvc.perform(get("/owners")
                        .param("lastName", "Davis")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].lastName", everyItem(equalTo("Davis"))));

        verify(ownerService, times(1)).findByLastName("Davis");
        log.info("testFindByLastName: SOLID D — dependencia en abstracción OwnerService");
    }
}
