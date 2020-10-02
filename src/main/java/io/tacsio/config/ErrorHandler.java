package io.tacsio.config;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import java.util.HashMap;
import java.util.Map;

@Provider
public class ErrorHandler implements ExceptionMapper<Exception> {
    @Override
    public Response toResponse(Exception exception) {
        Map<String, String> error = new HashMap<>();
        error.put("error", exception.getMessage());

        return Response
                .status(Response.Status.BAD_REQUEST)
                .entity(error)
                .build();
    }
}
