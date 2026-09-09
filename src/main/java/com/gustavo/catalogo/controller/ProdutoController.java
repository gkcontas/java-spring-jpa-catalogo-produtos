package com.gustavo.catalogo.controller;

import com.gustavo.catalogo.dto.EstoqueRequest;
import com.gustavo.catalogo.dto.ProdutoRequest;
import com.gustavo.catalogo.dto.ProdutoResponse;
import com.gustavo.catalogo.service.ProdutoService;
import jakarta.validation.Valid;
import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produtos")
public class ProdutoController {

    private final ProdutoService produtoService;

    public ProdutoController(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @GetMapping
    public Page<ProdutoResponse> listar(
            @RequestParam(required = false) Long categoriaId,
            Pageable pageable
    ) {
        return produtoService.listar(categoriaId, pageable);
    }

    @GetMapping("/{id}")
    public ProdutoResponse buscarPorId(@PathVariable Long id) {
        return produtoService.buscarPorId(id);
    }

    @PostMapping
    public ResponseEntity<ProdutoResponse> criar(@Valid @RequestBody ProdutoRequest request) {
        ProdutoResponse criado = produtoService.criar(request);
        return ResponseEntity.created(URI.create("/produtos/" + criado.id())).body(criado);
    }

    @PutMapping("/{id}")
    public ProdutoResponse atualizar(@PathVariable Long id, @Valid @RequestBody ProdutoRequest request) {
        return produtoService.atualizar(id, request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        produtoService.excluir(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/estoque/entrada")
    public ProdutoResponse entradaEstoque(@PathVariable Long id, @Valid @RequestBody EstoqueRequest request) {
        return produtoService.entradaEstoque(id, request);
    }

    @PostMapping("/{id}/estoque/saida")
    public ProdutoResponse saidaEstoque(@PathVariable Long id, @Valid @RequestBody EstoqueRequest request) {
        return produtoService.saidaEstoque(id, request);
    }
}
