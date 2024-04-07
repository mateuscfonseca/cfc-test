package org.mateus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.equalTo;

import java.math.BigDecimal;
import java.util.UUID;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.mateus.dtos.CreateProdutoRequestDTO;
import org.mateus.dtos.UpdateProdutoRequestDTO;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProdutoResourceTest {

    @Test
    @Order(1)
    public void testBuscarProdutoPorId() {
        Long produtoId = 1L;

        String endpointPath = "/produtos/{id}";

        given()
                .pathParam("id", produtoId)
                .when()
                .get(endpointPath)
                .then()
                .statusCode(200)
                .body("id", equalTo(produtoId.intValue()))
                .body("nome", equalTo("Produto A"))
                .body("descricao", equalTo("Descrição A"))
                .body("preco", equalTo(10.0f));
    }

    @Test
    @Order(2)
    public void testListarProdutosComPaginacao() {
        int page = 1;
        int pageSize = 2;

        String endpointPath = "/produtos";
        given()
                .queryParam("page", page)
                .queryParam("pageSize", pageSize)
                .when()
                .get(endpointPath)
                .then()
                .statusCode(200)
                .body("size()", equalTo(pageSize));
    }

    @Test
    @Order(3)
    public void testCriarProduto() {
        String nomeProduto = "Novo Produto " + UUID.randomUUID();
        String descricao = "Descrição Novo Produto";
        BigDecimal precoProduto = BigDecimal.valueOf(25.0);

        String endpointPath = "/produtos";

        given()
                .contentType(ContentType.JSON)
                .body(new CreateProdutoRequestDTO().nome(nomeProduto).descricao(descricao).preco(precoProduto))
                .when()
                .post(endpointPath)
                .then()
                .statusCode(201)
                .body("nome", equalTo(nomeProduto))
                .body("preco", equalTo(precoProduto.floatValue()));
        ;
    }

    @Test
    @Order(4)
    public void testAtualizarProduto() {
        Long idProduto = 1L;
        String novoNome = "Novo Nome do Produto";
        BigDecimal novoPreco = BigDecimal.valueOf(30.0);

        // Configura o caminho do endpoint para atualização
        String endpointPath = "/produtos/";

        // Realiza a requisição PUT para atualizar o produto e verifica se foi
        // atualizado corretamente
        given()
                .contentType(ContentType.JSON)
                .body(new UpdateProdutoRequestDTO().id(idProduto).nome(novoNome).preco(novoPreco))
                .when()
                .put(endpointPath)
                .then()
                .statusCode(200)
                .body("nome", equalTo(novoNome))
                .body("preco", equalTo(novoPreco.floatValue()));
    }

    @Test
    public void testDeletarProduto() {
        Long idProduto = 1L;

        String endpointPath = "/produtos/" + idProduto;

        given()
                .when()
                .delete(endpointPath)
                .then()
                .statusCode(204);
    }

    @Test
    public void testValidarNomeVazio() {
        String endpointPath = "/produtos";

        given()
                .contentType(ContentType.JSON)
                .body(new CreateProdutoRequestDTO().descricao("descricao").preco(BigDecimal.valueOf(10.0)))
                .when()
                .post(endpointPath)
                .then()
                .statusCode(400); // Verifica se o código de status é 400 Bad Request
    }

    @Test
    public void testValidarDescricaoVazia() {
        String endpointPath = "/produtos";

        given()
                .contentType(ContentType.JSON)
                .body(new CreateProdutoRequestDTO().nome("Nome").preco(BigDecimal.valueOf(10.0)))
                .when()
                .post(endpointPath)
                .then()
                .statusCode(400); // Verifica se o código de status é 400 Bad Request
    }

    @Test
    public void testValidarPrecoVazio() {
        String endpointPath = "/produtos";

        given()
                .contentType(ContentType.JSON)
                .body(new CreateProdutoRequestDTO().nome("Nome").descricao("descricao"))
                .when()
                .post(endpointPath)
                .then()
                .statusCode(400);
    }

    @Test
    public void testValidarPrecoInvalido() {
        String endpointPath = "/produtos";

        given()
                .contentType(ContentType.JSON)
                .body(new CreateProdutoRequestDTO().nome("Nome").descricao("descricao").preco(BigDecimal.valueOf(0)))
                .when()
                .post(endpointPath)
                .then()
                .statusCode(400);
    }

    @Test
    public void testBuscarProdutoPorIdQueNaoExiste() {
        Long idProdutoInexistente = 9999L;

        String endpointPath = "/produtos/" + idProdutoInexistente;

        given()
                .when()
                .get(endpointPath)
                .then()
                .statusCode(404);
    }

    @Test
    public void testAtualizarProdutoPorIdQueNaoExiste() {
        Long idProdutoInexistente = 9999L;

        String endpointPath = "/produtos/";

        given()
                .contentType(ContentType.JSON)
                .body(new UpdateProdutoRequestDTO().id(idProdutoInexistente).nome("novoNome")
                        .preco(BigDecimal.valueOf(10.0)))
                .when()
                .put(endpointPath)
                .then()
                .statusCode(404);
    }

    @Test
    public void testDeletarProdutoPorIdQueNaoExiste() {
        Long idProdutoInexistente = 9999L;

        String endpointPath = "/produtos/" + idProdutoInexistente;

        given()
                .when()
                .delete(endpointPath)
                .then()
                .statusCode(404);
    }
}