package net.oliste;

import io.quarkus.hibernate.reactive.panache.PanacheRepository;
import io.quarkus.panache.common.Parameters;
import io.quarkus.panache.common.Sort;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import org.hibernate.reactive.mutiny.Mutiny.SessionFactory;

@ApplicationScoped
public class EmbeddingsRepository implements PanacheRepository<Article> {
  @Inject
  SessionFactory sessionFactory;
  // put your custom logic here as instance methods
  public Uni<List<Article>> findSimilarity(List<Double> embeddings){
    var conv = new VectorConverter();
    return sessionFactory.withSession(session -> {
      return session.createNativeQuery("SELECT * FROM article ORDER BY embedding <-> :embeddings LIMIT 5", Article.class)
          .setParameter("embeddings", conv.convertToDatabaseColumn(embeddings))
          .getResultList();
    });
  }
}
