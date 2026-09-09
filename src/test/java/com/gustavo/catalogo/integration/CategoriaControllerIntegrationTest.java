package com.gustavo.catalogo.integration;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavo.catalogo.dto.CategoriaRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

class CategoriaControllerIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void deveCriarListarEExcluirCategoria() throws Exception {
        CategoriaRequest request = new CategoriaRequest("Informática", "Produtos de informática");

        String resposta = mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.nome", is("Informática")))
                .andReturn().getResponse().getContentAsString();

        Long id = objectMapper.readTree(resposta).get("id").asLong();

        mockMvc.perform(get("/categorias"))
                .andExpect(status().isOk());

        mockMvc.perform(get("/categorias/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome", is("Informática")));

        mockMvc.perform(delete("/categorias/{id}", id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/categorias/{id}", id))
                .andExpect(status().isNotFound());
    }

    @Test
    void deveRetornarBadRequestParaNomeEmBranco() throws Exception {
        CategoriaRequest request = new CategoriaRequest("", null);

        mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.camposInvalidos.nome").exists());
    }
}
