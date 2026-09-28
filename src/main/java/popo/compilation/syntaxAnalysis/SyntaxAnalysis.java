package popo.compilation.syntaxAnalysis;

import popo.compilation.lexicalAnalysis.LexicalAnalysis;
import popo.compilation.node.Node;

public class SyntaxAnalysis {

    public LexicalAnalysis lexicalAnalysis;

    public SyntaxAnalysis(String sourceFileName) {
        this.lexicalAnalysis = new LexicalAnalysis(sourceFileName);
    }

    public Node AnaSyntax() {
        Node A = this.lexicalAnalysis.AnaLexic(); // Génération de l'arbre d'analyse lexicale
        return A;
    }
}
