package popo.compilation.node;

import java.util.ArrayList;


public class Node {
    public int type; // ValidNode.ValidTypes.get("TYPE_NAME")
    public String value;
    public String Ident;    // si ValidNode.IDENT

    public int lineNumber;

    public int nbChildren;
    public ArrayList<Node> children;

    public Node(int type) {
        this.type = type;
    }

    public Node (int type, int valeur) {
        this.type = type;
        this.value = String.valueOf(valeur);
    }

    public Node (int type, String ident) {
        this.type = type;
        this.Ident = ident;
    }

    public Node (int type, Node child1) {
        this.type = type;
        this.nbChildren = 1;
        this.children = new ArrayList<Node>() {
            {
                add(child1);
            }
        };
    }

    public Node (int type, Node child1, Node child2) {
        this.type = type;
        this.nbChildren = 2;
        this.children = new ArrayList<Node>()  {
            {
                add(child1);
                add(child2);
            }
        };
    }

    public static void addNode (Node parent, Node child) {
        parent.nbChildren++;
        parent.children.add(child);
    }

}
