package popo.compilation.node;

import java.util.HashMap;

import popo.compilation.token.ValidTokens;

public class ValidNode {
    public static final HashMap<String, Integer> ValidTypes = new HashMap<>();
    static {
        ValidTypes.put("CONST", 0);
        ValidTypes.put("PLUS", 1);
        ValidTypes.put("MINUS", 2);
        ValidTypes.put("UNARY_MINUS", 30);
        ValidTypes.put("MUL", 3);
        ValidTypes.put("DIV", 4);
        ValidTypes.put("MOD", 5);
        ValidTypes.put("LT", 6);
        ValidTypes.put("GT", 7);
        ValidTypes.put("LE", 8);
        ValidTypes.put("GE", 9);
        ValidTypes.put("EQ", 10);
        ValidTypes.put("NEQ", 11);
        ValidTypes.put("ASSIGN", 12);
        ValidTypes.put("AND", 13);
        ValidTypes.put("OR", 14);
        ValidTypes.put("NOT", 15);
        ValidTypes.put("DEBUG", 16);
        ValidTypes.put("DROP", 17);
        ValidTypes.put("BLOCK", 18);
        ValidTypes.put("DECLARATION", 19);
        ValidTypes.put("REFERENCE", 20);
        ValidTypes.put("SEQUENCE", 21);
        ValidTypes.put("IDENT", 22);
    }
    public static final HashMap<ValidTokens, NodeInfo> OP = new HashMap<>(ValidTypes.size());
    static {
        //Prio : 7
        OP.put(ValidTokens.MUL, new NodeInfo(ValidTokens.MUL, 7, 1, 3));
        OP.put(ValidTokens.DIV, new NodeInfo(ValidTokens.DIV, 7, 1, 4));
        OP.put(ValidTokens.MOD, new NodeInfo(ValidTokens.MOD, 7, 1, 5));
        
        //Prio : 6
        OP.put(ValidTokens.PLUS, new NodeInfo(ValidTokens.PLUS, 6, 1, 1));
        OP.put(ValidTokens.MINUS, new NodeInfo(ValidTokens.MINUS, 6, 1, 2));
        
        //Prio : 5
        OP.put(ValidTokens.LT, new NodeInfo(ValidTokens.LT, 5, 1, 6));
        OP.put(ValidTokens.GT, new NodeInfo(ValidTokens.GT, 5, 1, 7));
        OP.put(ValidTokens.LE, new NodeInfo(ValidTokens.LE, 5, 1, 8));
        OP.put(ValidTokens.GE, new NodeInfo(ValidTokens.GE, 5, 1, 9));
        
        //Prio : 4
        OP.put(ValidTokens.EQ, new NodeInfo(ValidTokens.EQ, 4, 1, 10));
        OP.put(ValidTokens.NEQ, new NodeInfo(ValidTokens.NEQ, 4, 1, 11));
        
        //Prio : 3
        OP.put(ValidTokens.AND, new NodeInfo(ValidTokens.AND, 3, 1, 13));
        OP.put(ValidTokens.NOT, new NodeInfo(ValidTokens.NOT, 3, 1, 15));
        
        //Prio : 2
        OP.put(ValidTokens.OR, new NodeInfo(ValidTokens.OR, 2, 1, 14));
        
        //Prio : 1
        OP.put(ValidTokens.ASSIGN, new NodeInfo(ValidTokens.ASSIGN, 1, 0, 12));
    }
}