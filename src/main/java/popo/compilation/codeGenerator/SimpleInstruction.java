package popo.compilation.codeGenerator;

public class SimpleInstruction {
    public int NodeType;
    public String prefixe;
    public String suffixe;

    public SimpleInstruction(int NodeType, String prefixe, String suffixe) {
        this.NodeType = NodeType;
        this.prefixe = prefixe;
        this.suffixe = suffixe;
    }
}
