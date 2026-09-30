package popo.compilation.node;

import popo.compilation.token.ValidTokens;

public class NodeInfo {
    public ValidTokens TokenType;
    public int priority;
    public int associativity;
    public int NodeType;

    public NodeInfo(ValidTokens TokenType, int priority, int associativity, int NodeType) {
        this.TokenType = TokenType;
        this.priority = priority;
        this.associativity = associativity;
        this.NodeType = NodeType;
    }

    public NodeInfo() {
        this.TokenType = null; // Assuming NONE is a valid default value in ValidTokens
        this.priority = 0;
        this.associativity = 0;
        this.NodeType = 0;
    }
}
