package org.mateus.producers;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.logging.Logger;
import org.mateus.resources.ProdutoResource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class ProdutoProducer {

    private static final Logger LOG = Logger.getLogger(ProdutoProducer.class);

    @Inject
    @Channel("produtos-out")
    Emitter<String> produtoEmitter;

    @Inject
    ObjectMapper mapper;

    public void emitir(ProdutoEventDTO dto) throws JsonProcessingException {

        final var dtoAsString = mapper.writeValueAsString(dto);

        
        produtoEmitter.send(dtoAsString) .whenComplete((result, ex) -> {
            if (ex != null) {
                // Loga a exceção se ocorrer
                LOG.error("Erro ao enviar mensagem para o Kafka: " + ex.getMessage(), ex);
            } else {
                // Loga que o envio foi bem-sucedido
                LOG.info("Mensagem enviada com sucesso para o Kafka!");
            }
        }).toCompletableFuture();
    }

    
}
