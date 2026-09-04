package popo.compilation.lexicalAnalysis;

public enum ValidTokens {

    // Fin de flux
    EOS,

    // Constantes et identificateurs
    CONST,      // ex: 42
    IDENT,      // ex: nomDeVariable

    // Opérateurs arithmétiques
    PLUS,       // +
    MINUS,      // -
    STAR,       // *  (multiplication ET déréférencement de pointeur, ex: *p)
    SLASH,      // /
    MOD,        // %  (modulo)
    AMP,        // &  (adresse d'une variable, ex: &x — aussi utilisable en ET binaire)

    // Comparateurs
    LT,         //
    GT,         // >
    LE,         // <=
    GE,         // >=
    EQ,         // ==
    NEQ,        // !=
    ASSIGN,     // =  (affectation)

    // Portes logiques
    AND,        // &&
    OR,         // ||
    NOT,        // !

    // Repérages / délimiteurs
    LPAREN,     // (
    RPAREN,     // )
    LBRACKET,   // [
    RBRACKET,   // ]
    LBRACE,     // {
    RBRACE,     // }
    SEMI,       // ;
    COMMA,      // ,

    // Mots-clés
    IF,
    ELSE,
    FOR,
    WHILE,
    DO,
    INT,
    VOID,
    CONTINUE,
    BREAK,
    RETURN
}