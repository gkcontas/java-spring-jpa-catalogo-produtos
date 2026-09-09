package com.gustavo.catalogo.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.gustavo.catalogo.dto.EstoqueRequest;
import com.gustavo.catalogo.dto.ProdutoRequest;
import com.gustavo.catalogo.exception.EstoqueInsuficienteException;
import com.gustavo.catalogo.exception.RecursoNaoEncontradoException;
import com.gustavo.catalogo.model.Categoria;
import com.gustavo.catalogo.model.Produto;
import com.gustavo.catalogo.repository.ProdutoRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private ProdutoRepository produtoRepository;

    @Mock
    private CategoriaService categoriaService;

    @InjectMocks
    private ProdutoService produtoService;

    private Categoria categoria;
    private Produto produto;

    @BeforeEach
    void setUp() {
        categoria = new Categoria("Eletrônicos", "Produtos eletrônicos");
        produto = new Produto("Mouse", "Mouse sem fio", new BigDecimal("99.90"), 10, categoria);
    }

    @Test
    void deveCriarProdutoComCategoriaExistente() {
        when(categoriaService.buscarEntidadePorId(1L)).thenReturn(categoria);
        when(produtoRepository.save(any(Produto.class))).thenReturn(produto);

        ProdutoRequest request = new ProdutoRequest("Mouse", "Mouse sem fio", new BigDecimal("99.90"), 10, 1L);

        var resposta = produtoService.criar(request);

        assertThat(resposta.nome()).isEqualTo("Mouse");
        assertThat(resposta.quantidadeEstoque()).isEqualTo(10);
        assertThat(resposta.categoria().nome()).isEqualTo("Eletrônicos");
    }

    @Test
    void deveLancarExcecaoAoBuscarProdutoInexistente() {
        when(produtoRepository.findById(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> produtoService.buscarPorId(99L))
                .isInstanceOf(RecursoNaoEncontradoException.class)
                .hasMessageContaining("Produto");
    }

    @Test
    void deveAdicionarEstoqueComSucesso() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        var resposta = produtoService.entradaEstoque(1L, new EstoqueRequest(5));

        assertThat(resposta.quantidadeEstoque()).isEqualTo(15);
    }

    @Test
    void deveLancarExcecaoAoRemoverEstoqueMaiorQueDisponivel() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        assertThatThrownBy(() -> produtoService.saidaEstoque(1L, new EstoqueRequest(50)))
                .isInstanceOf(EstoqueInsuficienteException.class)
                .hasMessageContaining("Estoque insuficiente");
    }

    @Test
    void deveRemoverEstoqueComSucesso() {
        when(produtoRepository.findById(1L)).thenReturn(Optional.of(produto));

        var resposta = produtoService.saidaEstoque(1L, new EstoqueRequest(4));

        assertThat(resposta.quantidadeEstoque()).isEqualTo(6);
    }
}
