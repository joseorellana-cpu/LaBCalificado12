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

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas del OwnerController con servicio mockeado.
 * Integrante B — Búsquedas y consultas.
 */
@Slf4j
@WebMvcTest(OwnerController.class)
@Import(OwnerControllerMockitoTest2.MockConfig.class)
public class OwnerControllerMockitoTest2 {

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
     * Integrante B: testFindOwnerOK → GET /owners/1 → 200 OK.
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

    /**
     * Integrante B: testFindAllOwners → GET /owners → 200 OK, JSON.
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
}
