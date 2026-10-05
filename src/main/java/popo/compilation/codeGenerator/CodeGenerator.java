package popo.compilation.codeGenerator;

import java.util.HashMap;

import popo.compilation.node.Node;
import popo.compilation.semanticAnalysis.SemanticAnalysis;

public class CodeGenerator {

    public static final int DBG_CODE_VALUE = -1;
    public SemanticAnalysis semanticAnalysis;
    //Map
    public static final HashMap<Integer, SimpleInstruction> InstructionSimples = new HashMap<>(); 
    static {
        InstructionSimples.put(DBG_CODE_VALUE, new SimpleInstruction(DBG_CODE_VALUE, "", "dbg"));
        InstructionSimples.put(0, new SimpleInstruction(0, "", "push"));
        InstructionSimples.put(1, new SimpleInstruction(1, "", "add"));
        InstructionSimples.put(2, new SimpleInstruction(2, "", "sub"));
        InstructionSimples.put(3, new SimpleInstruction(3, "", "mul"));
        InstructionSimples.put(4, new SimpleInstruction(4, "", "div"));
        InstructionSimples.put(5, new SimpleInstruction(5, "", "mod"));
        InstructionSimples.put(30, new SimpleInstruction(30, "push 0", "sub"));
    }

    public CodeGenerator(String sourceFileName) {
        this.semanticAnalysis = new SemanticAnalysis(sourceFileName);
    }

    public void genCode() {
        Node A = this.semanticAnalysis.AnaSem(); // Génération de l'arbre d'analyse sémantique
        // Génération de code
        genNode(A);
        System.out.println(InstructionSimples.get(DBG_CODE_VALUE).suffixe + "\n");
    }

    public void genNode(Node N) {
        if(InstructionSimples.containsKey(N.type)) {
            String prefixe = InstructionSimples.get(N.type).prefixe;
            if (prefixe != null && !prefixe.isEmpty()) {
                System.out.println(prefixe);
            }
            for (int i = 0; i < N.nbChildren; i++) {
                genNode(N.children.get(i));
            }
            System.out.println(InstructionSimples.get(N.type).suffixe + " " + (N.value != null ? N.value : ""));
        }
        else{
            switch (N.type) {
                default -> throw new CodeGenException("Unhandled node type: " + N.type);
            }
        }
    }
}