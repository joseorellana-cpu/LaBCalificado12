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

import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Pruebas de integración del OwnerController contra H2 (sin mocks del servicio).
 * Integrante C — Actualización y borrado.
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
     * S — Cada test tiene una sola responsabilidad: actualización exitosa.
     * Integrante C: testUpdateOwner → PUT /owners/{id} → 200 OK, luego GET confirma el cambio.
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
     * L — Los mocks son intercambiables; aquí H2 real con comportamiento consistente.
     * Integrante C: testDeleteOwner → DELETE /owners/{id} → 200 OK.
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

    /**
     * I — Usa solo los endpoints y métodos estrictamente necesarios.
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
}
