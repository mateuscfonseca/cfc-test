package org.mateus.service;

import static java.util.stream.Collectors.toList;

import java.util.List;

import org.jboss.logging.Logger;
import org.mateus.dtos.BuscaProdutoResponseDTO;
import org.mateus.dtos.CreateProdutoRequestDTO;
import org.mateus.dtos.UpdateProdutoRequestDTO;
import org.mateus.mappers.ProdutoMapper;
import org.mateus.persistence.entities.Produto;
import org.mateus.persistence.repositories.ProdutoRepository;
import org.mateus.service.exceptions.ProdutoException;
import org.mateus.service.exceptions.ProdutoNaoEncontradoException;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@ApplicationScoped
public class ProdutoService {

    private final ProdutoRepository repository;
    private static final Logger LOG = Logger.getLogger(ProdutoService.class);

    @Inject
    public ProdutoService(ProdutoRepository repository) {
        this.repository = repository;
    }

    public BuscaProdutoResponseDTO buscarPorId(Long id) throws ProdutoNaoEncontradoException {

        final var optProduto = this.repository.findByIdOptional(id);

        if (optProduto.isEmpty())
            throw new ProdutoNaoEncontradoException();

        return ProdutoMapper.toBuscaProdutoResponseDTO(optProduto.get());

    }

    public List<BuscaProdutoResponseDTO> listarPaginado(int page, int pageSize)
            throws ProdutoException {

        final var produtos = this.repository.findAll().page(page - 1, pageSize);
        return produtos.stream().map(ProdutoMapper::toBuscaProdutoResponseDTO).collect(toList());
    }

    public List<BuscaProdutoResponseDTO> listarTodos()
            throws ProdutoException {
        final var produtos = this.repository.findAll();

        return produtos.stream().map(ProdutoMapper::toBuscaProdutoResponseDTO).collect(toList());
    }

    public long total()
            throws ProdutoException {
        return this.repository.count();
    }

    @Transactional
    public BuscaProdutoResponseDTO criar(@Valid CreateProdutoRequestDTO createDto) throws ProdutoException {
        try {
            Produto produto = ProdutoMapper.fromCreateRequest(createDto);

            this.repository.persistAndFlush(produto);

            return ProdutoMapper.toBuscaProdutoResponseDTO(produto);

        } catch (PersistenceException e) {
            final var mensagem = "Erro ao criar produto";
            LOG.error(mensagem, e);

            throw new ProdutoException(mensagem, e);
        }
    }

    @Transactional
    public BuscaProdutoResponseDTO update(@Valid UpdateProdutoRequestDTO updateDto)
            throws ProdutoException, ProdutoNaoEncontradoException {
        try {
            Produto produto = ProdutoMapper.fromUpdateRequest(updateDto);
            final var optProduto = this.repository.findByIdOptional(updateDto.getId());

            if (optProduto.isEmpty())
                throw new ProdutoNaoEncontradoException();

            final var produtoAtual = optProduto.get();

            produtoAtual.update(produto);

            this.repository.persistAndFlush(produtoAtual);

            return ProdutoMapper.toBuscaProdutoResponseDTO(produtoAtual);

        } catch (PersistenceException e) {
            final var mensagem = "Erro ao atualizar produto";
            LOG.error(mensagem, e);

            throw new ProdutoException(mensagem, e);
        }
    }

    @Transactional
    public void delete(Long id)
            throws ProdutoException, ProdutoNaoEncontradoException {
        try {
            final var optProduto = this.repository.findByIdOptional(id);

            if (optProduto.isEmpty())
                throw new ProdutoNaoEncontradoException();

            this.repository.delete(optProduto.get());

        } catch (PersistenceException e) {
            final var mensagem = "Erro ao atualizar produto";
            LOG.error(mensagem, e);

            throw new ProdutoException(mensagem, e);
        }
    }

}
