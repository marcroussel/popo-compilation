package popo.compilation.SyntaxAcceptor;
import java.util.ArrayList;

import popo.compilation.lexicalAnalysis.LexicalException;
import popo.compilation.lexicalAnalysis.Token;
import popo.compilation.lexicalAnalysis.ValidTokens;

public class SyntaxAcceptor {
    ArrayList<Token> tokens;

    public SyntaxAcceptor(ArrayList<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Vérifie qu'un bloc ouvert (parenthèse, accolade, crochet) est correctement fermé
     * @param startIndex L'indice du token d'ouverture
     * @param openType Le type de token d'ouverture (LPAREN, LBRACE, LBRACKET)
     * @return L'indice du token après la fermeture
     * @throws LexicalException si le bloc n'est pas correctement fermé
     */
    private int acceptBlock(int startIndex, ValidTokens openType) {
        // Déterminer le type de fermeture correspondant
        ValidTokens closeType;
        if (openType == ValidTokens.LPAREN) closeType = ValidTokens.RPAREN;
        else if (openType == ValidTokens.LBRACE) closeType = ValidTokens.RBRACE;
        else if (openType == ValidTokens.LBRACKET) closeType = ValidTokens.RBRACKET;
        else throw new IllegalArgumentException("Invalid open type");
        
        // Vérifier que le token à startIndex est du type openType
        if (startIndex >= tokens.size() || tokens.get(startIndex).getType() != openType) {
            throw new LexicalException("Expected '" + getSymbol(openType) + "' at position " + startIndex);
        }
        
        // Compter les ouvertures/fermetures
        int count = 1;
        int i = startIndex + 1;
        while (i < tokens.size() && count > 0) {
            if (tokens.get(i).getType() == openType) count++;
            else if (tokens.get(i).getType() == closeType) count--;
            i++;
        }
        
        if (count != 0) {
            throw new LexicalException("Unmatched '" + getSymbol(openType) + "'");
        }
        
        return i; // Indice après la fermeture
    }

    /**
     * Convertit un ValidTokens en symbole lisible
     */
    private String getSymbol(ValidTokens type) {
        if (type == ValidTokens.LPAREN) return "(";
        else if (type == ValidTokens.RPAREN) return ")";
        else if (type == ValidTokens.LBRACE) return "{";
        else if (type == ValidTokens.RBRACE) return "}";
        else if (type == ValidTokens.LBRACKET) return "[";
        else if (type == ValidTokens.RBRACKET) return "]";
        return "?";
    }

    /**
     * Accepte une déclaration simple terminée par un point-virgule
     * (ex: break; ou continue;)
     * @param keywordType Le type de mot-clé attendu (BREAK, CONTINUE, etc.)
     * @throws LexicalException si la structure n'est pas respectée
     */

    // modifier car points virgules mal détectés
    private void acceptSimpleStatement(ValidTokens keywordType) {
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() == keywordType) {
                // Vérifier le point-virgule
                if (i + 1 >= tokens.size() || tokens.get(i + 1).getType() != ValidTokens.SEMI) {
                    throw new LexicalException("Expected ';' after '" + getKeywordName(keywordType) + "'");
                }
            }
        }
    }

    /**
     * Retourne le nom lisible d'un mot-clé
     */
    private String getKeywordName(ValidTokens type) {
        switch (type) {
            case BREAK: return "break";
            case CONTINUE: return "continue";
            case RETURN: return "return";
            case IF: return "if";
            case WHILE: return "while";
            case FOR: return "for";
            case DO: return "do";
            case INT: return "int";
            case VOID: return "void";
            default: return "?";
        }
    }

    /**
     * Helper récursif pour traiter if/else if/else à partir d'un index donné
     */
    private int acceptIfRecursive(int index) {
        if (index >= tokens.size() || tokens.get(index).getType() != ValidTokens.IF) {
            return index;
        }
        
        int j = index + 1;
        
        // Vérifier '('
        if (j >= tokens.size() || tokens.get(j).getType() != ValidTokens.LPAREN) {
            throw new LexicalException("Expected '(' after 'if'");
        }
        j = acceptBlock(j, ValidTokens.LPAREN);
        
        // Vérifier '{'
        if (j >= tokens.size() || tokens.get(j).getType() != ValidTokens.LBRACE) {
            throw new LexicalException("Expected '{' after 'if' condition");
        }
        j = acceptBlock(j, ValidTokens.LBRACE);
        
        // Vérifier 'else' ou 'else if'
        if (j < tokens.size() && tokens.get(j).getType() == ValidTokens.ELSE) {
            j++;
            
            if (j < tokens.size() && tokens.get(j).getType() == ValidTokens.IF) {
                // C'est un else if - appel récursif
                return acceptIfRecursive(j);
            } else if (j < tokens.size() && tokens.get(j).getType() == ValidTokens.LBRACE) {
                // C'est un else simple
                j = acceptBlock(j, ValidTokens.LBRACE);
                
            } else {
                throw new LexicalException("Expected '{' or 'if' after 'else'");
            }
        }
        
        return j;
    }

    public boolean acceptIf() {
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() == ValidTokens.IF) {
                acceptIfRecursive(i);
            }
        }
        return true;
    }

    /**
     * Accepte une structure de contrôle while.
     * Structure attendue: while (condition) { block }
     * @return true si la validation réussit
     * @throws LexicalException si la structure n'est pas respectée
     */
    public boolean acceptWhile() {
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() == ValidTokens.WHILE) {
                // Vérifier: while (condition) { block }
                if (i + 1 >= tokens.size() || tokens.get(i + 1).getType() != ValidTokens.LPAREN) {
                    throw new LexicalException("Expected '(' after 'while'");
                }
                int j = acceptBlock(i + 1, ValidTokens.LPAREN);
                
                // Vérifier l'accolade ouvrante
                if (j >= tokens.size() || tokens.get(j).getType() != ValidTokens.LBRACE) {
                    throw new LexicalException("Expected '{' after 'while' condition");
                }
                j = acceptBlock(j, ValidTokens.LBRACE);
            }
        }
        return true;
    }

    /**
     * Accepte une structure de contrôle for.
     * Structure attendue: for (init; condition; update) { block }
     * @return true si la validation réussit
     * @throws LexicalException si la structure n'est pas respectée
     */
    public boolean acceptFor() {
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() == ValidTokens.FOR) {
                // Vérifier: for (init; condition; update) { block }
                if (i + 1 >= tokens.size() || tokens.get(i + 1).getType() != ValidTokens.LPAREN) {
                    throw new LexicalException("Expected '(' after 'for'");
                }
                
                // Vérifier que la clause 'for' contient 2 points-virgules
                int semiCount = 0;
                int parenCount = 1;
                int j = i + 2;
                while (j < tokens.size() && parenCount > 0) {
                    if (tokens.get(j).getType() == ValidTokens.LPAREN) parenCount++;
                    else if (tokens.get(j).getType() == ValidTokens.RPAREN) parenCount--;
                    else if (tokens.get(j).getType() == ValidTokens.SEMI && parenCount == 1) {
                        semiCount++;
                    }
                    j++;
                }
                if (semiCount != 2) {
                    throw new LexicalException("Expected 2 semicolons in 'for' clause");
                }
                // acceptBlock retourne l'indice après la ')', et on l'a déjà fait
                // Donc on va directement à j qui pointe après la ')'
                j = i + 1;
                j = acceptBlock(j, ValidTokens.LPAREN);
                
                // Vérifier l'accolade ouvrante
                if (j >= tokens.size() || tokens.get(j).getType() != ValidTokens.LBRACE) {
                    throw new LexicalException("Expected '{' after 'for' clause");
                }
                j = acceptBlock(j, ValidTokens.LBRACE);
            }
        }
        return true;
    }

    public boolean acceptDo() {
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() == ValidTokens.DO) {
                // Vérifier: do { block } while (condition);
                if (i + 1 >= tokens.size() || tokens.get(i + 1).getType() != ValidTokens.LBRACE) {
                    throw new LexicalException("Expected '{' after 'do'");
                }
                
                int j = acceptBlock(i + 1, ValidTokens.LBRACE);
                
                // Vérifier 'while'
                if (j >= tokens.size() || tokens.get(j).getType() != ValidTokens.WHILE) {
                    throw new LexicalException("Expected 'while' after 'do' block");
                }
                j++;
                
                // Vérifier '('
                if (j >= tokens.size() || tokens.get(j).getType() != ValidTokens.LPAREN) {
                    throw new LexicalException("Expected '(' after 'while' in 'do-while'");
                }
                
                j = acceptBlock(j, ValidTokens.LPAREN);
                
                // Vérifier ';'
                if (j >= tokens.size() || tokens.get(j).getType() != ValidTokens.SEMI) {
                    throw new LexicalException("Expected ';' after 'do-while'");
                }
            }
        }
        return true;
    }

    public boolean acceptReturn() {
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() == ValidTokens.RETURN) {
                // Vérifier: return [valeur];
                int j = i + 1;
                // La valeur de retour est optionnelle, on regarde si c'est un point-virgule
                if (j < tokens.size() && tokens.get(j).getType() != ValidTokens.SEMI) {
                    // Il y a une valeur de retour (identifiant, constante, expression, etc.)
                    // On accepte IDENT ou CONST
                    if (tokens.get(j).getType() != ValidTokens.IDENT && 
                        tokens.get(j).getType() != ValidTokens.CONST &&
                        tokens.get(j).getType() != ValidTokens.INT &&
                        tokens.get(j).getType() != ValidTokens.VOID) {
                        throw new LexicalException("Expected value or identifier after 'return'");
                    }
                    j++;
                }
                // Vérifier le point-virgule
                if (j >= tokens.size() || tokens.get(j).getType() != ValidTokens.SEMI) {
                    throw new LexicalException("Expected ';' after 'return'");
                }
            }
        }
        return true;
    }

    public boolean acceptInt() {
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() == ValidTokens.INT) {
                // Vérifier: int identifier [= valeur];
                if (i + 1 >= tokens.size() || tokens.get(i + 1).getType() != ValidTokens.IDENT) {
                    throw new LexicalException("Expected identifier after 'int'");
                }
                int j = i + 2;
                
                // Optionnellement vérifier initialisation (=)
                if (j < tokens.size() && tokens.get(j).getType() == ValidTokens.ASSIGN) {
                    j++;
                    // Accepter une constante ou identifiant après =
                    if (j >= tokens.size() || (tokens.get(j).getType() != ValidTokens.CONST && 
                                               tokens.get(j).getType() != ValidTokens.IDENT)) {
                        throw new LexicalException("Expected value after '='");
                    }
                    j++;
                }
                
                // Vérifier le point-virgule
                if (j >= tokens.size() || tokens.get(j).getType() != ValidTokens.SEMI) {
                    throw new LexicalException("Expected ';' after variable declaration");
                }
            }
        }
        return true;
    }

    public boolean acceptVoid() {
        for (int i = 0; i < tokens.size(); i++) {
            Token token = tokens.get(i);
            if (token.getType() == ValidTokens.VOID) {
                // Vérifier: void identifier ( )
                if (i + 1 >= tokens.size() || tokens.get(i + 1).getType() != ValidTokens.IDENT) {
                    throw new LexicalException("Expected identifier after 'void'");
                }
                int j = i + 2;
                
                // Vérifier la parenthèse ouvrante
                if (j >= tokens.size() || tokens.get(j).getType() != ValidTokens.LPAREN) {
                    throw new LexicalException("Expected '(' after function name");
                }
                
                // Accepter le bloc de parenthèses (paramètres)
                j = acceptBlock(j, ValidTokens.LPAREN);
                
                // Vérifier l'accolade ouvrante pour le corps de la fonction
                if (j >= tokens.size() || tokens.get(j).getType() != ValidTokens.LBRACE) {
                    throw new LexicalException("Expected '{' after function signature");
                }
                
                j = acceptBlock(j, ValidTokens.LBRACE);
            }
        }
        return true;
    }

    /**
     * Accepte une instruction break.
     * Structure attendue: break;
     * @return true si la validation réussit
     * @throws LexicalException si la structure n'est pas respectée
     */
    public boolean acceptBreak() {
        acceptSimpleStatement(ValidTokens.BREAK);
        return true;
    }

    /**
     * Accepte une instruction continue.
     * Structure attendue: continue;
     * @return true si la validation réussit
     * @throws LexicalException si la structure n'est pas respectée
     */
    public boolean acceptContinue() {
        acceptSimpleStatement(ValidTokens.CONTINUE);
        return true;
    }
}
