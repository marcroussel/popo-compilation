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
        InstructionSimples.put(0, new SimpleInstruction(0, "", "push"));
        InstructionSimples.put(1, new SimpleInstruction(1, "", "add"));
        InstructionSimples.put(2, new SimpleInstruction(2, "", "sub"));
        InstructionSimples.put(3, new SimpleInstruction(3, "", "mul"));
        InstructionSimples.put(4, new SimpleInstruction(4, "", "div"));
        InstructionSimples.put(5, new SimpleInstruction(5, "", "mod"));
        InstructionSimples.put(6, new SimpleInstruction(6, "", "cmplt"));
        InstructionSimples.put(7, new SimpleInstruction(7, "", "cmpgt"));
        InstructionSimples.put(8, new SimpleInstruction(8, "", "cmple"));
        InstructionSimples.put(9, new SimpleInstruction(9, "", "cmpge"));
        InstructionSimples.put(10, new SimpleInstruction(10, "", "cmpeq"));
        InstructionSimples.put(11, new SimpleInstruction(11, "", "cmpne"));
        //InstructionSimples.put(12, new SimpleInstruction(12, "", ""));
        InstructionSimples.put(13, new SimpleInstruction(13, "", "and"));
        InstructionSimples.put(14, new SimpleInstruction(14, "", "or"));
        InstructionSimples.put(15, new SimpleInstruction(15, "", "not"));
        InstructionSimples.put(16, new SimpleInstruction(16, "", "dbg"));
        InstructionSimples.put(17, new SimpleInstruction(17, "", "drop 1"));
        InstructionSimples.put(30, new SimpleInstruction(30, "push 0", "sub"));
    }

    public CodeGenerator(String sourceFileName) {
        this.semanticAnalysis = new SemanticAnalysis(sourceFileName);
    }
    /* 
        Génération de code à partir de l'arbre d'analyse sémantique 
    */
    public void genCode() {
        Node A = this.semanticAnalysis.AnaSem(); // Génération de l'arbre d'analyse sémantique
        System.out.println("res n" + this.semanticAnalysis.nbvar);
        // Génération de code
        genNode(A);
    }

    /* 
        Génération de code pour un nœud spécifique de l'arbre d'analyse sémantique 
    */
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
                case 12: // ASSIGN
                    genNode(N.children.get(0)); // ND_CONST ou ND_REF
                    System.out.println("dup"); // On duplique la valeur à assigner pour la garder sur la pile
                    System.out.println("set " + N.children.get(0).index); // On assigne la valeur à la variable
                    break;
                
                case 18: // BLOCK
                    for (int i = 0; i < N.nbChildren; i++) {
                        genNode(N.children.get(i));
                    }
                    break;

                case  20: // REF
                    System.out.println("get " + N.index); 
                    break;

                case 21: // SEQUENCE
                    break;

                default : throw new CodeGenException("Unhandled node type: " + N.type);
            }
        }
    }
}