package popo.compilation.lexicalAnalysis;

import popo.compilation.token.Token;
import popo.compilation.token.ValidTokens;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public class LexicalAnalysis {

    // Caractères pouvant suivre un token sans faire partie de celui-ci
    // (ex: "42;" ou "42)" sont valides, le ';'/')' n'appartient pas au nombre)
    public static final Set<Character> CLOSING_CHARS = Set.of(')', ']', '}', ';', ',');

    // Code source
    public final String source;

    // Tokens courant et dernier vu
    public Token currentToken;
    public Token lastToken;

    // Position de lecture dans le flux source et ligne courante
    public int charPos = 0;
    public int line = 1;

    // Map des tokens multi-caractères
    public static final Map<String, ValidTokens> TWO_CHAR_TOKENS = new HashMap<>();

    static {
        TWO_CHAR_TOKENS.put("==", ValidTokens.EQ);
        TWO_CHAR_TOKENS.put("!=", ValidTokens.NEQ);
        TWO_CHAR_TOKENS.put("<=", ValidTokens.LE);
        TWO_CHAR_TOKENS.put(">=", ValidTokens.GE);
        TWO_CHAR_TOKENS.put("&&", ValidTokens.AND);
        TWO_CHAR_TOKENS.put("||", ValidTokens.OR);
    }

    /**
     * Constructeur de l'analyseur lexical.
     * Convertit le fichier source en un String,
     * et charge le premier token dans currentToken.
     * @param sourceFileName : Le chemin vers le fichier source
     */
    public LexicalAnalysis(String sourceFileName) {
        this.source = readSource(sourceFileName);
    }

    /**
     * Lit le fichier source et le convertit en String.
     * Cherche d'abord sur le système de fichiers (cas d'un chemin passé en argument
     * de l'exécutable), puis dans le classpath (ex: "/samples/petit_test.c",
     * qui se trouve dans src/main/resources).
     * @param sourceFileName : Le chemin vers le fichier source
     * @return Le contenu du fichier
     */
    private static String readSource(String sourceFileName) {
        try {
            Path path = Path.of(sourceFileName);
            if (Files.isRegularFile(path)) {
                return Files.readString(path, StandardCharsets.UTF_8);
            }

            try (InputStream inputStream = LexicalAnalysis.class.getResourceAsStream(sourceFileName)) {
                if (inputStream == null) {
                    throw new FileNotFoundException("Source file not found: " + sourceFileName);
                }
                return new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
            }
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    /* ------------------------------------------------------------------------------------- */

    /**
     * Initialise l'analyse lexicale
     */
    public void init() {
        // prend un nom de fichier source et initialise l'analyse lexicale
        System.out.println("Construction des Tokens");
        this.currentToken = new Token(null, 0, 0);

        System.out.println("Number of characters of source: " + source.length());

        while (charPos < source.length() && this.currentToken.type != ValidTokens.EOS) {
            System.out.println("");
            Token tokenToAdd = new Token(this.currentToken);
            tokens.add(tokenToAdd);
            System.out.println("Adding current token: " + this.currentToken.type);
        }
        //Accept(tokens);
    }

    /**
     * Renvoie le caractère suivant sans avancer la position, sans le consommer.
     * Renvoie '\0' si on est à la fin du flux.
     */
    public char peekNextChar() {
        if (charPos >= source.length()) {
            return '\0';
        }
        return source.charAt(charPos);
    }

    /**
     * Consomme et renvoie le caractère courant, en avançant la position.
     * Met à jour le compteur de ligne si on passe un retour à la ligne.
     */
    public char advanceChar() {
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
    public boolean isAlphaNumericChar(char c) {
        return (Character.isAlphabetic(c) || Character.isDigit(c) || c == '_') && !Character.isWhitespace(c) ;
    }

    /**
     * Consomme les espaces, tabulations et retours à la ligne
     */
    public void skipSpaces(){
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
    public ValidTokens getTwoCharToken(char firstChar, char secondChar) {
        String twoCharToken = String.valueOf(firstChar) + secondChar;
        return TWO_CHAR_TOKENS.get(twoCharToken);
    }

    /**
     * Détermination du prochain Token,
     * en regardant tous les caractères du token, et en déduisant le type.
     * Gère correctement les tokens multi-caractères comme ==, !=, <=, >=, &&, ||
     * ainsi que les caractères alphanumériques
     */
    public void next() {
        this.lastToken = new Token(this.currentToken);

        skipSpaces();

        // Si fin du flux source détecté,
        // On arrête cette fonction
        if (charPos >= source.length()) {
            this.currentToken = new Token(ValidTokens.EOS, 0, line);
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
                this.currentToken.type = twoCharTokenType;
                this.currentToken.line = line;

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
                    this.currentToken.type = ValidTokens.LPAREN;
                    break;
                case ")":
                    this.currentToken.type = ValidTokens.RPAREN;
                    break;
                case "{":
                    this.currentToken.type = ValidTokens.LBRACE;
                    break;
                case "}":
                    this.currentToken.type = ValidTokens.RBRACE;
                    break;
                case "[":
                    this.currentToken.type = ValidTokens.LBRACKET;
                    break;
                case "]":
                    this.currentToken.type = ValidTokens.RBRACKET;
                    break;

                // Reperages & delimiteurs
                case ":":
                    this.currentToken.type = ValidTokens.COLON;
                    break;
                case ";":
                    this.currentToken.type = ValidTokens.SEMI;
                    break;
                case ",":
                    this.currentToken.type = ValidTokens.COMMA;
                    break;
                case ".":
                    this.currentToken.type = ValidTokens.DOT;
                    break;
                case "\"":
                    this.currentToken.type = ValidTokens.DQOT;
                    break;
                case "'":
                    this.currentToken.type = ValidTokens.SQOT;
                    break;

                // Opérateurs arithmétiques
                case "=":
                    this.currentToken.type = ValidTokens.ASSIGN;
                    break;
                case "+":
                    this.currentToken.type = ValidTokens.PLUS;
                    break;
                case "-":
                    this.currentToken.type = ValidTokens.MINUS;
                    break;
                case "*":
                    this.currentToken.type = ValidTokens.MUL;
                    break;
                case "/":
                    this.currentToken.type = ValidTokens.DIV;
                    break;
                case "%":
                    this.currentToken.type = ValidTokens.MOD;
                    break;
                case "&":
                    this.currentToken.type = ValidTokens.AMP;
                    break;

                // Comparateurs
                case "<":
                    this.currentToken.type = ValidTokens.LT;
                    break;
                case ">":
                    this.currentToken.type = ValidTokens.GT;
                    break;

                // Opérateurs logiques
                case "!":
                    this.currentToken.type = ValidTokens.NOT;
                    break;
                // Lorsqu'un caractère inconnu a été détecté,
                // On lève une erreur
                default:
                    throw new LexicalException(String.format("Unrecognized character: '%s' at line %d", token, this.line));
            }
            System.out.println("Current token type: " + this.currentToken.type);
            this.currentToken.line = line ;
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
                this.currentToken.type = ValidTokens.CONST;
                this.currentToken.valeur = Integer.parseInt(tokenBuilder.toString());
            }
            this.currentToken.line = line;

            // Pour tester
            System.out.println("Token found: " + this.currentToken.type);
            if (this.currentToken.type == ValidTokens.CONST) {
                System.out.println("with value: " + this.currentToken.valeur);
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
                    this.currentToken.type = ValidTokens.IF;
                    break;
                case "else":
                    this.currentToken.type = ValidTokens.ELSE;
                    break;
                case "for":
                    this.currentToken.type = ValidTokens.FOR;
                    break;
                case "while":
                    this.currentToken.type = ValidTokens.WHILE;
                    break;
                case "do":
                    this.currentToken.type = ValidTokens.DO;
                    break;
                case "int":
                    this.currentToken.type = ValidTokens.INT;
                    break;
                case "void":
                    this.currentToken.type = ValidTokens.VOID;
                    break;
                case "continue":
                    this.currentToken.type = ValidTokens.CONTINUE;
                    break;
                case "break":
                    this.currentToken.type = ValidTokens.BREAK;
                    break;
                case "return":
                    this.currentToken.type = ValidTokens.RETURN;
                    break;
                default:
                    // Aucun mot-clé reconnu : on considère qu'il s'agit d'un identificateur
                    this.currentToken.type = ValidTokens.IDENT;
                    this.currentToken.ident = charToken;
                    break;
            }

            this.currentToken.line = line;

            // Pour tester
            System.out.println("Token found: " + this.currentToken.type);
            if (this.currentToken.type == ValidTokens.IDENT) {
                System.out.println("with value: " + this.currentToken.ident);
            }
        }

        // Pour tous les autres cas
        else {
            throw new LexicalException(String.format("Unrecognized character: '%c' at line %d", firstChar, this.line));
        }
    }
    boolean check(ValidTokens type){
        if (currentToken.type == type) {
            next();
            return true;
        }

        return false;
    }

    void accept(ValidTokens type) throws LexicalException {
        if (currentToken.type != type) {
            throw new LexicalException(
                    String.format("Token '%s' expected, but '%s' found at line %d", type, currentToken.type, this.line)
            );
        }
    }
}