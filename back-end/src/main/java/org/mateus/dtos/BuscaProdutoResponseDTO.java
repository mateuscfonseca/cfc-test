package org.mateus.dtos;

import java.math.BigDecimal;

import org.eclipse.microprofile.openapi.annotations.media.Schema;

@Schema
public record BuscaProdutoResponseDTO(Long id, String nome, String descricao, BigDecimal preco) {
}
