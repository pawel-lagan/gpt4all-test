package net.oliste;

import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/interact")
public class InteractResource {

    @Inject
    InteractService interactService;

    @Inject
    EmbeddingsService embeddingsService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String greet() {
        return "Hello";
    }

    @POST
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String interact(String content) {
        return interactService.request(content);
    }

    @POST
    @Path("/embeddings")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public String embeddings(String content) {
        return embeddingsService.embeddings(content).toString();
    }
}
