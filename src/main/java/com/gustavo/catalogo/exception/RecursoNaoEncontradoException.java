package com.gustavo.catalogo.exception;

public class RecursoNaoEncontradoException extends RuntimeException {

    public RecursoNaoEncontradoException(String recurso, Long id) {
        super("%s não encontrado(a) com id %d".formatted(recurso, id));
    }
}
