package com.gustavo.catalogo.integration;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gustavo.catalogo.dto.CategoriaRequest;
import com.gustavo.catalogo.dto.EstoqueRequest;
import com.gustavo.catalogo.dto.ProdutoRequest;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

class ProdutoControllerIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long criarCategoria(String nome) throws Exception {
        String resposta = mockMvc.perform(post("/categorias")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CategoriaRequest(nome, null))))
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(resposta).get("id").asLong();
    }

    @Test
    void deveCriarProdutoEMovimentarEstoque() throws Exception {
        Long categoriaId = criarCategoria("Papelaria");

        ProdutoRequest request = new ProdutoRequest("Caderno", "Caderno 100 folhas", new BigDecimal("15.50"), 10, categoriaId);

        String resposta = mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.quantidadeEstoque", is(10)))
                .andReturn().getResponse().getContentAsString();

        Long produtoId = objectMapper.readTree(resposta).get("id").asLong();

        mockMvc.perform(post("/produtos/{id}/estoque/entrada", produtoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new EstoqueRequest(5))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantidadeEstoque", is(15)));

        mockMvc.perform(post("/produtos/{id}/estoque/saida", produtoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new EstoqueRequest(3))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.quantidadeEstoque", is(12)));
    }

    @Test
    void deveRetornarErroAoRemoverEstoqueMaiorQueDisponivel() throws Exception {
        Long categoriaId = criarCategoria("Brinquedos");

        ProdutoRequest request = new ProdutoRequest("Bola", "Bola de futebol", new BigDecimal("40.00"), 2, categoriaId);

        String resposta = mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();

        Long produtoId = objectMapper.readTree(resposta).get("id").asLong();

        mockMvc.perform(post("/produtos/{id}/estoque/saida", produtoId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new EstoqueRequest(10))))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.mensagem").exists());
    }

    @Test
    void deveRetornarNotFoundAoCriarProdutoComCategoriaInexistente() throws Exception {
        ProdutoRequest request = new ProdutoRequest("Item", "desc", new BigDecimal("10.00"), 1, 999999L);

        mockMvc.perform(post("/produtos")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
