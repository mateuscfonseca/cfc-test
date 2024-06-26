package org.mateus.service;

import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Objects;

import org.jboss.logging.Logger;
import org.mateus.dtos.BuscaProdutoResponseDTO;
import org.mateus.dtos.CreateProdutoRequestDTO;
import org.mateus.dtos.UpdateProdutoRequestDTO;
import org.mateus.mappers.ProdutoMapper;
import org.mateus.persistence.entities.Produto;
import org.mateus.persistence.repositories.ProdutoRepository;
import org.mateus.producers.ProdutoProducer;
import org.mateus.service.exceptions.ProdutoException;
import org.mateus.service.exceptions.ProdutoNaoEncontradoException;

import com.fasterxml.jackson.core.JsonProcessingException;

import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.PersistenceException;
import jakarta.transaction.HeuristicMixedException;
import jakarta.transaction.HeuristicRollbackException;
import jakarta.transaction.NotSupportedException;
import jakarta.transaction.RollbackException;
import jakarta.transaction.SystemException;
import jakarta.transaction.TransactionManager;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;

@ApplicationScoped
public class ProdutoService {

    private final ProdutoRepository repository;
    private final ProdutoProducer producer;
    private final TransactionManager transactionManager;
    private static final Logger LOG = Logger.getLogger(ProdutoService.class);

    @Inject
    public ProdutoService(ProdutoRepository repository, ProdutoProducer producer,
            TransactionManager transactionManager) {
        this.repository = repository;
        this.producer = producer;
        this.transactionManager = transactionManager;
    }

    public BuscaProdutoResponseDTO buscarPorId(Long id) throws ProdutoNaoEncontradoException {

        final var optProduto = this.repository.findByIdOptional(id);

        if (optProduto.isEmpty())
            throw new ProdutoNaoEncontradoException();

        return ProdutoMapper.toBuscaProdutoResponseDTO(optProduto.get());

    }

    public List<BuscaProdutoResponseDTO> listarPaginado(int page, int pageSize, String sorbyColumn, Boolean sortByAscending)
            throws ProdutoException {

        var sort = Sort.descending("id");
        sorbyColumn = ProdutoMapper.translateSortByColumn(sorbyColumn);
        if(Objects.nonNull(sorbyColumn) && !sorbyColumn.isEmpty() && Objects.nonNull(sortByAscending)) {
            sort = sortByAscending ? Sort.ascending(sorbyColumn) : Sort.descending(sorbyColumn);
        }

        final var produtos = this.repository.findAll(sort).page(page - 1, pageSize);
        return produtos.stream().map(ProdutoMapper::toBuscaProdutoResponseDTO).collect(toList());
    }

    public List<BuscaProdutoResponseDTO> listarTodos()
            throws ProdutoException {
        final var produtos = this.repository.findAll();

        return produtos.stream().map(ProdutoMapper::toBuscaProdutoResponseDTO).collect(toList());
    }

    public long total() {
        return this.repository.count();
    }

    public BuscaProdutoResponseDTO criar(@Valid CreateProdutoRequestDTO createDto) throws ProdutoException {
        try {
            Produto produto = ProdutoMapper.fromCreateRequest(createDto);
            this.transactionManager.begin();
            this.repository.persistAndFlush(produto);
            this.transactionManager.commit();

            this.producer.emitir(ProdutoMapper.toCreateEvent(produto));

            return ProdutoMapper.toBuscaProdutoResponseDTO(produto);

        } catch (PersistenceException | JsonProcessingException | NotSupportedException | SystemException
                | SecurityException | IllegalStateException | RollbackException | HeuristicMixedException
                | HeuristicRollbackException e) {
            try {
                this.transactionManager.rollback();
            } catch (IllegalStateException | SecurityException | SystemException e1) {
                e1.printStackTrace();
            }
            final var mensagem = "Erro ao criar produto";
            LOG.error(mensagem, e);

            throw new ProdutoException(mensagem, e);
        }
    }

    public BuscaProdutoResponseDTO update(@Valid UpdateProdutoRequestDTO updateDto)
            throws ProdutoException, ProdutoNaoEncontradoException {
        try {
            this.transactionManager.begin();
            Produto produto = ProdutoMapper.fromUpdateRequest(updateDto);
            final var optProduto = this.repository.findByIdOptional(updateDto.getId());

            if (optProduto.isEmpty())
                throw new ProdutoNaoEncontradoException();

            final var produtoAtual = optProduto.get();

            produtoAtual.update(produto);

            this.repository.persistAndFlush(produtoAtual);
            this.transactionManager.commit();

            this.producer.emitir(ProdutoMapper.toUpdateEvent(produto));

            return ProdutoMapper.toBuscaProdutoResponseDTO(produtoAtual);

        } catch (PersistenceException | NotSupportedException | SystemException | SecurityException
                | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException
                | JsonProcessingException e) {
            try {
                this.transactionManager.rollback();
            } catch (IllegalStateException | SecurityException | SystemException e1) {
                e1.printStackTrace();
            }
            final var mensagem = "Erro ao atualizar produto";
            LOG.error(mensagem, e);

            throw new ProdutoException(mensagem, e);
        }
    }

    public void delete(Long id)
            throws ProdutoException, ProdutoNaoEncontradoException {
        try {
            final var optProduto = this.repository.findByIdOptional(id);

            if (optProduto.isEmpty())
                throw new ProdutoNaoEncontradoException();
            this.transactionManager.begin();
            this.repository.delete(optProduto.get());
            this.transactionManager.commit();

            this.producer.emitir(ProdutoMapper.toDeleteEvent(optProduto.get()));

        } catch (PersistenceException | NotSupportedException | SystemException | SecurityException
                | IllegalStateException | RollbackException | HeuristicMixedException | HeuristicRollbackException
                | JsonProcessingException e) {
            try {
                this.transactionManager.rollback();
            } catch (IllegalStateException | SecurityException | SystemException e1) {
                e1.printStackTrace();
            }
            final var mensagem = "Erro ao atualizar produto";
            LOG.error(mensagem, e);

            throw new ProdutoException(mensagem, e);
        }
    }

}
