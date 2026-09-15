package popo.compilation.lexicalAnalysis;

public class Token {
    ValidTokens type;
    int valeur;
    String ident; // Pour stocker le nom de la variable (si on détecte une variable)
    int line;

    /**
     * Constructeur par défaut
     * @param type
     * @param valeur
     * @param line
     */
    public Token(ValidTokens type, int valeur, int line) {
        this.type = type;
        this.valeur = valeur;
        this.ident = null;
        this.line = line;
    }

    /**
     * Constructeur de tokens pour identificateurs
     * @param type
     * @param valeur
     * @param ident
     * @param line
     */
    public Token(ValidTokens type, int valeur, String ident, int line) {
        this.type = type;
        this.valeur = valeur;
        this.ident = ident;
        this.line = line;
    }
}
