/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import AST.Program;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

/**
 *
 * @author ricke
 */
public class Compiladores {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws IOException {

		ArrayList<String> nomeArquivos = new ArrayList<String>();
		String dir = System.getProperty("user.dir");
		dir = dir.concat("\\src\\TestesCaseiros");

		System.out.println(dir);

		// pega todos os arquivos e adiciona um arrayList
		// de forma recursiva
		Files.walk(Paths.get(dir)).forEach(filePath -> {
			if (Files.isRegularFile(filePath)) {
				nomeArquivos.add(filePath.getFileName().toString());
			}
		});
		Collections.sort(nomeArquivos);

		for (String nome : nomeArquivos) {
			System.out.println(nome);
			String entrada = new String();
			String linha = null;
			try {
				FileReader arq = new FileReader(dir + "/" + nome);
				BufferedReader lerArq = new BufferedReader(arq);

				linha = lerArq.readLine();
				while (linha != null) {
					entrada = entrada.concat(linha).concat("\n");
					linha = lerArq.readLine();

				}
				arq.close();
			} catch (IOException e) {
				System.err.printf("Erro na abertura do arquivo: %s.\n",
					e.getMessage());
			}

			entrada = entrada.concat(" ");

			char[] input = entrada.toCharArray();
			Compiler compiler = new Compiler();

			ArrayList<Program> program = null;
			try {
				program = compiler.compile(input, nome);
				System.out.println("OK!!!!");
				System.out.println("==========================================================================");

			} catch (RuntimeException e) {
				System.out.println("==========================================================================");
			}

			if (program != null) {
				try {
					for(Program s : program){
						s.genC(nome.replace(".txt", ".c"));
					}
				} catch (RuntimeException e) {
					System.out.println("Não foi possível gerar C, erro de " + e);
				}
			}

		}
	}

}
