package popo.compilation.symbol;

public class Symbol {
    private Object value;

    public Symbol(Object value) {
        this.value = value;
    }

    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }
}
