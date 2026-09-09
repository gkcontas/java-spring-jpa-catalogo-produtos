package com.gustavo.catalogo.exception;

public class EstoqueInsuficienteException extends RuntimeException {

    public EstoqueInsuficienteException(Long produtoId, int quantidadeDisponivel, int quantidadeSolicitada) {
        super("Estoque insuficiente para o produto %d: disponível %d, solicitado %d"
                .formatted(produtoId, quantidadeDisponivel, quantidadeSolicitada));
    }
}
