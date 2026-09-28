package popo.compilation.syntaxAnalysis;

import popo.compilation.lexicalAnalysis.LexicalAnalysis;
import popo.compilation.node.Node;

public class SyntaxAnalysis {

    public LexicalAnalysis lexicalAnalysis;

    public SemanticAnalysis(String sourceFileName) {
        this.lexicalAnalysis = new LexicalAnalysis(sourceFileName);
    }

    public Node AnaSem() {
        Node A = this.syntaxAnalysis.AnaSyntax(); // Génération de l'analyse syntaxique
        return A;
    }
}
