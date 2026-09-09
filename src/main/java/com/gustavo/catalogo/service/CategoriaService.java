package com.gustavo.catalogo.service;

import com.gustavo.catalogo.dto.CategoriaRequest;
import com.gustavo.catalogo.dto.CategoriaResponse;
import com.gustavo.catalogo.exception.RecursoNaoEncontradoException;
import com.gustavo.catalogo.model.Categoria;
import com.gustavo.catalogo.repository.CategoriaRepository;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class CategoriaService {

    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional(readOnly = true)
    public List<CategoriaResponse> listar() {
        return categoriaRepository.findAll().stream()
                .map(CategoriaResponse::de)
                .toList();
    }

    @Transactional(readOnly = true)
    public CategoriaResponse buscarPorId(Long id) {
        return CategoriaResponse.de(buscarEntidadePorId(id));
    }

    @Transactional(readOnly = true)
    public Categoria buscarEntidadePorId(Long id) {
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNaoEncontradoException("Categoria", id));
    }

    public CategoriaResponse criar(CategoriaRequest request) {
        Categoria categoria = new Categoria(request.nome(), request.descricao());
        return CategoriaResponse.de(categoriaRepository.save(categoria));
    }

    public CategoriaResponse atualizar(Long id, CategoriaRequest request) {
        Categoria categoria = buscarEntidadePorId(id);
        categoria.setNome(request.nome());
        categoria.setDescricao(request.descricao());
        return CategoriaResponse.de(categoria);
    }

    public void excluir(Long id) {
        Categoria categoria = buscarEntidadePorId(id);
        categoriaRepository.delete(categoria);
    }
}
