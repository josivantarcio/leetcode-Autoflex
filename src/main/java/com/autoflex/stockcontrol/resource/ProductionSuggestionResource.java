package com.autoflex.stockcontrol.resource;

import com.autoflex.stockcontrol.dto.ProductionSuggestionDTO;
import com.autoflex.stockcontrol.service.ProductionSuggestionService;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/api/production-suggestions")
@Produces(MediaType.APPLICATION_JSON)
public class ProductionSuggestionResource {

    @Inject
    ProductionSuggestionService service;

    @GET
    public ProductionSuggestionDTO suggest() {
        return service.suggest();
    }
}
