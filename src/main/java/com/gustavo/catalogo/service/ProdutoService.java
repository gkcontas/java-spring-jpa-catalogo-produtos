package com.gustavo.catalogo.service;

import com.gustavo.catalogo.dto.EstoqueRequest;
import com.gustavo.catalogo.dto.ProdutoRequest;
import com.gustavo.catalogo.dto.ProdutoResponse;
import com.gustavo.catalogo.exception.RecursoNaoEncontradoException;
import com.gustavo.catalogo.model.Categoria;
import com.gustavo.catalogo.model.Produto;
import com.gustavo.catalogo.repository.ProdutoRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProdutoService {

    private final ProdutoRepository produtoRepository;
    private final CategoriaService categoriaService;

    public ProdutoService(ProdutoRepository produtoRepository, CategoriaService categoriaService) {
        this.produtoRepository = produtoRepository;
        this.categoriaService = categoriaService;
    }

    @Transactional(readOnly = true)
    public Page<ProdutoResponse> listar(Long categoriaId, Pageable pageable) {
        Page<Produto> pagina = categoriaId != null
                ? produtoRepository.findByCategoriaId(categoriaId, pageable)
                : produtoRepository.findAll(pageable);
        return pagina.map(ProdutoResponse::de);
    }

    @Transactional(readOnly = true)
    public ProdutoResponse buscarPorId(Long id) {
        return ProdutoResponse.de(buscarEntidadePorId(id));
    }

    private Produto buscarEntidadePorId(Long id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Produto", id));
    }

    public ProdutoResponse criar(ProdutoRequest request) {
        Categoria categoria = categoriaService.buscarEntidadePorId(request.categoriaId());
        int quantidadeInicial = request.quantidadeEstoqueInicial() != null ? request.quantidadeEstoqueInicial() : 0;
        Produto produto = new Produto(
                request.nome(),
                request.descricao(),
                request.preco(),
                quantidadeInicial,
                categoria
        );
        return ProdutoResponse.de(produtoRepository.save(produto));
    }

    public ProdutoResponse atualizar(Long id, ProdutoRequest request) {
        Produto produto = buscarEntidadePorId(id);
        Categoria categoria = categoriaService.buscarEntidadePorId(request.categoriaId());
        produto.atualizarDados(request.nome(), request.descricao(), request.preco(), categoria);
        return ProdutoResponse.de(produto);
    }

    public void excluir(Long id) {
        Produto produto = buscarEntidadePorId(id);
        produtoRepository.delete(produto);
    }

    public ProdutoResponse entradaEstoque(Long id, EstoqueRequest request) {
        Produto produto = buscarEntidadePorId(id);
        produto.adicionarEstoque(request.quantidade());
        return ProdutoResponse.de(produto);
    }

    public ProdutoResponse saidaEstoque(Long id, EstoqueRequest request) {
        Produto produto = buscarEntidadePorId(id);
        produto.removerEstoque(request.quantidade());
        return ProdutoResponse.de(produto);
    }
}
