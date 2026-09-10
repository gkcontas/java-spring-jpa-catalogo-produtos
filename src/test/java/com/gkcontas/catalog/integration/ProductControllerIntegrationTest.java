package com.gkcontas.catalog.integration;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gkcontas.catalog.dto.CategoryRequest;
import com.gkcontas.catalog.dto.ProductRequest;
import com.gkcontas.catalog.dto.StockRequest;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

class ProductControllerIntegrationTest extends IntegrationTestBase {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long createCategory(String name) throws Exception {
        String response = mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new CategoryRequest(name, null))))
                .andReturn().getResponse().getContentAsString();
        return objectMapper.readTree(response).get("id").asLong();
    }

    @Test
    void shouldCreateProductAndMoveStock() throws Exception {
        Long categoryId = createCategory("Stationery");

        ProductRequest request = new ProductRequest("Notebook", "100-page notebook", new BigDecimal("15.50"), 10, categoryId);

        String response = mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.stockQuantity", is(10)))
                .andReturn().getResponse().getContentAsString();

        Long productId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(post("/products/{id}/stock/increase", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StockRequest(5))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockQuantity", is(15)));

        mockMvc.perform(post("/products/{id}/stock/decrease", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StockRequest(3))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.stockQuantity", is(12)));
    }

    @Test
    void shouldReturnErrorWhenDecreasingMoreThanAvailableStock() throws Exception {
        Long categoryId = createCategory("Toys");

        ProductRequest request = new ProductRequest("Ball", "Soccer ball", new BigDecimal("40.00"), 2, categoryId);

        String response = mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andReturn().getResponse().getContentAsString();

        Long productId = objectMapper.readTree(response).get("id").asLong();

        mockMvc.perform(post("/products/{id}/stock/decrease", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new StockRequest(10))))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(jsonPath("$.message").exists());
    }

    @Test
    void shouldReturnNotFoundWhenCreatingProductWithMissingCategory() throws Exception {
        ProductRequest request = new ProductRequest("Item", "desc", new BigDecimal("10.00"), 1, 999999L);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isNotFound());
    }
}
