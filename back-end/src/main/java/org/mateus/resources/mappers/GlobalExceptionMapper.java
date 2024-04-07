package org.mateus.resources.mappers;

import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class GlobalExceptionMapper implements ExceptionMapper<Exception> {

    @Override
    public Response toResponse(Exception exception) {
        exception.printStackTrace();
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                       .entity(exception.getMessage())
                       .build();
    }
    
}
