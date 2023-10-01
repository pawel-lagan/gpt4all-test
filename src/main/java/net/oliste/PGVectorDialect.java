package net.oliste;

import io.quarkus.runtime.annotations.RegisterForReflection;
import org.hibernate.dialect.PostgreSQLDialect;
@RegisterForReflection
public class PGVectorDialect extends PostgreSQLDialect {
    public PGVectorDialect() {
      super();
      registerKeyword("<=>");
      registerKeyword("<->");
    }
}
