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
 * Integrante B — Búsquedas y consultas.
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
     * Integrante B: testFindOwnerOK → GET /owners/1 → 200 OK, jsonPath $.lastName = "Franklin".
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
     * Integrante B: testFindByLastName → GET /owners?lastName=Davis → resultados.
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

    /**
     * Integrante B: testFindAllOwners → GET /owners → 200 OK, JSON.
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
}
