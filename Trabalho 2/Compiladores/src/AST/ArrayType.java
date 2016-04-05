package AST;

public class ArrayType {
    private StdType tipo;
    private boolean tipoArray;    
    
    public StdType getTipo() {
        return tipo;
    }

    public boolean isTipoArray() {
        return tipoArray;
    }

    public ArrayType(StdType tipo, boolean tipoArray) {
        this.tipo = tipo;
        this.tipoArray = tipoArray;
    }
    
    
}
