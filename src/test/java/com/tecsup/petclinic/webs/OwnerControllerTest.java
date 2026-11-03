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
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración del OwnerController contra H2 (sin mocks del servicio).
 * Integrante A — Listado y consulta.
 */
@Slf4j
@SpringBootTest
@AutoConfigureMockMvc
public class OwnerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OwnerService ownerService;

    /**
     * S — Cada test verifica una sola responsabilidad: listar todos los owners.
     * Integrante A: testFindAllOwners → GET /owners → 200 OK, JSON, primer id = 1.
     */
    @Test
    public void testFindAllOwners() throws Exception {
        // Arrange — al menos un owner debe existir en H2
        var owners = ownerService.findAll();
        if (owners.isEmpty()) {
            var dto = new OwnerDTO(null, "Test", "TestOwner", "Test Addr", "Test City", "123456");
            dto = ownerService.create(dto);
        }

        // Act & Assert
        mockMvc.perform(get("/owners")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(greaterThanOrEqualTo(1))))
                .andExpect(jsonPath("$[0].id", notNullValue()));

        log.info("testFindAllOwners: SOLID S — un test, una responsabilidad (listar)");
    }

    /**
     * O — Extensible: probar cualquier ID sin modificar código.
     * Integrante A: testFindOwnerOK → GET /owners/1 → 200 OK, jsonPath $.lastName = "Franklin".
     */
    @Test
    public void testFindOwnerOK() throws Exception {
        // Arrange — asegurar owner con lastName "Franklin"
        try {
            ownerService.findById(1);
        } catch (Exception e) {
            var dto = new OwnerDTO(null, "John", "Franklin", "123 Main St", "Madison", "5551234");
            ownerService.create(dto);
        }

        // Act & Assert
        mockMvc.perform(get("/owners/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.lastName", is("Franklin")));

        log.info("testFindOwnerOK: SOLID O — extensible para otros IDs sin cambiar código");
    }

    /**
     * L — Los mocks son intercambiables; aquí usamos H2 real sin romper aserciones.
     * Integrante A: testFindOwnerKO → GET /owners/666 → 404 Not Found.
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
}
