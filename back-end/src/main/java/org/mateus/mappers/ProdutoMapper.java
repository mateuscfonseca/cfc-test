package org.mateus.mappers;

import org.mateus.dtos.BuscaProdutoResponseDTO;
import org.mateus.dtos.CreateProdutoEventDTO;
import org.mateus.dtos.CreateProdutoRequestDTO;
import org.mateus.dtos.UpdateProdutoRequestDTO;
import org.mateus.persistence.entities.Produto;
import org.mateus.utils.DateUtils;

public class ProdutoMapper {

    public static Produto fromCreateRequest(CreateProdutoRequestDTO dto) {
        return new Produto().nome(dto.getNome()).descricao(dto.getDescricao()).preco(dto.getPreco());
    }

    public static Produto fromUpdateRequest(UpdateProdutoRequestDTO dto) {
        return new Produto().id(dto.getId()).nome(dto.getNome()).descricao(dto.getDescricao()).preco(dto.getPreco());
    }

    public static BuscaProdutoResponseDTO toBuscaProdutoResponseDTO(Produto produto) {
        return new BuscaProdutoResponseDTO(produto.getId(), produto.getNome(), produto.getDescricao(),
                produto.getPreco());
    }

    public static CreateProdutoEventDTO toCreateEvent(Produto produto) {
        return new CreateProdutoEventDTO(produto.getId(),produto.getNome(), produto.getDescricao(), produto.getPreco(), DateUtils.dataHoraAtual());
    }

}
