package org.mateus.dtos;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CreateProdutoEventDTO(Long id, String nome, String descricao, BigDecimal preco, LocalDateTime timeStamp) {
    
}
