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
		
		StringBuffer aux = new StringBuffer();

                aux = fdecl.genC(0);
		
		writer.append(aux);
		writer.close();

		return aux;
	}

}
