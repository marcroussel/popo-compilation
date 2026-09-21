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

    public ValidTokens getType() {
        return type;
    }

    public void setType(ValidTokens type) {
        this.type = type;
    }

    public int getValeur() {
        return valeur;
    }

    public void setValeur(int valeur) {
        this.valeur = valeur;
    }

    public String getIdent() {
        return ident;
    }

    public void setIdent(String ident) {
        this.ident = ident;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }
}
