package com.restaurant.config;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.container.PreMatching;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.ext.Provider;
import javax.ws.rs.core.Response;

@Provider
@PreMatching
public class CorsFilter implements ContainerRequestFilter, ContainerResponseFilter {

    private static final String ALLOWED_ORIGIN = "http://localhost:5173";
    private static final String ALLOWED_HEADERS = "origin, content-type, accept, authorization";
    private static final String ALLOWED_METHODS = "GET, POST, PUT, DELETE, OPTIONS, HEAD";
    private static final String MAX_AGE = "3600"; // 1 hora

    // Intercepta todas las requests
    @Override
    public void filter(ContainerRequestContext requestContext) throws IOException {
        // Si es preflight OPTIONS, devolver OK inmediatamente con headers
        if ("OPTIONS".equalsIgnoreCase(requestContext.getMethod())) {
            Response response = Response.ok()
                    .header("Access-Control-Allow-Origin", ALLOWED_ORIGIN)
                    .header("Access-Control-Allow-Headers", ALLOWED_HEADERS)
                    .header("Access-Control-Allow-Methods", ALLOWED_METHODS)
                    .header("Access-Control-Allow-Credentials", "true")
                    .header("Access-Control-Max-Age", MAX_AGE)
                    .build();
            requestContext.abortWith(response);
        }
    }

    // Agrega headers CORS a todas las respuestas normales
    @Override
    public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
            throws IOException {
        responseContext.getHeaders().putSingle("Access-Control-Allow-Origin", ALLOWED_ORIGIN);
        responseContext.getHeaders().putSingle("Access-Control-Allow-Headers", ALLOWED_HEADERS);
        responseContext.getHeaders().putSingle("Access-Control-Allow-Methods", ALLOWED_METHODS);
        responseContext.getHeaders().putSingle("Access-Control-Allow-Credentials", "true");
        responseContext.getHeaders().putSingle("Access-Control-Max-Age", MAX_AGE);
    }
}

