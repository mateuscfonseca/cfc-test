package org.mateus.resources;

import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.jboss.resteasy.reactive.RestQuery;
import org.mateus.dtos.CreateProdutoRequestDTO;
import org.mateus.dtos.UpdateProdutoRequestDTO;
import org.mateus.service.ProdutoService;
import org.mateus.service.exceptions.ProdutoNaoEncontradoException;

import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.DELETE;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

@Path("/produtos")
public class ProdutoResource {

    private final ProdutoService produtoService;

    @Inject
    public ProdutoResource(ProdutoService produtoService) {
        this.produtoService = produtoService;
    }

    @Path("{id}")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Busca um produto por id", description = "Busca um produto por id, caso não encontre retorna 404")
    public Response buscar(@PathParam("id") Long id) {
        try {
            final var response = this.produtoService.buscarPorId(id);
            return Response.ok(response).build();
        } catch (ProdutoNaoEncontradoException e) {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Lista produtos de forma paginada", description = "Lista produtos de forma paginada, parametro page começa em 1")
    public Response listar(@RestQuery("page") int page, @RestQuery("pageSize") int pageSize,
            @RestQuery("sortByColumn") String sortByColumn, @RestQuery("sortByAsceding") Boolean sortByAsceding) {

        final var response = this.produtoService.listarPaginado(page, pageSize, sortByColumn, sortByAsceding);
        return Response.ok(response).build();

    }

    @Path("/todos")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Lista todos os produtos", description = "Lista todos os produtos")
    public Response listarTodos() {

        final var response = this.produtoService.listarTodos();
        return Response.ok(response).build();

    }

    @Path("/total")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Operation(summary = "Traz o total de produtos cadastrados")
    public Response total() {

        final var response = this.produtoService.total();
        return Response.ok(response).build();

    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Retry(maxRetries = 2)
    @Operation(summary = "Cria um novo produto")
    public Response criar(@Valid CreateProdutoRequestDTO dto) {
        final var response = this.produtoService.criar(dto);
        return Response.status(Status.CREATED).entity(response).build();
    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Operation(summary = "Atualiza um produto", description = "Atributos vazios são desconsiderados, exceto o ID que é obrigatório")
    @Retry(maxRetries = 2)
    public Response atualizar(@Valid UpdateProdutoRequestDTO dto) {
        try {
            final var response = this.produtoService.update(dto);

            return Response.ok(response).build();
        } catch (ProdutoNaoEncontradoException e) {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

    @Path("{id}")
    @DELETE
    @Retry(maxRetries = 2)
    public Response delete(Long id) {
        try {
            this.produtoService.delete(id);
            return Response.status(Status.NO_CONTENT).build();
        } catch (ProdutoNaoEncontradoException e) {
            return Response.status(Status.NOT_FOUND).build();
        }
    }

}
