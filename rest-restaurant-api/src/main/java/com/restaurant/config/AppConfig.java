package com.restaurant.config;

import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import org.glassfish.jersey.media.multipart.MultiPartFeature;

import com.restaurant.resource.EmpleadoResource;
import com.restaurant.resource.CatalogoComidaResource;
import com.restaurant.resource.ClienteResource;
import com.restaurant.resource.DireccionClienteResource;
import com.restaurant.resource.LoginResource;
import com.restaurant.resource.MesaResource;
import com.restaurant.resource.MetodoPagoResource;
import com.restaurant.resource.RolResource;
import com.restaurant.resource.SucursalResource;
import com.restaurant.resource.TipoContratoResource;

@ApplicationPath("/api")
public class AppConfig extends Application {
    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new HashSet<>();
        resources.add(EmpleadoResource.class); 
        resources.add(CatalogoComidaResource.class); 
        resources.add(LoginResource.class); 
        resources.add(DireccionClienteResource.class); 
        resources.add(MetodoPagoResource.class); 
        resources.add(RolResource.class); 
        resources.add(SucursalResource.class); 
        resources.add(TipoContratoResource.class); 
        resources.add(ClienteResource.class); 
        resources.add(MesaResource.class); 
        resources.add(MultiPartFeature.class); // Muy importante
        return resources;
    }
}