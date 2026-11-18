package popo.compilation.codeGenerator;

public class CodeGenException extends RuntimeException {
    public CodeGenException(String message) {
        super("Code Generation Error: " + message);
    }
}
