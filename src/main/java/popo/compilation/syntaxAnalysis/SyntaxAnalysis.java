package popo.compilation.syntaxAnalysis;

import popo.compilation.lexicalAnalysis.LexicalAnalysis;
import popo.compilation.node.Node;
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


    public Node I() {
        Node nExp = E(); // Parser le contenu de E
        this.lexicalAnalysis.accept(ValidTokens.SEMI); // "Manger" le token ";"

        return nExp;
    }

    public Node E(int pmin) {
        Node a1 = P();
        // while (OP[courant.type] != null) {
        while (ValidNode.ValidNodeInfos.get()) // Tu en es là bg
            op = OP[courant.type];
            if (op.hasPrio(pmin)) {
                break;
            }
            this.lexicalAnalysis.next();
            Node a2 = EE(op.prio++); // On cherche des opérateurs plus prioritaires que l'opérateur actuel
            a1 = node_2(op.nd, a1, a2); // On crée un noeud de l'opérateur avec comme enfants a1 et a2
        }
        return a1;
    }

    public Node E() {
        return E(0);
    }

    public Node P() {

        // Cas où l'on détectera un moins unaire
        if (this.lexicalAnalysis.check(ValidTokens.MINUS)) {
            Node p = P();
            return new Node(nd_moins_un, p);
        }

        // Même chose pour le token Not

        // Cas pour un chiffre
        Node s = S();

        return s;
    }


    public Node A() {

        // On consomme token si le token vérifié est bien une constante ou une parenthèse ouvrante
        // Mais si ça renvoie false, on consomme pas

        // Constante (A)
        // TODO : nd_const à définir
        if (this.lexicalAnalysis.check(ValidTokens.CONST)) {
            return new Node(ValidNode.ValidTypes.get("CONST"), this.lexicalAnalysis.lastToken.valeur); // On doit dans tous les cas renvoyer un arbre
        }

        // ( E )
        else if (this.lexicalAnalysis.check(ValidTokens.LPAREN)) {
            Node e = E();
            this.lexicalAnalysis.accept(ValidTokens.RPAREN); // On vérifie qu'après récup de l'expression, on a bien une paranthèse fermante
            return e;
        }

        else {
            throw new SyntaxException("Constant value or '(' expected at line " + this.lexicalAnalysis.currentToken.line);
        }
    }
}
