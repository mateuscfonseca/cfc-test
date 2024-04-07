package org.mateus.resources.mappers;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.ext.ExceptionMapper;
import jakarta.ws.rs.ext.Provider;

@Provider
public class ConstraintViolationExceptionMapper implements ExceptionMapper<ConstraintViolationException> {

    @Override
    public Response toResponse(ConstraintViolationException exception) {
        StringBuilder errorMessage = new StringBuilder();
        for (ConstraintViolation<?> violation : exception.getConstraintViolations()) {
            errorMessage.append(violation.getMessage()).append("\n");
        }
        return Response.status(Response.Status.BAD_REQUEST)
                       .entity(errorMessage.toString())
                       .build();
    }
    
}
