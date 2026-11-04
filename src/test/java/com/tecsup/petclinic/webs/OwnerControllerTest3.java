package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.OwnerDTO;
import com.tecsup.petclinic.services.OwnerService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración del OwnerController contra H2 (sin mocks del servicio).
 * Integrante C — Casos de error.
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class OwnerControllerTest3 {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OwnerService ownerService;

    /**
     * Integrante C: testFindOwnerKO → GET /owners/666 → 404 Not Found.
     */
    @Test
    public void testFindOwnerKO() throws Exception {
        // Arrange — ID 666 no existe
        // Act & Assert
        mockMvc.perform(get("/owners/666")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        log.info("testFindOwnerKO: SOLID L — comportamiento consistente independiente de la fuente de datos");
    }

    /**
     * Integrante C: testDeleteOwnerKO → DELETE /owners/1000 → 404 Not Found.
     */
    @Test
    public void testDeleteOwnerKO() throws Exception {
        // Arrange — ID 1000 no existe
        // Act & Assert
        mockMvc.perform(delete("/owners/1000")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        log.info("testDeleteOwnerKO: SOLID I — solo usa deleteById, mínimo de la interfaz");
    }

    /**
     * Integrante C: testCreateOwner_BadRequest → cuerpo inválido (sin lastName) → 400 Bad Request.
     */
    @Test
    public void testCreateOwner_BadRequest() throws Exception {
        // Arrange — JSON sin lastName (campo requerido)
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

        log.info("testCreateOwner_BadRequest: SOLID S — una sola responsabilidad (validación de entrada)");
    }
}
