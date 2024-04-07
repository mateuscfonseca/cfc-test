package org.mateus.dtos;

import java.math.BigDecimal;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

public class UpdateProdutoRequestDTO {

    @NotNull(message = "Id não pode ser nulo")
    Long id;

    @Length(min = 3, max = 255, message = "O nome deve ter no mínimo 3 caracteres e no máximo 255.")
    String nome;

    @Length(min = 3, max = 2000, message = "A descrição deve ter no mínimo 3 caracteres e no máximo 2000.")
    String descricao;

    @DecimalMin(value = "0.01", message = "O preço não pode ser zero nem negativo")
    BigDecimal preco;

    public UpdateProdutoRequestDTO() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UpdateProdutoRequestDTO id(Long id) {
        setId(id);
        return this;
    }

    public UpdateProdutoRequestDTO nome(String nome) {
        setNome(nome);
        return this;
    }

    public UpdateProdutoRequestDTO descricao(String descricao) {
        setDescricao(descricao);
        return this;
    }

    public UpdateProdutoRequestDTO preco(BigDecimal preco) {
        setPreco(preco);
        return this;
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

}
