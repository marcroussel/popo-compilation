package popo.compilation.semanticAnalysis;

public class SemanticException extends RuntimeException {
    public SemanticException(String message) {
        super("Semantic Error: " + message);
    }
}
