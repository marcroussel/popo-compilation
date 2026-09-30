package popo.compilation;

import popo.compilation.codeGenerator.CodeGenerator;
import popo.compilation.token.ValidTokens;


public class Main {

    public static CodeGenerator codeGenerator;

    public static void main(String[] args) {
        System.out.println(".start");
        init("/samples/petit_test.c");

        while (codeGenerator.semanticAnalysis.syntaxAnalysis.lexicalAnalysis.currentToken.type != ValidTokens.EOS) {
            codeGenerator.genCode();
        }
        System.out.println(".halt");
    }
    
    public static void init(String filePath) {
        codeGenerator = new CodeGenerator(filePath);
        codeGenerator.semanticAnalysis.syntaxAnalysis.lexicalAnalysis.next();
    }
}
