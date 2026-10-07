package popo.compilation.token;

public enum ValidTokens {

    // Fin de flux
    EOS,

    // Constantes et identificateurs
    CONST,      // ex: 42
    IDENT,      // ex: nomDeVariable

    // Opérateurs arithmétiques
    PLUS,       // +
    MINUS,      // -
    MUL,        // *  (multiplication ET déréférencement de pointeur, ex: *p)
    DIV,        // /
    MOD,        // %  (modulo)
    AMP,        // &  (adresse d'une variable, ex: &x — aussi utilisable en ET binaire)

    // Comparateurs
    LT,         // <
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
    DQOT,       // "
    SQOT,       // '
    SEMI,       // ;
    COMMA,      // ,
    COLON,      // :
    DOT,        // .

    // Mots-clés
    IF,
    ELSE,
    FOR,
    WHILE,
    DO,
    VOID,
    CONTINUE,
    BREAK,
    RETURN,
    DEBUG,
    DROP,

    //Type de déclaration
    INT
}