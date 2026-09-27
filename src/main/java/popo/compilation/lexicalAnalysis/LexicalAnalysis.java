package popo.compilation.lexicalAnalysis;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class LexicalAnalysis {

    // Caractères pouvant suivre un token sans faire partie de celui-ci
    // (ex: "42;" ou "42)" sont valides, le ';'/')' n'appartient pas au nombre)
    private static final Set<Character> CLOSING_CHARS = Set.of(')', ']', '}', ';', ',');

    // Code source
    private final String source;

    // Tokens courant et dernier vu
    private Token currentToken;
    private Token lastToken;

    // Position de lecture dans le flux source et ligne courante
    private int charPos = 0;
    private int line = 1;

    // Map des tokens multi-caractères
    private static final Map<String, ValidTokens> TWO_CHAR_TOKENS = new HashMap<>();

    static {
        TWO_CHAR_TOKENS.put("==", ValidTokens.EQ);
        TWO_CHAR_TOKENS.put("!=", ValidTokens.NEQ);
        TWO_CHAR_TOKENS.put("<=", ValidTokens.LE);
        TWO_CHAR_TOKENS.put(">=", ValidTokens.GE);
        TWO_CHAR_TOKENS.put("&&", ValidTokens.AND);
        TWO_CHAR_TOKENS.put("||", ValidTokens.OR);
    }


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
        System.out.println("Construction des Tokens");
        setCurrentToken(new Token(null, 0, 0));

        System.out.println("Number of characters of source: " + source.length());

        while (charPos < source.length() && getCurrentToken().getType() != ValidTokens.EOS) {
            System.out.println("");
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
     * Vérifie si deux caractères consécutifs forment un token valide multi-caractères
     * en cherchant dans la Map TWO_CHAR_TOKENS
     * @param firstChar Le premier caractère
     * @param secondChar Le deuxième caractère
     * @return Le ValidTokens correspondant, ou null si ce n'est pas une combinaison valide
     */
    private ValidTokens getTwoCharToken(char firstChar, char secondChar) {
        String twoCharToken = String.valueOf(firstChar) + secondChar;
        return TWO_CHAR_TOKENS.get(twoCharToken);
    }

    /**
     * Détermination du prochain Token,
     * en regardant tous les caractères du token, et en déduisant le type.
     * Gère correctement les tokens multi-caractères comme ==, !=, <=, >=, &&, ||
     * ainsi que les caractères alphanumériques
     */
    public void nextToken() {
        setLastToken(new Token(getCurrentToken()));

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

        if (!isAlphaNumericChar(firstChar)) {
            System.out.println("Non alphanumeric token detected");

            // Si le premier caractère est un opérateur ou délimiteur, vérifier si c'est une combinaison de 2 caractères
            // en utilisant peekNextChar() pour les cas nécessitant de regarder le caractère suivant
            // ex: '=' seul vs '==', '<' seul vs '<=', etc.
            char nextChar = peekNextChar();

            // Vérifier si c'est une combinaison de 2 caractères valide en utilisant ValidTokens
            ValidTokens twoCharTokenType = getTwoCharToken(firstChar, nextChar);
            if (twoCharTokenType != null) {
                tokenBuilder.append(advanceChar());

                // Créer directement le token avec le type détecté par la Map
                this.currentToken.setType(twoCharTokenType);
                this.currentToken.setLine(line);

                System.out.println("Pos:" + this.charPos + " Line:" + this.line);
                System.out.println("Token found: " + tokenBuilder.toString());

                return;
            }

            // TODO : Potentiellement à retirer - À confirmer avec le prof
            // Cas spécial : si c'est un point suivi d'un chiffre, c'est un nombre décimal
            /*
            else if (firstChar == '.' && Character.isDigit(nextChar)) {
                // Consommer le point et les chiffres suivants
                while (charPos < source.length() && Character.isDigit(peekNextChar())) {
                    tokenBuilder.append(advanceChar());
                }
            }
             */

            String token = tokenBuilder.toString();

            System.out.println("Pos:" + this.charPos + " Line:" + this.line);
            System.out.println("Token found: " + token);

            // Lecture des tokens simples (caractères uniques et identifiants)
            switch (token) {
                // Parenthèses et accolades
                case "(":
                    this.currentToken.setType(ValidTokens.LPAREN);
                    break;
                case ")":
                    this.currentToken.setType(ValidTokens.RPAREN);
                    break;
                case "{":
                    this.currentToken.setType(ValidTokens.LBRACE);
                    break;
                case "}":
                    this.currentToken.setType(ValidTokens.RBRACE);
                    break;
                case "[":
                    this.currentToken.setType(ValidTokens.LBRACKET);
                    break;
                case "]":
                    this.currentToken.setType(ValidTokens.RBRACKET);
                    break;

                // Reperages & delimiteurs
                case ":":
                    this.currentToken.setType(ValidTokens.COLON);
                    break;
                case ";":
                    this.currentToken.setType(ValidTokens.SEMI);
                    break;
                case ",":
                    this.currentToken.setType(ValidTokens.COMMA);
                    break;
                case ".":
                    this.currentToken.setType(ValidTokens.DOT);
                    break;
                case "\"":
                    this.currentToken.setType(ValidTokens.DQOT);
                    break;
                case "'":
                    this.currentToken.setType(ValidTokens.SQOT);
                    break;

                // Opérateurs arithmétiques
                case "=":
                    this.currentToken.setType(ValidTokens.ASSIGN);
                    break;
                case "+":
                    this.currentToken.setType(ValidTokens.PLUS);
                    break;
                case "-":
                    this.currentToken.setType(ValidTokens.MINUS);
                    break;
                case "*":
                    this.currentToken.setType(ValidTokens.MUL);
                    break;
                case "/":
                    this.currentToken.setType(ValidTokens.DIV);
                    break;
                case "%":
                    this.currentToken.setType(ValidTokens.MOD);
                    break;
                case "&":
                    this.currentToken.setType(ValidTokens.AMP);
                    break;

                // Comparateurs
                case "<":
                    this.currentToken.setType(ValidTokens.LT);
                    break;
                case ">":
                    this.currentToken.setType(ValidTokens.GT);
                    break;

                // Opérateurs logiques
                case "!":
                    this.currentToken.setType(ValidTokens.NOT);
                    break;
                // Lorsqu'un caractère inconnu a été détecté,
                // On lève une erreur
                default:
                    throw new LexicalException(String.format("Unrecognized character: '%s' at line %d", token, this.line));
            }

            this.currentToken.setLine(line);
        }

        // Si le token est numérique
        else if (Character.isDigit(firstChar)) {
            System.out.println("Digital token detected");

            // Récupération de tous les chiffres de la valeur numérique
            while (charPos < source.length() && Character.isDigit(peekNextChar())) {
                tokenBuilder.append(advanceChar());
            }

            // On lève une erreur si le caractère suivant n'est ni un espace,
            // ni un caractère de fermeture (';', ')', ... ), ni la fin du flux
            char afterDigits = peekNextChar();
            if (!Character.isWhitespace(afterDigits) && afterDigits != '\0' && !CLOSING_CHARS.contains(afterDigits)) {
                throw new LexicalException(String.format("Invalid token: '%s' at line %d", tokenBuilder.toString(), this.line));
            }

            // Sinon, on a bien une valeur numérique valide
            // (le caractère de fermeture éventuel n'est pas consommé ici,
            // il sera lu comme son propre token au prochain appel)
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
        else if (Character.isAlphabetic(firstChar)) {

            System.out.println("Alphabetic token detected");

            // Récupération des autres caractères, tant qu'ils sont alphanumériques
            // ou des underscores
            while (charPos < source.length() && isAlphaNumericChar(peekNextChar())) {
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

        // Pour tous les autres cas
        else {
            throw new LexicalException(String.format("Unrecognized character: '%c' at line %d", firstChar, this.line));
        }
    }
}
