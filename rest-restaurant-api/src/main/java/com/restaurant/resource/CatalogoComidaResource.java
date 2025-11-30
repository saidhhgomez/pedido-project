package com.restaurant.resource;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.restaurant.model.CatalogoComida;
import com.restaurant.service.CatalogoComidaService;

@Path("/catalogo")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class CatalogoComidaResource {

    private CatalogoComidaService service = new CatalogoComidaService();

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CatalogoComida> listarCatalogo() {
        return service.listar();
    }
    
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public boolean agregar(CatalogoComida d) {
    return service.agregar(d);	
        }
    
    @PUT
    @Path("/{idCatalogo}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response actualizar(@PathParam("idCatalogo") int id, CatalogoComida comida) {
        boolean ok = service.actualizar(id, comida );
        if (ok) {
            return Response.ok("{\"mensaje\":\"CatalogoComida actualizado\"}").build();
        } else {
            return Response.status(Response.Status.NOT_FOUND).entity("{\"error\":\"El plato no encontrado o no se pudo actualizar\"}").build();
        }
    }
    @DELETE
    @Path("/{idCatalogo}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response eliminar(@PathParam("idCatalogo") int id) {
    	
                             
        boolean ok = service.eliminar(id);
        if (ok) {
            return Response.ok("{\"mensaje\":\"Plato eliminado correctamente\"}").build();
        }
        return Response.status(Response.Status.NOT_FOUND)
                       .entity("{\"error\":\"No se encontró el plato\"}").build();
    }
}
