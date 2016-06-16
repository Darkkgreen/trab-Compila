/* 
* Trabalho de Compiladores - Final
* Gustavo Rodrigues RA 489999
* Henrique Teruo Eihara RA 490016
 */
import AST.Program;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
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
            System.exit(0);
        }
        

        entrada = entrada.concat(" ");

        char[] input = entrada.toCharArray();
        Compiler compiler = new Compiler();

        ArrayList<Program> program = null;
        try{
        program = compiler.compile(input, nome);
        }catch (RuntimeException e){
            System.out.println();
            System.out.print(e);
            System.exit(0);
        }

        File file = new File(nomeC);

        FileWriter writer = new FileWriter(file);
        StringBuffer aux;
        Integer i = 0;

        if (program != null) {
            writer.append("#include <stdio.h>\n");
            writer.append("#include <stdlib.h>\n\n");
            for (Program s : program) {
                if (i != 0) {
                    writer.append("\n");
                }
                aux = s.genC(0);
                writer.append(aux);
                i++;
            }

            writer.close();
        }

    }

}
