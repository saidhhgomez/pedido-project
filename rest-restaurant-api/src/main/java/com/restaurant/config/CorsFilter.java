package com.restaurant.config;

import java.io.IOException;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.ext.Provider;

@Provider
public class CorsFilter implements ContainerResponseFilter {

    @Override
    public void filter(ContainerRequestContext requestContext,
                       ContainerResponseContext responseContext) throws IOException {

        responseContext.getHeaders().putSingle(
            "Access-Control-Allow-Origin", "http://localhost:5173"
        );
        responseContext.getHeaders().putSingle(
            "Access-Control-Allow-Headers",
            "Origin, Content-Type, Accept, Authorization"
        );
        responseContext.getHeaders().putSingle(
            "Access-Control-Allow-Methods",
            "GET, POST, PUT, DELETE, OPTIONS"
        );
        responseContext.getHeaders().putSingle(
            "Access-Control-Allow-Credentials", "true"
        );
        responseContext.getHeaders().putSingle(
            "Access-Control-Max-Age", "3600"
        );
    }
}
