package popo.compilation.symbol;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Optional;

public class SymbolTable {

    private ArrayList<HashMap<String, Symbol>> symbolTableStack = new ArrayList<HashMap<String, Symbol>>();

    /**
     * Constructeur de la table des symboles,
     * qui commence par créer un premier bloc
     */
    public SymbolTable() {

        // Génération du premier bloc de code
        begin();
    }

    /**
     * Déclare un nouveau symbole dans la dernière HashMap
     * @param ident L'identificateur attribué à ce nouveau symbole
     * @return le nouveau symbole rajouté
     * @throws SymbolException Lorsque le symbole existe déjà dans la dernière HashMap
     */
    Symbol declare(String ident) throws SymbolException {

        // Vérification de si le symbole est présent ou non dans la HashMap
        Optional<Symbol> symbol = Optional.ofNullable(symbolTableStack.getLast().get(ident));

        if (symbol.isPresent()) {
            throw new SymbolException("Symbol already declared: " + ident);
        }

        // Création et renvoi du nouveau symbole
        Symbol newSymbol = new Symbol(ident);
        symbolTableStack.getLast().put(ident, newSymbol);
        return newSymbol;
    }

    /**
     * Cherche, dans toute la table, le symbole qui a l'identificateur ident
     * @param ident L'identificateur du symbole cherché
     * @return Le symbole correspondant, si trouvé
     * @throws SymbolException Si le symbole n'a pas été trouvé
     */
    Symbol find(String ident) throws SymbolException {

        // Recherche de l'identificateur
        Optional<Symbol> symbol = Optional.ofNullable(symbolTableStack.getLast().get(ident));

        // Cas où l'identificateur est présent dans la dernière HashMap
        if (symbol.isPresent()) {
            return symbol.get();
        }
        else {

            // Recherche du symbole dans les autres HashMaps
            for (int i = symbolTableStack.size() - 1; i >= 0; i--) {
                symbol = Optional.ofNullable(symbolTableStack.get(i).get(ident));

                if (symbol.isPresent()) {
                    return symbol.get();
                }
            }

            // Cas où le symbole n'a été trouvé nulle part
            throw new SymbolException("Symbol not found: " + ident);
        }
    }

    /**
     * Crée une nouvelle HashMap de symboles,
     * lorsque l'on entre dans un nouveau bloc de code,
     * puis l'empile dans la stack
     */
    void begin() {
        symbolTableStack.add(new HashMap<String, Symbol>());
    }

    /**
     * Dépile la HashMap de symboles au sommet de la stack,
     * lorsque l'on sort d'un bloc de code
     */
    void end() {
        symbolTableStack.removeLast();
    }
}
