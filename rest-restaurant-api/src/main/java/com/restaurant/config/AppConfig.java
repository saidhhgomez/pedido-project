package com.restaurant.config;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import com.restaurant.resource.EmpleadoResource;
import com.restaurant.resource.ClienteResource;
import com.restaurant.resource.FileResource;

@ApplicationPath("/api")
public class AppConfig extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(EmpleadoResource.class); 
        resources.add(ClienteResource.class); 
        resources.add(FileResource.class);
        resources.add(MultiPartFeature.class); // Muy importante
        return resources;
    }
}