package net.oliste;

import io.quarkus.hibernate.reactive.panache.common.WithSession;
import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import java.util.stream.Collectors;

@Path("/interact")
@WithTransaction
@WithSession
public class InteractResource {

    @Inject
    InteractService interactService;

    @Inject
    EmbeddingsService embeddingsService;

    @Inject
    EmbeddingsRepository embeddingsRepository;

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

    @POST
    @Path("/article")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> addArticle(String content) {

        var art = new Article();
        art.setContent(content);
        art.setEmbedding(embeddingsService.embeddingsAsList(content));
        return embeddingsRepository.persistAndFlush(art).onItem().transformToUni((item) -> Uni.createFrom().item("OK"));

//        return Uni.createFrom().item("OK");
    }

    @POST
    @Path("/ask")
    @Consumes(MediaType.TEXT_PLAIN)
    @Produces(MediaType.TEXT_PLAIN)
    public Uni<String> ask(String content) {


        var emb = embeddingsService.embeddingsAsList(content);
        return embeddingsRepository.findSimilarity(emb).onItem().transformToUni((item) -> {
            var context = item.stream().map(Article::getContent).collect(Collectors.joining(","));
             var tt = "Based on the context: " + context + "\n Answer the question: "+ content;

            var result = interactService.request(tt);

              return Uni.createFrom().item(result);
            });

//        return Uni.createFrom().item("OK");
    }
}
