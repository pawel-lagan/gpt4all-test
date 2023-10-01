package net.oliste;

import jakarta.enterprise.context.ApplicationScoped;
import org.commonmark.node.AbstractVisitor;
import org.commonmark.node.FencedCodeBlock;
import java.util.Optional;

@ApplicationScoped
class JavaBlockVisitor extends AbstractVisitor {
  private String sourceCode;

  public Optional<String> getGeneratedSourceCode() {
    return Optional.ofNullable(sourceCode);
  }


  @Override
  public void visit(FencedCodeBlock code) {
    if ("java".equals(code.getInfo())) {
      // Get the content of the block
      sourceCode = code.getLiteral();
    }
  }
}