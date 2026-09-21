package popo.compilation.lexicalAnalysis;

public class LexicalAnalysis {

    // Code source
    private final String source;

    // Tokens courant et dernier vu
    private Token currentToken;
    private Token lastToken;

    // Position de lecture dans le flux source et ligne courante
    private int charPos = 0;
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
        if (charPos >= source.length()) {
            return '\0';
        }
        return source.charAt(charPos);
    }

    /**
     * Consomme et renvoie le caractère courant, en avançant la position.
     * Met à jour le compteur de ligne si on passe un retour à la ligne.
     */
    private char advanceChar() {
        char c = source.charAt(charPos);
        charPos++;

        // Si retour à la ligne détectée → nouvelle ligne
        if (c == '\n') {
            line++;
        }
        return c;
    }

    /**
     * Détermine si le caractère est alphanumérique (underscore toléré)
     * et ne contient pas d'espace.
     * @param c Le caractère à évaluer
     * @return Vrai si le caractère est alphanumérique ou underscore, Faux sinon
     */
    private boolean isAlphaNumericChar(char c) {
        return (Character.isAlphabetic(c) || Character.isDigit(c) || c == '_') && !Character.isWhitespace(c) ;
    }

    /**
     * (Description à changer)
     * Prise en compte du prochain Token,
     * en regardant tous les caractères du token, et en déduisant le type
     */
    public void nextToken() {
        setLastToken(getCurrentToken());

        // On ignore les espaces, tabulations et retours à la ligne
        while (charPos < source.length() && Character.isWhitespace(peekNextChar())) {
            advanceChar();
        }

        // Si fin du flux source détecté,
        // On arrête cette fonction
        if (charPos >= source.length()) {
            setCurrentToken(new Token(ValidTokens.EOS, 0, line));
            return;
        }

        // Construction du token tant que les caractères
        // suivants sont alphanumériques
        StringBuilder tokenBuilder = new StringBuilder();
        do {
            tokenBuilder.append(advanceChar());
        } while (charPos < source.length() && isAlphaNumericChar(peekNextChar()));

        String token = tokenBuilder.toString();

        System.out.println("Pos:" + this.charPos + " Line:" + this.line);
        System.out.println("Token found: " + token);

        // (utiliser peekNextChar() pour les cas nécessitant de regarder le caractère suivant,
        // ex: '=' seul vs '==', '<' seul vs '<=', un chiffre suivi d'autres chiffres, etc.)

        switch (token) {
            case "(" :
                this.currentToken.setType(ValidTokens.LPAREN);
                break;
            case ")" :
                this.currentToken.setType(ValidTokens.RPAREN);
                break;
        }

        this.currentToken.setLine(line);
    }

}
