package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.services.OwnerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas del OwnerController con servicio mockeado.
 * Integrante A — Listado y consulta.
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
     * D — Depende de abstracciones (OwnerService), no de implementaciones concretas.
     * Integrante A: testFindAllOwners → GET /owners → 200 OK, JSON, primer id = 1.
     */
    @Test
    public void testFindAllOwners() throws Exception {
        // Arrange
        OwnerDTO mockOwner = new OwnerDTO(1, "John", "Franklin", "123 Main St", "Madison", "5551234");
        when(ownerService.findAll()).thenReturn(List.of(mockOwner));

        // Act & Assert
        mockMvc.perform(get("/owners")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)));

        verify(ownerService, times(1)).findAll();
        log.info("testFindAllOwners: SOLID D — dependencia en abstracción OwnerService");
    }

    /**
     * S — Cada test tiene una sola responsabilidad: obtener un owner por ID.
     * Integrante A: testFindOwnerOK → GET /owners/1 → 200 OK, jsonPath $.lastName = "Franklin".
     */
    @Test
    public void testFindOwnerOK() throws Exception {
        // Arrange
        OwnerDTO mockOwner = new OwnerDTO(1, "John", "Franklin", "123 Main St", "Madison", "5551234");
        when(ownerService.findById(1)).thenReturn(mockOwner);

        // Act & Assert
        mockMvc.perform(get("/owners/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", is("Franklin")));

        verify(ownerService, times(1)).findById(1);
        log.info("testFindOwnerOK: SOLID S — un test, una responsabilidad (consulta por ID)");
    }

    /**
     * I — Usa solo el método estrictamente necesario de OwnerService (findById).
     * Integrante A: testFindOwnerKO → GET /owners/666 → 404 Not Found.
     */
    @Test
    public void testFindOwnerKO() throws Exception {
        // Arrange
        when(ownerService.findById(666)).thenThrow(new com.tecsup.petclinic.exceptions.OwnerNotFoundException("Owner not found with id: 666"));

        // Act & Assert
        mockMvc.perform(get("/owners/666")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        verify(ownerService, times(1)).findById(666);
        log.info("testFindOwnerKO: SOLID I — usa solo findById, métodos mínimos de la interfaz");
    }
}
