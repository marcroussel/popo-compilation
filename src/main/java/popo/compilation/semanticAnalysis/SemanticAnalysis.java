package popo.compilation.semanticAnalysis;
import popo.compilation.node.Node;
import popo.compilation.symbol.SymbolTable;
import popo.compilation.syntaxAnalysis.SyntaxAnalysis;

public class SemanticAnalysis {

    public SyntaxAnalysis syntaxAnalysis;
    public SymbolTable symbolTable;

    public SemanticAnalysis(String sourceFileName) {
        this.syntaxAnalysis = new SyntaxAnalysis(sourceFileName);
        this.symbolTable = new SymbolTable();
    }
    
    public Node AnaSem() {
        Node A = this.syntaxAnalysis.AnaSyntax(); // Génération de l'arbre d'analyse syntaxique
        return A;
    }
}
