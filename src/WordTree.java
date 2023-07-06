import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

public class WordTree {
    class CharNode {
        private char character;
        private boolean isFinal;
        private CharNode father;
        private List<CharNode> children;

        public CharNode(char character) {
            this(character,false); // Chama o outro construtor
        }
        
        public CharNode(char character, boolean isFinal) {
            this.character = character;
            this.isFinal = isFinal;
            children = new LinkedList<>();
        }

        /**
        * Adiciona um filho (caracter) no nodo. Não pode aceitar caracteres repetidos.
        * @param character caracter a ser adicionado
        * @param isfinal se é final da palavra ou não
        */
        public CharNode addChild(char character, boolean isfinal) {
            CharNode aux = new CharNode(character,isfinal);
            aux.father = this;
            children.add(aux);
            return aux;
        }
        
        public int getNumberOfChildren() {
            return children.size();
        }
        
        public CharNode getChild(int index) {
            if ((index < 0) || (index >= children.size())) {
                throw new IndexOutOfBoundsException();
            }
            return children.get(index);
        }

        /**
         * Obtém a palavra correspondente a este nodo, subindo até a raiz da árvore
         * @return a palavra
         */
        private String getWord() {
            Stack<Character> chars = new Stack<>();
            chars.push(character); // Adiciona o último caractere
            return getWord(father,chars); // Monta a palavra pela recursão
        }

        /**
         * Método recursivo com estrutura de pilha, que constrói a palavra
         * @param n nodo que contém caractere da palavra
         * @param chars estrutura de pilha que armazena os caracteres da palavra
         * @return a palavra completa
         */
        private String getWord(CharNode n, Stack<Character> chars) {
            if (n.father == null) { // Identifica o fim da palavra - chegou a raiz
                String word = "";

                while (!chars.isEmpty()) {
                    word += chars.pop();
                }
                return word; // Retorna da palavra montada
            }

            chars.push(n.character); // Se ainda há caracteres, os adiciona
            return getWord(n.father,chars); // e chama a recursão novamente
        }
        
        /**
        * Encontra e retorna o nodo que tem determinado caracter.
        * @param character caractere a ser encontrado.
        */
        public CharNode findChildChar(char character) {
            if (children.isEmpty()) return null;

            for (CharNode cn : children) {
                if (cn.character == character) {
                    return cn;
                }
            }
            return null;
        }
    }

    private CharNode root;
    private int totalNodes;
    private int totalWords;

    public WordTree() {
        root = new CharNode(' '); // Inicializa a raíz
        totalNodes = 1; // Inicializa com o CharNode root
        totalWords = 0;
    }

    public int getTotalWords() {
        return totalWords;
    }

    public int getTotalNodes() {
        return totalNodes;
    }
    
    /**
    * Adiciona palavra na estrutura em árvore
    * @param palavra palavra lida do csv
    */
    public void addWord(Palavra palavra) {
        CharNode aux = root;
        String word = palavra.getPalavra().toLowerCase();

        for (int i=0; i<word.length(); i++) {
            CharNode n = aux.findChildChar(word.charAt(i));
            if (n == null) {
                if (i == word.length()-1) { // Se é o último caractere
                    n = aux.addChild(word.charAt(i),true);
                }
                else {
                    n = aux.addChild(word.charAt(i),false);
                }
                totalNodes++;
            }
            aux = n; // Atualiza a referência
        }
        totalWords++;
    }

    /**
     * Vai descendo na árvore até onde conseguir encontrar a palavra
     * @param word palavra a ser procurada
     * @return o nodo final encontrado
     */
    private CharNode findCharNodeForWord(String word) {
        CharNode aux = root;

        for (int i=0; i<word.length(); i++) {
            aux = aux.findChildChar(word.charAt(i));
            if (aux == null) {
                return null;
            }
        }
        return aux;
    }

    /**
     * Percorre a árvore e retorna uma lista com as palavras iniciadas pelo prefixo dado.
     * Tipicamente, um método recursivo.
     * @param prefix prefixo informado para busca, ex: "ca"
     * @return casa, casamento, etc...
     */
    public LinkedList<String> searchAll(String prefix) {
        LinkedList<String> palavras = new LinkedList<>();
        CharNode pre = findCharNodeForWord(prefix);

        if (pre != null) {
            searchAll(pre,palavras);
        }
        return palavras;
    }

    public void searchAll(CharNode n, LinkedList<String> palavras) {
        if (n.isFinal) {
            palavras.add(n.getWord());
        }

        for (CharNode cn : n.children) { // Procura nos filhos
            searchAll(cn,palavras);
        }
    }
}
