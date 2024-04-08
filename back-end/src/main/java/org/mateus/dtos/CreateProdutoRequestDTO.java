package org.mateus.dtos;

import java.math.BigDecimal;

import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
@Schema
public class CreateProdutoRequestDTO {

    @NotEmpty(message = "O nome não pode ser vazio.")
    @Length(min = 3, max = 255, message = "O nome deve ter no mínimo 3 caracteres e no máximo 255.")
    String nome;

    @NotEmpty(message = "O nome não pode ser vazio.")
    @Length(min = 3, max = 2000, message = "A descrição deve ter no mínimo 3 caracteres e no máximo 2000.")
    String descricao;

    @DecimalMin(value = "0.01", message = "O preço não pode ser zero nem negativo")
    @NotNull(message = "O valor do preço não pode ser nulo")
    BigDecimal preco;


    public CreateProdutoRequestDTO() {
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return this.descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public BigDecimal getPreco() {
        return this.preco;
    }

    public void setPreco(BigDecimal preco) {
        this.preco = preco;
    }

    public CreateProdutoRequestDTO nome(String nome) {
        setNome(nome);
        return this;
    }

    public CreateProdutoRequestDTO descricao(String descricao) {
        setDescricao(descricao);
        return this;
    }

    public CreateProdutoRequestDTO preco(BigDecimal preco) {
        setPreco(preco);
        return this;
    }


}
