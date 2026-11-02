package popo.compilation.node;

import popo.compilation.debug.DebugLogger;

import java.util.ArrayList;


public class Node {
    public int type;        // ValidNode.ValidTypes.get("TYPE_NAME")
    public String value;
    public String Ident;    // si "IDENT"
    public int index;       // si "REF"

    public int lineNumber;

    public int nbChildren;
    public ArrayList<Node> children;

    public Node(int type) {
        this.type = type;
        this.nbChildren = 0;
        this.children = new ArrayList<>();
    }

    public Node (int type, int valeur) {
        this.type = type;
        this.value = String.valueOf(valeur);
        this.nbChildren = 0;
        this.children = new ArrayList<>();
    }

    public Node (int type, String ident) {
        this.type = type;
        this.Ident = ident;
        this.nbChildren = 0;
        this.children = new ArrayList<>();
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

    public void addNode (Node child) {
        this.nbChildren++;
        this.children.add(child);
    }

    /* ----- Fonctions statiques de débogage ----- */
    /**
     * Retourne le nom lisible d'un type de nœud (ex : 18 -> "BLOCK")
     */
    private static String nodeTypeName(int type) {
        for (var entry : ValidNode.ValidTypes.entrySet()) {
            if (entry.getValue() == type) {
                return entry.getKey();
            }
        }
        return "UNKNOWN";
    }

    /**
     * Affiche pour débogage un nœud : son type, puis ses enfants et leur type
     * @param N le nœud à inspecter
     */
    private static void debugNode(Node N) {
        if (N == null) {
            DebugLogger.log("[DEBUG] node = null");
            return;
        }

        DebugLogger.log("[DEBUG] node : " + nodeTypeName(N.type) + " (" + N.type + ")"
                + (N.Ident != null ? ", ident=" + N.Ident : "")
                + (N.value != null ? ", value=" + N.value : "")
                + ", line=" + N.lineNumber);

        if (N.children == null || N.children.isEmpty()) {
            DebugLogger.log("[DEBUG]   (no children)");
            return;
        }

        for (int i = 0; i < N.children.size(); i++) {
            Node c = N.children.get(i);
            DebugLogger.log("[DEBUG]   child " + i + " : " + nodeTypeName(c.type) + " (" + c.type + ")"
                    + (c.Ident != null ? ", ident=" + c.Ident : ""));
        }
    }
}
