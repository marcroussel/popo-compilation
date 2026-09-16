package popo.compilation.lexicalAnalysis;

public class LexicalAnalysis {

    // Code source
    private final String source;

    // Tokens courant et dernier vu
    private Token currentToken;
    private Token lastToken;

    // Position de lecture dans le flux source et ligne courante
    private int pos = 0;
    private int line = 1;

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

        System.out.println("Number of characters of source: " + source.length());

        // A faire boucler
        nextToken();
    }

    /**
     * Renvoie le caractère courant sans avancer la position, sans le consommer.
     * Renvoie '\0' si on est à la fin du flux.
     */
    private char peekNextChar() {
        if (pos >= source.length()) {
            return '\0';
        }
        return source.charAt(pos);
    }

    /**
     * Consomme et renvoie le caractère courant, en avançant la position.
     * Met à jour le compteur de ligne si on passe un retour à la ligne.
     */
    private char advanceChar() {
        char c = source.charAt(pos);
        pos++;

        // Si retour à la ligne détectée → nouvelle ligne
        if (c == '\n') {
            line++;
        }
        return c;
    }

    /**
     * (Description à changer)
     * Prise en compte du prochain Token,
     * en regardant tous les caractères du token, et en déduisant le type
     */
    public void nextToken() {
        setLastToken(getCurrentToken());

        // On ignore les espaces, tabulations et retours à la ligne
        while (pos < source.length() && Character.isWhitespace(peekNextChar())) {
            advanceChar();
        }

        // Fin du flux source
        if (pos >= source.length()) {
            setCurrentToken(new Token(ValidTokens.EOS, 0, line));
            return;
        }

        char c = advanceChar();

        System.out.println("Pos:" + this.pos + " Line:" + this.line);
        System.out.println("Character found: " + c);
        System.out.println("Next character found: " + peekNextChar());

        // TODO: switch (c) { ... } pour déterminer le type de token
        // (utiliser peek() pour les cas nécessitant de regarder le caractère suivant,
        // ex: '=' seul vs '==', '<' seul vs '<=', un chiffre suivi d'autres chiffres, etc.)
    }

}
