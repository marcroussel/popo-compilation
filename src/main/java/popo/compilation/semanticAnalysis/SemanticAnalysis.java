package popo.compilation.semanticAnalysis;

import popo.compilation.node.Node;
import popo.compilation.syntaxAnalysis.SyntaxAnalysis;

public class SemanticAnalysis {

    public SyntaxAnalysis syntaxAnalysis;

    public SemanticAnalysis(String sourceFileName) {
        this.syntaxAnalysis = new SyntaxAnalysis(sourceFileName);
    }
    
    public Node AnaSem() {
        Node A = this.syntaxAnalysis.AnaSyntax(); // Génération de l'analyse syntaxique
        return A;
    }
}
