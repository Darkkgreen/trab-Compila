package AST;

public class Type {
    private ArrayType array;
    private StdType std;

    public Type(ArrayType array, StdType std) {
        this.array = array;
        this.std = std;
    }
}
