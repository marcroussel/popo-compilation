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

    Node F() {
        Node nIns = I();
        return nIns;
    }


    Node I() {
        Node nExp = E(); // Parser le contenu de E
        this.lexicalAnalysis.accept(ValidTokens.SEMI); // "Manger" le token ";"

        return nExp;
    }

    Node E() {
        Node sousArbre = A();
        return sousArbre;
    }

    Node A() {

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
