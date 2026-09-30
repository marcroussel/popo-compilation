package popo.compilation.codeGenerator;

import java.util.HashMap;

import popo.compilation.node.Node;
import popo.compilation.node.ValidNode;
import popo.compilation.semanticAnalysis.SemanticAnalysis;

public class CodeGenerator {

    public SemanticAnalysis semanticAnalysis;
    //Map
    public static final HashMap<Integer, SimpleInstruction> InstructionSimples = new HashMap<>(); 
    static {
        InstructionSimples.put(-1, new SimpleInstruction(0, "", "dbg"));
        InstructionSimples.put(0, new SimpleInstruction(0, "", "push"));
        InstructionSimples.put(1, new SimpleInstruction(1, "", "add"));
        InstructionSimples.put(2, new SimpleInstruction(2, "", "sub"));
        InstructionSimples.put(3, new SimpleInstruction(3, "", "mul"));
        InstructionSimples.put(30, new SimpleInstruction(30, "push 0", "sub"));
    }

    public CodeGenerator(String sourceFileName) {
        this.semanticAnalysis = new SemanticAnalysis(sourceFileName);
    }

    public void genCode() {
        Node A = this.semanticAnalysis.AnaSem(); // Génération de l'arbre d'analyse sémantique
        // Génération de code à venir
        genNode(A);
    }

    public void genNode(Node N) {
        if(ValidNode.OP.get(N.type) != null) {
            System.out.println(InstructionSimples.get(N.type).prefixe);
            for (int i = 0; i < N.nbChildren; i++) {
                genNode(N.children.get(i));
            }
            System.out.println(InstructionSimples.get(N.type).suffixe);
        }
        switch (N.type) {
            case 1000:
                break;
            case 1001:
                break;
            default:
                throw new CodeGenException("");
        }
    }
}