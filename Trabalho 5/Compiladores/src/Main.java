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
public class Main {

	/**
	 * @param args the command line arguments
	 */
	public static void main(String[] args) throws IOException {

		String nome = args[0];
		String nomeC = args[1];

		String entrada = new String();
		String linha = null;
		try {
			FileReader arq = new FileReader(nome);
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
		program = compiler.compile(input, nome);

		if (program != null) {
			try {
				for (Program s : program) {
					s.genC(nomeC);
				}
			} catch (RuntimeException e) {
				System.out.println("Não foi possível gerar C, erro de " + e);
			}
		}

	}

}
