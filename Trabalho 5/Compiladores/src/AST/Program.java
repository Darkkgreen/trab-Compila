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

	public StringBuffer genC(String nome, String dir) throws IOException {
		File file = new File(dir+nome);
		file.getParentFile().mkdirs();
		FileWriter writer = new FileWriter(file);
		
		StringBuffer aux = new StringBuffer("#include <stdio.h>\n#include <stdlib.h>\n\nvoid main(){\n");
		StringBuffer variable;
		StringBuffer stmt;

//		for (Variable v : listV) {
//			variable = v.genC(1);
			//aux.append(variable);
			//aux.append("\n");
		//}
		aux.append("\n");

		//for (Stmt s : listS) {
		//	stmt = s.genC(1);
		//	aux.append(stmt);
		//	aux.append("\n");
		//}

		aux.append("}");
		
		writer.append(aux);
		writer.close();

		return aux;
	}

}
