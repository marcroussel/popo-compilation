package popo.compilation.codeGenerator;

import popo.compilation.semanticAnalysis.SemanticAnalysis;
import popo.compilation.node.Node;

public class CodeGenerator {

    public SemanticAnalysis semanticAnalysis;

    public CodeGenerator(String sourceFileName) {
        this.semanticAnalysis = new SemanticAnalysis(sourceFileName);
    }

    public Node gencode() {
        Node A = this.semanticAnalysis.AnaSem();

        // Génération de code à venir
    }
}
    

