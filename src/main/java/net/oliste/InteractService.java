package net.oliste;


import com.hexadevlabs.gpt4all.LLModel;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import java.util.List;
import java.util.Map;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class InteractService {

  @Inject
  JavaBlockVisitor javaBlockVisitor;

  @ConfigProperty(name = "model.path")
  String baseModelPath;

  @ConfigProperty(name = "model.file")
  String modelFilePath;

  private LLModel model;

  private LLModel.GenerationConfig config;

  @PostConstruct
  public void initModel() {
    java.nio.file.Path modelPath = java.nio.file.Path.of(baseModelPath, modelFilePath);
    model = new LLModel(modelPath);
    config = LLModel.config()
        .withNPredict(4096).build();
  }

  @PreDestroy
  public void cleanModel() throws Exception {
    model.close();
  }

  public String request(String content) {
    // Creates the object required by GPT4All to send the question
    final List<Map<String, String>> message = List.of(createMessage(content));

    // Sends the question to the model
    final LLModel.ChatCompletionResponse chatCompletionResponse =
        model.chatCompletion(message, config);

    var choices = chatCompletionResponse.choices;
    if (choices.isEmpty()) {
      throw new IllegalStateException("No Java code");
    }

    String text = choices.get(0).get("content");

    return parseResponse(text);
  }

  private String parseResponse(String text) {
    final Parser parser = Parser.builder().build();

    // Parse the content
    final Node node = parser.parse(text);

    // Navigate through the model, finding Java blocks
    node.accept(javaBlockVisitor);

    return javaBlockVisitor.getGeneratedSourceCode().orElseThrow(() -> new IllegalArgumentException("No code found"));
  }

  private Map<String, String> createMessage(String content) {
    return Map.of("role", "user", "content", content);
  }
}
