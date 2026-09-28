package popo.compilation.SyntaxAcceptor;

public class Node {
    public ValidNode type;
    public String value;
    public String Ident;    // si ValidNode.IDENT

    public int lineNumber;

    public int nbChildren;
    public Node[] children;

    public Node(int type) {
        this.type = ValidNode.;
    }

    public Node (int type, int valeur) {
        this.type = ValidNode.;
        this.value = ;
    }

    public Node (int type, Node child1) {
        this.type = ValidNode.;
        this.nbChildren = 1;
        this.children = new Node[]{child1};
    }

    public Node (int type, Node child1, Node child2) {
        this.type = ValidNode.;
        this.nbChildren = 2;
        this.children = new Node[]{child1, child2};
    }

    public void addNode (Node parent, Node child) {
        parent.nbChildren++;
        Node[] newChildren = new Node[parent.nbChildren];
        System.arraycopy(parent.children, 0, newChildren, 0, parent.children.length);
        newChildren[parent.nbChildren - 1] = child;
        parent.children = newChildren;
    }

}
