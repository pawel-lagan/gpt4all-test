package net.oliste;


import ai.djl.Application;
import ai.djl.MalformedModelException;
import ai.djl.inference.Predictor;
import ai.djl.repository.zoo.Criteria;
import ai.djl.repository.zoo.ModelNotFoundException;
import ai.djl.repository.zoo.ZooModel;
import ai.djl.training.util.ProgressBar;
import ai.djl.translate.TranslateException;
import ai.djl.util.Utils;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class EmbeddingsService {
  private static final Logger logger = LoggerFactory.getLogger(EmbeddingsService.class);

  @ConfigProperty(name = "model.path")
  String baseModelPath;

  @ConfigProperty(name = "model.embeddings-file")
  String embeddingsModelFilePath;

  private ZooModel<String[], float[][]> model;


  @PostConstruct
  public void initModel()
      throws IOException, ModelNotFoundException, MalformedModelException  {
    Criteria<String[], float[][]> criteria =
        Criteria.builder()
            .optApplication(Application.NLP.TEXT_EMBEDDING)
            .setTypes(String[].class, float[][].class)
            .optModelPath(Path.of(baseModelPath))
            .optModelName(embeddingsModelFilePath)
            .optTranslator(new EmbeddingsTranslator())
            .optEngine("TensorFlow")
            .optProgress(new ProgressBar())
            .build();

    model = criteria.loadModel();
  }

  @PreDestroy
  public void cleanModel() {
    model.close();
  }

  public float[][] embeddings(String content) {

    try(Predictor<String[], float[][]> predictor = model.newPredictor()) {
      var inputs = content.split("\n");
      var embeddings = predictor.predict(inputs);
      for (int i = 0; i < inputs.length; i++) {
        logger.info("Embedding for: {}\n", Arrays.toString(embeddings[i]));
      }
      return embeddings;
    } catch (TranslateException e) {
      throw new RuntimeException(e);
    }
  }
}
