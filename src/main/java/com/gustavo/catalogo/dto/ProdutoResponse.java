package com.gustavo.catalogo.dto;

import com.gustavo.catalogo.model.Produto;
import java.math.BigDecimal;

public record ProdutoResponse(
        Long id,
        String nome,
        String descricao,
        BigDecimal preco,
        int quantidadeEstoque,
        CategoriaResponse categoria
) {

    public static ProdutoResponse de(Produto produto) {
        return new ProdutoResponse(
                produto.getId(),
                produto.getNome(),
                produto.getDescricao(),
                produto.getPreco(),
                produto.getQuantidadeEstoque(),
                CategoriaResponse.de(produto.getCategoria())
        );
    }
}
