package popo.compilation.node;

import java.util.ArrayList;
import java.util.HashMap;

public class ValidNode {
    public static HashMap<String, Integer> ValidTypes = new HashMap<>();
    static {
        ValidTypes.put("CONST", 0);
        ValidTypes.put("PLUS", 1);
        ValidTypes.put("MINUS", 2);
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
    }
    public static ArrayList<NodeInfo> ValidNodeInfos = new ArrayList<>(ValidTypes.size());
    static {
        //Prio : 7
        ValidNodeInfos.add(new NodeInfo(ValidTypes.get("MUL"), 7, 1, 3));
        ValidNodeInfos.add(new NodeInfo(ValidTypes.get("DIV"), 7, 1, 4));
        ValidNodeInfos.add(new NodeInfo(ValidTypes.get("MOD"), 7, 1, 5));
        
        //Prio : 6
        ValidNodeInfos.add(new NodeInfo(ValidTypes.get("PLUS"), 6, 1, 1));
        ValidNodeInfos.add(new NodeInfo(ValidTypes.get("MINUS"), 6, 1, 2));
        
        //Prio : 5
        ValidNodeInfos.add(new NodeInfo(ValidTypes.get("LT"), 5, 1, 6));
        ValidNodeInfos.add(new NodeInfo(ValidTypes.get("GT"), 5, 1, 7));
        ValidNodeInfos.add(new NodeInfo(ValidTypes.get("LE"), 5, 1, 8));
        ValidNodeInfos.add(new NodeInfo(ValidTypes.get("GE"), 5, 1, 9));
        
        //Prio : 4
        ValidNodeInfos.add(new NodeInfo(ValidTypes.get("EQ"), 4, 1, 10));
        ValidNodeInfos.add(new NodeInfo(ValidTypes.get("NEQ"), 4, 1, 11));
        
        //Prio : 3
        ValidNodeInfos.add(new NodeInfo(ValidTypes.get("AND"), 3, 1, 13));
        
        //Prio : 2
        ValidNodeInfos.add(new NodeInfo(ValidTypes.get("OR"), 2, 1, 14));
        
        //Prio : 1
        ValidNodeInfos.add(new NodeInfo(ValidTypes.get("ASSIGN"), 1, 1, 12));
    
    }


}