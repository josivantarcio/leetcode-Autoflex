package com.autoflex.stockcontrol.resource;

import com.autoflex.stockcontrol.dto.ProductRawMaterialDTO;
import com.autoflex.stockcontrol.service.ProductRawMaterialService;
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

import java.util.List;

@Path("/api/product-raw-materials")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ProductRawMaterialResource {

    @Inject
    ProductRawMaterialService service;

    @GET
    public List<ProductRawMaterialDTO> listAll() {
        return service.listAll();
    }

    @GET
    @Path("/product/{productId}")
    public List<ProductRawMaterialDTO> findByProductId(@PathParam("productId") Long productId) {
        return service.findByProductId(productId);
    }

    @POST
    public Response create(@Valid ProductRawMaterialDTO dto) {
        ProductRawMaterialDTO created = service.create(dto);
        return Response.status(Response.Status.CREATED).entity(created).build();
    }

    @PUT
    @Path("/{id}")
    public ProductRawMaterialDTO update(@PathParam("id") Long id, @Valid ProductRawMaterialDTO dto) {
        return service.update(id, dto);
    }

    @DELETE
    @Path("/{id}")
    public Response delete(@PathParam("id") Long id) {
        service.delete(id);
        return Response.noContent().build();
    }
}
