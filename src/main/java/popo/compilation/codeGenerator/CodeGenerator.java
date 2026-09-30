package popo.compilation.codeGenerator;

import popo.compilation.node.Node;
import popo.compilation.semanticAnalysis.SemanticAnalysis;

public class CodeGenerator {

    public SemanticAnalysis semanticAnalysis;

    public CodeGenerator(String sourceFileName) {
        this.semanticAnalysis = new SemanticAnalysis(sourceFileName);
    }

    public void genCode() {
        Node A = this.semanticAnalysis.AnaSem(); // Génération de l'arbre d'analyse sémantique
        // Génération de code à venir
        genNode(A);
    }

    public void genNode(Node N) {
        switch (N.type) {
            case 0: // CONST
                System.out.println("push " + N.value); // On pousse sur le sommet de la pile de la machine vrituelle
                System.out.println("dbg"); // On pousse sur le sommet de la pile de la machine vrituelle
                break;
            //case ND_ADD:
            //    printf("add");   Le reste c'est pour après
            //    break;



            default:
                throw new CodeGenException("");
        }
    }
}
    

