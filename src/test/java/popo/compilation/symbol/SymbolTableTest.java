package popo.compilation.symbol;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SymbolTableTest {

    private SymbolTable table;

    @BeforeEach
    void setUp() {
        table = new SymbolTable();
    }

    @Test
    void declareReturnsNewSymbolWithIdentifierAsValue() {
        Symbol symbol = table.declare("x");

        assertNotNull(symbol);
        assertEquals("x", symbol.getValue());
    }

    @Test
    void declareSameIdentifierTwiceInSameBlockThrows() {
        table.declare("x");

        SymbolException exception = assertThrows(SymbolException.class, () -> table.declare("x"));
        assertEquals("Symbol already declared: x", exception.getMessage());
    }

    @Test
    void findReturnsDeclaredSymbol() {
        Symbol declared = table.declare("x");

        assertSame(declared, table.find("x"));
    }

    @Test
    void findUnknownIdentifierThrows() {
        SymbolException exception = assertThrows(SymbolException.class, () -> table.find("unknown"));
        assertEquals("Symbol not found: unknown", exception.getMessage());
    }

    @Test
    void sameIdentifierCanBeDeclaredInNestedBlock() {
        Symbol outer = table.declare("x");
        table.begin();

        Symbol inner = table.declare("x");

        assertNotSame(outer, inner);
        assertSame(inner, table.find("x"));
    }

    @Test
    void findSearchesOuterBlocks() {
        Symbol outer = table.declare("x");
        table.begin();
        table.declare("y");

        assertSame(outer, table.find("x"));
    }

    @Test
    void symbolsOfEndedBlockAreNoLongerVisible() {
        table.begin();
        table.declare("y");
        table.end();

        assertThrows(SymbolException.class, () -> table.find("y"));
    }

    @Test
    void symbolsOfOuterBlockAreVisibleAfterEndingInnerBlock() {
        Symbol outer = table.declare("x");
        table.begin();
        table.end();

        assertSame(outer, table.find("x"));
    }

    @Test
    void innerDeclarationShadowsOuterOnlyWhileInsideBlock() {
        Symbol outer = table.declare("x");
        table.begin();
        Symbol inner = table.declare("x");
        table.end();

        assertSame(outer, table.find("x"));
        assertNotSame(inner, table.find("x"));
    }

    @Test
    void sameIdentifierCanBeRedeclaredInNewBlockAfterEnd() {
        table.begin();
        table.declare("y");
        table.end();

        assertDoesNotThrow(() -> table.declare("y"));
    }
}
