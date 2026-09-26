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

        while (charPos < source.length() && getCurrentToken().getType() != ValidTokens.EOS) {
            nextToken();
        }
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
        setLastToken(new Token(getCurrentToken()));

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

        /*
        // Construction du token tant que les caractères
        // suivants sont alphanumériques
        StringBuilder tokenBuilder = new StringBuilder();
        do {
            tokenBuilder.append(advanceChar());
        } while (charPos < source.length() && isAlphaNumericChar(peekNextChar()));


        String token = tokenBuilder.toString();
        */

        // Lecture du prochain
        char token = advanceChar();

        System.out.println("Pos:" + this.charPos + " Line:" + this.line);
        System.out.println("Token found: " + token);

        // (utiliser peekNextChar() pour les cas nécessitant de regarder le caractère suivant,
        // ex: '=' seul vs '==', '<' seul vs '<=', un chiffre suivi d'autres chiffres, etc.)

        // Lecture des tokens reconnaissables (pour Mathéo)
        /*
        switch (token) {
            case "(" :
                this.currentToken.setType(ValidTokens.LPAREN);
                break;
            case ")" :
                this.currentToken.setType(ValidTokens.RPAREN);
                break;
        }
         */

        // Si le token est numérique
        if (Character.isDigit(token)) {
            System.out.println("Digital token detected");

            // Récupération de tous les chiffres de la valeur numérique
            StringBuilder tokenBuilder = new StringBuilder(String.valueOf(token));
            while (charPos < source.length() && Character.isDigit(peekNextChar())){
                tokenBuilder.append(advanceChar());
            }

            // On lève une erreur si le caractère suivant n'est pas un espace
            // TODO : Laisser passer des caractères de fermeture (parenthèse fermante, point-virgule, etc...)
            if (!Character.isWhitespace(peekNextChar())) {
                throw new LexicalException(String.format("Invalid token: '%s' at line %d", tokenBuilder.toString(), this.line));
            }

            // Si le caractère est un espace,
            // alors on estime qu'on a une valeur numérique
            else {
                this.currentToken.setType(ValidTokens.CONST);
                this.currentToken.setValeur(Integer.parseInt(tokenBuilder.toString()));
            }

            this.currentToken.setLine(line);

            // Pour tester
            System.out.println("Token found: " + this.currentToken.getType());
            if (this.currentToken.getType() == ValidTokens.CONST) {
                System.out.println("with value: " + this.currentToken.getValeur());
            }
        }

        // Si le token est alphabétique
        else if (Character.isAlphabetic(token)) {

            System.out.println("Alphabetic token detected");

            StringBuilder tokenBuilder = new StringBuilder(String.valueOf(token));
            while (charPos < source.length() && isAlphaNumericChar(peekNextChar())){
                tokenBuilder.append(advanceChar());
            }

            String charToken = tokenBuilder.toString();

            System.out.println("Total token found: " + charToken);

            // Détection de mots clés
            switch (charToken) {
                case "if":
                    this.currentToken.setType(ValidTokens.IF);
                    break;
                case "else":
                    this.currentToken.setType(ValidTokens.ELSE);
                    break;
                case "for":
                    this.currentToken.setType(ValidTokens.FOR);
                    break;
                case "while":
                    this.currentToken.setType(ValidTokens.WHILE);
                    break;
                case "do":
                    this.currentToken.setType(ValidTokens.DO);
                    break;
                case "int":
                    this.currentToken.setType(ValidTokens.INT);
                    break;
                case "void":
                    this.currentToken.setType(ValidTokens.VOID);
                    break;
                case "continue":
                    this.currentToken.setType(ValidTokens.CONTINUE);
                    break;
                case "break":
                    this.currentToken.setType(ValidTokens.BREAK);
                    break;
                case "return":
                    this.currentToken.setType(ValidTokens.RETURN);
                    break;
                default:
                    // Aucun mot-clé reconnu : on considère qu'il s'agit d'un identificateur
                    this.currentToken.setType(ValidTokens.IDENT);
                    this.currentToken.setIdent(charToken);
                    break;
            }

            this.currentToken.setLine(line);

            // Pour tester
            System.out.println("Token found: " + this.currentToken.getType());
            if (this.currentToken.getType() == ValidTokens.IDENT) {
                System.out.println("with value: " + this.currentToken.getIdent());
            }
        }

        // Si un caractère inconnu a été détecté
        else {
            throw new LexicalException(String.format("Unrecognized character: '%c' at line %d", token, this.line));
        }
    }
}
