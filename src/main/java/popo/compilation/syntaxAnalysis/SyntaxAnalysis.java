package popo.compilation.syntaxAnalysis;

import popo.compilation.lexicalAnalysis.LexicalAnalysis;
import popo.compilation.node.Node;
import popo.compilation.node.NodeInfo;
import popo.compilation.node.ValidNode;
import popo.compilation.token.ValidTokens;

public class SyntaxAnalysis {

    public LexicalAnalysis lexicalAnalysis;

    public SyntaxAnalysis(String sourceFileName) {
        this.lexicalAnalysis = new LexicalAnalysis(sourceFileName);
    }

    public Node AnaSyntax() {
        Node A = this.F(); // Génération de l'arbre d'analyse lexicale
        return A;
    }

    public Node F() {
        Node nIns = I();
        return nIns;
    }

    /* 
        Analyse syntaxique pour un nœud d'instruction 
    */
    public Node I() {


        if (this.lexicalAnalysis.check(ValidTokens.DEBUG)) {
            Node e = E();
            this.lexicalAnalysis.accept(ValidTokens.SEMI); // Manger le token ";"
            return new Node(ValidNode.ValidTypes.get("DEBUG"), e);
        }

        if (this.lexicalAnalysis.check(ValidTokens.LBRACE)) {
            Node N = new Node(ValidNode.ValidTypes.get("BLOCK"));
            while (!this.lexicalAnalysis.check(ValidTokens.RBRACE)) {
                N.addNode(I());
            }
            return N;
        }

        // Code de détection d'une déclaration de variable
        if (this.lexicalAnalysis.check(ValidTokens.INT)) {

            Node N = new Node(ValidNode.ValidTypes.get("SEQUENCE")); // Nœud séquence : un fils DECLARATION par variable

            // Création de nœuds DECLARATION, jusqu'à ne plus trouver de virgule
            do {
                // L'identificateur est lu avant d'être consommé, et de passer au token suivant
                String ident = this.lexicalAnalysis.currentToken.ident;
                int line = this.lexicalAnalysis.currentToken.line;
                this.lexicalAnalysis.accept(ValidTokens.IDENT);

                Node declaration = new Node(ValidNode.ValidTypes.get("DECLARATION"), ident);
                declaration.lineNumber = line;
                N.addNode(declaration);
            } while (this.lexicalAnalysis.check(ValidTokens.COMMA));

            this.lexicalAnalysis.accept(ValidTokens.SEMI); // Manger le token ";"
            return N;
        }

        // Ajouter le noeud drop au-dessus
        Node e = E(); // Parser le contenu de E
        this.lexicalAnalysis.accept(ValidTokens.SEMI); // Manger le token ";"
        return new Node(ValidNode.ValidTypes.get("DROP"), e);
    }

    /**
     * Analyse syntaxique pour un nœud d'expression
    */
    public Node E() {
        return EE(0);
    }

    /**
     * Analyse syntaxique pour un nœud d'expression nécessitant la gestion des priorités
     * @param pmin La priorité minimale pour l'analyse des expressions
     * @return Le nœud représentant l'expression analysée
     */
    public Node EE(int pmin) {
        Node a1 = P();
        while (true) {
            NodeInfo op = ValidNode.OP.get(this.lexicalAnalysis.currentToken.type);
            if (op == null || op.priority < pmin) {
                break;
            }
            int line = this.lexicalAnalysis.currentToken.line;
            this.lexicalAnalysis.next();
            Node a2 = EE(op.priority + op.associativity);
            a1 = new Node(op.NodeType, a1, a2);
            a1.lineNumber = line;
        }
        return a1;
    }

    /* 
        Analyse syntaxique pour un nœud primaire (constante, parenthèse, etc.)
    */
    public Node P() {

        // Cas où l'on détectera un moins unaire
        if (this.lexicalAnalysis.check(ValidTokens.MINUS)) {
            Node p = P();
            return new Node(ValidNode.ValidTypes.get("UNARY_MINUS"), p);
        }

        // Cas où l'on détectera un token Not
        if (this.lexicalAnalysis.check(ValidTokens.NOT)) {
            Node p = P();
            return new Node(ValidNode.ValidTypes.get("NOT"), p);
        }

        Node s = A();

        return s;
    }

    /* 
        Analyse syntaxique pour un nœud atomique (constante ou parenthèse)
    */
    public Node A() {

        // On consomme token si le token vérifié est bien une constante ou une parenthèse ouvrante
        // Mais si ça renvoie false, on consomme pas

        // Constante (A)
        if (this.lexicalAnalysis.check(ValidTokens.CONST)) {
            return new Node(ValidNode.ValidTypes.get("CONST"), this.lexicalAnalysis.lastToken.valeur); // On doit dans tous les cas renvoyer un arbre
        }

        // Identificateur : référence à une variable (A)
        // L'identificateur est lu avant d'être consommé
        if (this.lexicalAnalysis.currentToken.type == ValidTokens.IDENT) {
            Node ref = new Node(ValidNode.ValidTypes.get("REFERENCE"), this.lexicalAnalysis.currentToken.ident);
            ref.lineNumber = this.lexicalAnalysis.currentToken.line;
            this.lexicalAnalysis.next(); // Consommation de l'identificateur
            return ref;
        }

        // Expression entre parenthèses (( E ))
        else if (this.lexicalAnalysis.check(ValidTokens.LPAREN)) {
            Node e = E();
            this.lexicalAnalysis.accept(ValidTokens.RPAREN); // On vérifie qu'après récup de l'expression, on a bien une paranthèse fermante
            return e;
        }

        else {
            throw new SyntaxException("Constant, identifier or '(' expected at line " + this.lexicalAnalysis.currentToken.line);
        }
    }
}
