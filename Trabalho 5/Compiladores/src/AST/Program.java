package AST;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class Program {
	
	private FunctionDecl fdecl;

	public FunctionDecl getFdecl() {
		return fdecl;
	}

	public Program(FunctionDecl fdecl) {
		this.fdecl = fdecl;
	}

	public StringBuffer genC(Integer tabs) throws IOException {
		StringBuffer aux = new StringBuffer();
                aux = fdecl.genC(0);
                
                return aux;
	}

}
