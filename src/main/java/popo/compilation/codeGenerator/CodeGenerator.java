package popo.compilation.codeGenerator;

import popo.compilation.semanticAnalysis.SemanticAnalysis;
import popo.compilation.node.Node;

public class CodeGenerator {

    public SemanticAnalysis semanticAnalysis;

    public CodeGenerator(String sourceFileName) {
        this.semanticAnalysis = new SemanticAnalysis(sourceFileName);
    }

    public void gencode() {
        Node A = this.semanticAnalysis.AnaSem(); // Génération de l'arbre d'analyse sémantique

        // Génération de code à venir
    }

    public void gennode(Node N) {
        switch (N.type) {
            case ND_CONST: // Attention, normalement on doit pas avoir le type prédéfini
                System.out.println("push " + N.value); // On pousse sur le sommet de la pile de la machine vrituelle
                break;
            //case ND_ADD:
            //    printf("add");   Le reste c'est pour après
            //    break;

            default:
                throw new CodeGenException("");
        }
    }
}
    

