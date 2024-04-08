package org.mateus.producers;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ProdutoEventDTO(ProdutoEventType type, Long id, String nome, String descricao, BigDecimal preco, LocalDateTime timeStamp) {
    
}
