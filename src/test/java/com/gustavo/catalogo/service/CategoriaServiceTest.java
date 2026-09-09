package com.gustavo.catalogo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.gustavo.catalogo.dto.CategoriaRequest;
import com.gustavo.catalogo.exception.RecursoNaoEncontradoException;
import com.gustavo.catalogo.model.Categoria;
import com.gustavo.catalogo.repository.CategoriaRepository;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CategoriaServiceTest {

    @Mock
    private CategoriaRepository categoriaRepository;

    @InjectMocks
    private CategoriaService categoriaService;

    @Test
    void deveCriarCategoria() {
        Categoria categoria = new Categoria("Livros", "Livros em geral");
        when(categoriaRepository.save(any(Categoria.class))).thenReturn(categoria);

        var resposta = categoriaService.criar(new CategoriaRequest("Livros", "Livros em geral"));

        assertThat(resposta.nome()).isEqualTo("Livros");
    }

    @Test
    void deveLancarExcecaoAoBuscarCategoriaInexistente() {
        when(categoriaRepository.findById(42L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> categoriaService.buscarPorId(42L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("Categoria");
    }

    @Test
    void deveExcluirCategoriaExistente() {
        Categoria categoria = new Categoria("Livros", "Livros em geral");
        when(categoriaRepository.findById(1L)).thenReturn(Optional.of(categoria));

        categoriaService.excluir(1L);

        verify(categoriaRepository).delete(categoria);
    }
}
