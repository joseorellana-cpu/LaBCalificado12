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

import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración del OwnerController contra H2 (sin mocks del servicio).
 * Integrante B — Creación.
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class OwnerControllerTest2 {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OwnerService ownerService;

    /**
     * O — Las pruebas son extensibles: agregar nuevos campos no rompe este test.
     * Integrante B: testCreateOwner → POST /owners → 201 Created y jsonPath de los campos enviados.
     */
    @Test
    public void testCreateOwner() throws Exception {
        // Arrange
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
                .andExpect(jsonPath("$.firstName", is("Carlos")))
                .andExpect(jsonPath("$.lastName", is("Rodriguez")))
                .andExpect(jsonPath("$.address", is("Av. Javier 456")))
                .andExpect(jsonPath("$.city", is("Lima")))
                .andExpect(jsonPath("$.telephone", is("987654321")));

        log.info("testCreateOwner: SOLID O — extensible sin modificar código de producción");
    }

    /**
     * S — Cada test verifica una sola responsabilidad: cuerpo inválido → 400.
     * Integrante B: testCreateOwner_BadRequest → cuerpo inválido (sin lastName) → 400 Bad Request.
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

    /**
     * L — Los mocks son intercambiables; aquí H2 real, comportamiento consistente.
     * Integrante B: testFindByLastName → GET /owners?lastName=Davis → 2 resultados.
     */
    @Test
    public void testFindByLastName() throws Exception {
        // Arrange — asegurarse de que existan al menos 2 con lastName "Davis"
        try {
            var results = ownerService.findByLastName("Davis");
            if (results.size() < 2) {
                var d1 = new OwnerDTO(null, "Jaime", "Davis", "Calle Falsa 123", "Santiago", "999111222");
                var d2 = new OwnerDTO(null, "Maria", "Davis", "Av. Libertad 456", "Lima", "999333444");
                ownerService.create(d1);
                ownerService.create(d2);
            }
        } catch (Exception e) {
            var d1 = new OwnerDTO(null, "Jaime", "Davis", "Calle Falsa 123", "Santiago", "999111222");
            var d2 = new OwnerDTO(null, "Maria", "Davis", "Av. Libertad 456", "Lima", "999333444");
            ownerService.create(d1);
            ownerService.create(d2);
        }

        // Act & Assert
        mockMvc.perform(get("/owners")
                        .param("lastName", "Davis")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(2))))
                .andExpect(jsonPath("$[*].lastName", everyItem(is("Davis"))));

        log.info("testFindByLastName: SOLID L — comportamiento consistente, resultados intercambiables");
    }
}
