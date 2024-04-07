package org.mateus.dtos;

import java.math.BigDecimal;

public record BuscaProdutoResponseDTO(Long id, String nome, String descricao, BigDecimal preco) {
}
