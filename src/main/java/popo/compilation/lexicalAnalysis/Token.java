package popo.compilation.lexicalAnalysis;

public class Token {
    int type;
    int valeur;
    String ident; // Pour stocker le nom de la variable (si on détecte une variable)
    int line;
}
