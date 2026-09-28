package popo.compilation;

import popo.compilation.codeGenerator.CodeGenerator;
import popo.compilation.lexicalAnalysis.LexicalAnalysis;
import popo.compilation.token.ValidTokens;


public class Main {

    public static CodeGenerator codeGenerator;

    public static void main(String[] args) {

        CodeGenerator codeGenerator = new CodeGenerator("/samples/petit_test.c");
        codeGenerator.gencode();

        try {
            LexicalAnalysis lexicalAnalysis = new LexicalAnalysis("/samples/petit_test.c");
            lexicalAnalysis.init();

            while (lexicalAnalysis.currentToken.type != ValidTokens.EOS) {
                codeGenerator.gencode();
            }
        } catch (Exception e) {
            System.out.println("ERROR : " + e.toString());
        }
    }
}
