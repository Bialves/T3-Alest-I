import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

public class App {
    private Scanner scan;
    private LinkedList<Palavra> lista;
    private WordTree tree;

    public App() {
        scan = new Scanner(System.in);
        lista = new LinkedList<>();
        tree = new WordTree();
    }

    public void executa() {
        String aux[];

        Path path = Paths.get("dicionario.csv");

        try (BufferedReader reader = Files.newBufferedReader(path, Charset.forName("UTF-8"))) {
            String line = reader.readLine();
            Palavra palavra = null;

            while (line != null) {
                aux = line.split(";");
                if (lista.size() == 0) {
                    aux[0] = aux[0].substring(1);
                    System.out.println(aux[0]);
                }
                palavra = new Palavra(aux[0], aux[1]);
                lista.add(palavra);
                line = reader.readLine();
            }

            for (Palavra p : lista) { // Adiciona as palavras na árvore
                tree.addWord(p);
            }
        } catch (FileNotFoundException e) {
            System.err.format("Arquivo não encontrado: %s%n", e);
        } catch (IOException e) {
            System.err.format("Erro na leitura do arquivo: %s%n", e);
        }
        // Execução da aplicação
        boolean run = true;
        do {
            System.out.println("============== MENU ==============");
            System.out.println("1. Buscar palavra");
            System.out.println("2. Sair");
            int opcao = scan.nextInt();

            switch (opcao) {
                case 1:
                    scan.nextLine();
                    busca();
                    break;
                case 2:
                    run = false;
                    break;
                default:
                    System.out.println("ERRO: Opção inválida!");
            }
        } while (run);
    }
    // Método de busca no dicionário
    public void busca() {
        System.out.println("Total de palavras: " + tree.getTotalWords() + " | Total de nodos: " + tree.getTotalNodes());
        System.out.println("> Escreva um ou mais caracteres: ");
        String prefixo = scan.nextLine();

        LinkedList<String> palavraSearch = tree.searchAll(prefixo.toLowerCase()); // Retorna as palavras com o prefixo
        if (palavraSearch.isEmpty()) {
            System.out.println("ERRO: sem palavras inicias com a entrada informada!");
        }
        else {
            System.out.println("> Veja as palavras encontradas abaixo: ");
            System.out.println(palavraSearch.toString()); // Imprime as palavras encontradas

            System.out.println("> Informe a palavra que deseja ver o significado: ");
            String significado = scan.nextLine();

            List<Palavra> palavras =
                    lista
                            .stream()
                            .filter(p -> p.getPalavra().equalsIgnoreCase(significado))
                            .toList();

            if (palavras.isEmpty()) {
                System.out.println("ERRO: palavra não identificada!");
            } else {
                palavras.forEach(p ->
                        System.out.println("A palavra " + p.getPalavra() + " tem o significado: " + p.getSignificado())
                );
            }
        }
    }
}
