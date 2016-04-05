package AST;
import java.util.ArrayList;

public class VariableDecl {

    public ArrayList<Variable> getVariableDecl() {
        return variableDecl;
    }

    public void setVariableDecl(Variable variable) {
        this.variableDecl.add(variable);
    }

    public VariableDecl() {
        variableDecl =  new ArrayList<Variable>();
    }
    
    private ArrayList<Variable> variableDecl = null;
    
}
