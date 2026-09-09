package com.gustavo.catalogo.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProdutoRequest(

        @NotBlank(message = "nome é obrigatório")
        @Size(max = 150, message = "nome deve ter no máximo 150 caracteres")
        String nome,

        @Size(max = 1000, message = "descricao deve ter no máximo 1000 caracteres")
        String descricao,

        @NotNull(message = "preco é obrigatório")
        @DecimalMin(value = "0.0", inclusive = true, message = "preco não pode ser negativo")
        BigDecimal preco,

        @PositiveOrZero(message = "quantidadeEstoque não pode ser negativo")
        Integer quantidadeEstoqueInicial,

        @NotNull(message = "categoriaId é obrigatório")
        @Min(value = 1, message = "categoriaId inválido")
        Long categoriaId
) {
}
