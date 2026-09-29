package popo.compilation.node;

public class NodeInfo {
    public int TokenType;
    public int priority;
    public int associativity;
    public int NodeType;

    public NodeInfo(int TokenType, int priority, int associativity, int NodeType) {
        this.TokenType = TokenType;
        this.priority = priority;
        this.associativity = associativity;
        this.NodeType = NodeType;
    }

    public NodeInfo() {
        this.TokenType = 0;
        this.priority = 0;
        this.associativity = 0;
        this.NodeType = 0;
    }
}
