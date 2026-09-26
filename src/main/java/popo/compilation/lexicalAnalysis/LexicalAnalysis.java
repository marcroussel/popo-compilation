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
     * Renvoie le caractère suivant sans avancer la position, sans le consommer.
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
     * Consomme les espaces, tabulations et retours à la ligne
     */
    private void skipSpaces(){
        while (charPos < source.length() && Character.isWhitespace(peekNextChar())) {
            advanceChar();
        }
    }

    /**
     * (Description à changer)
     * Prise en compte du prochain Token,
     * en regardant tous les caractères du token, et en déduisant le type.
     * Gère correctement les tokens multi-caractères comme ==, !=, <=, >=, &&, ||
     */
    public void nextToken() {
        setLastToken(getCurrentToken());

        skipSpaces();

        // Si fin du flux source détecté,
        // On arrête cette fonction
        if (charPos >= source.length()) {
            setCurrentToken(new Token(ValidTokens.EOS, 0, line));
            return;
        }

        StringBuilder tokenBuilder = new StringBuilder();
        char firstChar = advanceChar();
        tokenBuilder.append(firstChar);

        // Si le premier caractère est alphanumérique, continuer à consommer les caractères alphanumériques
        if (isAlphaNumericChar(firstChar)) {
            while (charPos < source.length() && isAlphaNumericChar(peekNextChar())) {
                tokenBuilder.append(advanceChar());
            }
        } 
        // Si le premier caractère est un opérateur ou délimiteur, vérifier si c'est une combinaison de 2 caractères
        // en utilisant peekNextChar() pour les cas nécessitant de regarder le caractère suivant
        // ex: '=' seul vs '==', '<' seul vs '<=', '.' seul vs '.5' (nombre décimal), etc.
        else {
            char nextChar = peekNextChar();
            
            // Vérifier les combinaisons de 2 caractères d'opérateurs
            if ((firstChar == '=' && nextChar == '=') ||
                (firstChar == '!' && nextChar == '=') ||
                (firstChar == '<' && nextChar == '=') ||
                (firstChar == '>' && nextChar == '=') ||
                (firstChar == '&' && nextChar == '&') ||
                (firstChar == '|' && nextChar == '|')) {
                tokenBuilder.append(advanceChar());
            }
            // Cas spécial : si c'est un point suivi d'un chiffre, c'est un nombre décimal
            else if (firstChar == '.' && Character.isDigit(nextChar)) {
                // Consommer le point et les chiffres suivants
                while (charPos < source.length() && Character.isDigit(peekNextChar())) {
                    tokenBuilder.append(advanceChar());
                }
            }
        }

        String token = tokenBuilder.toString();

        System.out.println("Pos:" + this.charPos + " Line:" + this.line);
        System.out.println("Token found: " + token);

        
        // Lecture des tokens reconnaissables
        switch (token) {
            // Parenthèses et accolades
            case "(" :
                this.currentToken.setType(ValidTokens.LPAREN);
                break;
            case ")" :
                this.currentToken.setType(ValidTokens.RPAREN);
                break;
            case "{" :
                this.currentToken.setType(ValidTokens.LBRACE);
                break;
            case "}" :
                this.currentToken.setType(ValidTokens.RBRACE);
                break;
            case "[" :
                this.currentToken.setType(ValidTokens.LBRACKET);
                break;
            case "]" :
                this.currentToken.setType(ValidTokens.RBRACKET);
                break;
            
            // Reperages & delimiteurs
            case ":" :
                this.currentToken.setType(ValidTokens.COLON);
                break;
            case ";" :
                this.currentToken.setType(ValidTokens.SEMI);
                break;
            case "," :
                this.currentToken.setType(ValidTokens.COMMA);
                break;
            case "." :
                this.currentToken.setType(ValidTokens.DOT);
                break;
            case "\"" :
                this.currentToken.setType(ValidTokens.DQOT);
                break;
            case "'" :
                this.currentToken.setType(ValidTokens.SQOT);
                break;
            
            // Opérateurs arithmétiques
            case "=" :
                this.currentToken.setType(ValidTokens.ASSIGN);
                break;
            case "+" :
                this.currentToken.setType(ValidTokens.PLUS);
                break;
            case "-" :
                this.currentToken.setType(ValidTokens.MINUS);
                break;
            case "*" :
                this.currentToken.setType(ValidTokens.MUL);
                break;
            case "/" :
                this.currentToken.setType(ValidTokens.DIV);
                break;
            case "%" :
                this.currentToken.setType(ValidTokens.MOD);
                break;
            case "&" :
                this.currentToken.setType(ValidTokens.AMP);
                break;

            
            // Comparateurs d'égalité
            case "==" :
                this.currentToken.setType(ValidTokens.EQ);
                break;
            case "!=" :
                this.currentToken.setType(ValidTokens.NEQ);
                break;
            
            // Comparateurs
            case "<" :
                this.currentToken.setType(ValidTokens.LT);
                break;
            case "<=" :
                this.currentToken.setType(ValidTokens.LE);
                break;
            case ">" :
                this.currentToken.setType(ValidTokens.GT);
                break;
            case ">=" :
                this.currentToken.setType(ValidTokens.GE);
                break;
            
            // Opérateurs logiques
            case "&&" :
                this.currentToken.setType(ValidTokens.AND);
                break;
            case "||" :
                this.currentToken.setType(ValidTokens.OR);
                break;
            case "!" :
                this.currentToken.setType(ValidTokens.NOT);
                break;
            
        }

        // Prévoir une lecture de valeurs (chaînes de caractères et chiffres)

        this.currentToken.setLine(line);
    }

}
