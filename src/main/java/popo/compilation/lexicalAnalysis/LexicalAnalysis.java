package popo.compilation.lexicalAnalysis;

public class LexicalAnalysis {

    private final String source;
    private Token currentToken;
    private Token lastToken;

    public LexicalAnalysis(String source) {
        this.source = source;
    }

    public Token getCurrentToken() {
        return currentToken;
    }

    private void setCurrentToken(Token currentToken) {
        this.currentToken = currentToken;
    }

    public Token getLastToken() {
        return lastToken;
    }

    private void setLastToken(Token lastToken) {
        this.lastToken = lastToken;
    }

    /* ------------------------------------------------------------------------------------- */

    /**
     * Initialise l'analyse lexicale
     */
    public void init() {
        // System.out.print(source);

        // System.out.println();
        System.out.println("Construction des Tokens");

        setCurrentToken(new Token(null, 0, 0));

        next();
    }

    public void next() {
        setLastToken(getCurrentToken());

        // Prévoir une lecture du caractère jusqu'au prochain espace

        // Prévoir un cas d'ignore des espaces

    }

}
