package popo.compilation.symbol;

public class Symbol {
    public int type;
    public int index;


    public Symbol(int index, int type) {
        this.index = index;
        this.type = type;
    }
    
    public Symbol(){
        this.type = ValidSymbol.ValidTypes.get("VARIABLE");
        this.index = -1;
    }
}
