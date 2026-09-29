package popo.compilation.node;

import java.util.ArrayList;

public class Node {
    public int type; // ValidNode.ValidTypes
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

    public void addNode (Node parent, Node child) {
        parent.nbChildren++;
        Node[] newChildren = new Node[parent.nbChildren];
        System.arraycopy(parent.children, 0, newChildren, 0, parent.children.size());
        newChildren[parent.nbChildren - 1] = child;
        parent.children = new ArrayList<Node>() {
            {
                for (Node n : newChildren) {
                    add(n);
                }
            }
        };
    }

}
