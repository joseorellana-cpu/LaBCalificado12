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
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración del OwnerController contra H2 (sin mocks del servicio).
 * Integrante A — CRUD básico.
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
     * Integrante A: testCreateOwner → POST /owners → 201 Created.
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
     * Integrante A: testUpdateOwner → PUT /owners/{id} → 200 OK, luego GET confirma el cambio.
     */
    @Test
    public void testUpdateOwner() throws Exception {
        // Arrange — crear un owner primero
        String json = """
            {
                "firstName": "Ana",
                "lastName": "Garcia",
                "address": "Calle A 100",
                "city": "Lima",
                "telephone": "999111000"
            }
            """;

        var createResult = mockMvc.perform(post("/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse();

        Integer ownerId = Integer.parseInt(createResult.getHeader("Location").replaceAll(".*/", ""));
        assertNotNull(ownerId);

        // Actualizar
        String updateJson = """
            {
                "firstName": "Ana Actualizada",
                "lastName": "Garcia Actualizada",
                "address": "Calle A 999",
                "city": "Trujillo",
                "telephone": "999888777"
            }
            """;

        // Act & Assert — PUT devuelve 200 OK
        mockMvc.perform(put("/owners/" + ownerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(updateJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("Ana Actualizada")))
                .andExpect(jsonPath("$.city", equalTo("Trujillo")));

        // Verificar: GET confirma el cambio
        mockMvc.perform(get("/owners/" + ownerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName", equalTo("Ana Actualizada")))
                .andExpect(jsonPath("$.telephone", equalTo("999888777")));

        log.info("testUpdateOwner: SOLID S — una sola responsabilidad (actualizar)");
    }

    /**
     * Integrante A: testDeleteOwner → DELETE /owners/{id} → 204 No Content.
     */
    @Test
    public void testDeleteOwner() throws Exception {
        // Arrange — crear un owner para eliminar
        String json = """
            {
                "firstName": "ToDelete",
                "lastName": "DeleteMe",
                "address": "Delete St 1",
                "city": "Lima",
                "telephone": "999000111"
            }
            """;

        var createResult = mockMvc.perform(post("/owners")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andReturn().getResponse();

        Integer ownerId = Integer.parseInt(createResult.getHeader("Location").replaceAll(".*/", ""));
        assertNotNull(ownerId);

        // Act & Assert — DELETE devuelve 204 No Content
        mockMvc.perform(delete("/owners/" + ownerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Verificar que ya no existe
        mockMvc.perform(get("/owners/" + ownerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());

        log.info("testDeleteOwner: SOLID L — comportamiento consistente, intercambiable");
    }
}
