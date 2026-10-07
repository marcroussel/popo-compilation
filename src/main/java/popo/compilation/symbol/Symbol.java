package popo.compilation.symbol;

public class Symbol {
    public String type;
    public int index;


    public Symbol(int index, String type) {
        this.index = index;
        this.type = type;
    }
    
    public Symbol(String ident){
        this.type = ident;
        this.index = -1;
    }
}
