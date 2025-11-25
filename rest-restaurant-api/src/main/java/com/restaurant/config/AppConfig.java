package com.restaurant.config;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import com.restaurant.resource.ClienteResource;
import com.restaurant.resource.EmpleadoResource;

@ApplicationPath("/api")
public class AppConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        
        resources.add(CorsFilter.class);
        resources.add(EmpleadoResource.class);
        resources.add(ClienteResource.class);
        return resources;
    }
}
