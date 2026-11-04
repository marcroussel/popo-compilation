package popo.compilation.symbol;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SymbolTableTest {

    @BeforeEach
    void setUp() {
        // SymbolTable est statique : on repart d'une pile vide avec un bloc global
        SymbolTable.symbolTableStack.clear();
        SymbolTable.begin();
    }

    @Test
    void declareReturnsDefaultVariableSymbol() throws SymbolException {
        Symbol symbol = SymbolTable.declare("x");

        assertEquals(ValidSymbol.ValidTypes.get("VARIABLE"), symbol.type);
        assertEquals(-1, symbol.index);
    }

    @Test
    void declareSameIdentifierTwiceInSameBlockThrows() throws SymbolException {
        SymbolTable.declare("x");

        SymbolException exception = assertThrows(SymbolException.class, () -> SymbolTable.declare("x"));
        assertEquals("Symbol already declared: x", exception.getMessage());
    }

    @Test
    void findReturnsDeclaredSymbol() throws SymbolException {
        Symbol declared = SymbolTable.declare("x");

        assertSame(declared, SymbolTable.find("x"));
    }

    @Test
    void findUnknownIdentifierThrows() {
        SymbolException exception = assertThrows(SymbolException.class, () -> SymbolTable.find("unknown"));
        assertEquals("Symbol not found: unknown", exception.getMessage());
    }

    @Test
    void sameIdentifierCanBeDeclaredInNestedBlock() throws SymbolException {
        Symbol outer = SymbolTable.declare("x");
        SymbolTable.begin();

        Symbol inner = SymbolTable.declare("x");

        assertNotSame(outer, inner);
        assertSame(inner, SymbolTable.find("x"));
    }

    @Test
    void findSearchesOuterBlocks() throws SymbolException {
        Symbol outer = SymbolTable.declare("x");
        SymbolTable.begin();
        SymbolTable.declare("y");

        assertSame(outer, SymbolTable.find("x"));
    }

    @Test
    void symbolsOfEndedBlockAreNoLongerVisible() throws SymbolException {
        SymbolTable.begin();
        SymbolTable.declare("y");
        SymbolTable.end();

        assertThrows(SymbolException.class, () -> SymbolTable.find("y"));
    }

    @Test
    void symbolsOfOuterBlockAreVisibleAfterEndingInnerBlock() throws SymbolException {
        Symbol outer = SymbolTable.declare("x");
        SymbolTable.begin();
        SymbolTable.end();

        assertSame(outer, SymbolTable.find("x"));
    }

    @Test
    void innerDeclarationShadowsOuterOnlyWhileInsideBlock() throws SymbolException {
        Symbol outer = SymbolTable.declare("x");
        SymbolTable.begin();
        Symbol inner = SymbolTable.declare("x");
        SymbolTable.end();

        assertSame(outer, SymbolTable.find("x"));
        assertNotSame(inner, SymbolTable.find("x"));
    }

    @Test
    void sameIdentifierCanBeRedeclaredInNewBlockAfterEnd() throws SymbolException {
        SymbolTable.begin();
        SymbolTable.declare("y");
        SymbolTable.end();

        assertDoesNotThrow(() -> SymbolTable.declare("y"));
    }
}
