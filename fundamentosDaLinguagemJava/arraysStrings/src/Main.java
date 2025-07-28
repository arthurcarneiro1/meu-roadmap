
public class Main {
    public static void main(String[] args) {
    //📦 Arrays em Java
        //✅ O que são?
        //Um array é uma estrutura de dados que armazena vários valores do mesmo tipo em uma única variável. Eles têm tamanho fixo, definido no momento da criação.
        //🧠 Como declarar e usar um array

        // Declarando um array de inteiros com 5 posições
        int[] numeros = new int[5];

        // Atribuindo valores
        numeros[0] = 10;
        numeros[1] = 20;

        // Imprimindo valores
        System.out.println(numeros[0]); // Saída: 10

        //🌀 Array com inicialização direta
        String[] nomes = {"Ana", "Bruno", "Carlos"};
        System.out.println(nomes[1]); // Saída: Bruno

        //index inicia em 0
        // Criando um array de 4 elementos
        String[] frutas = {"Maçã", "Banana", "Laranja", "Uva"};

        // A indexação começa em 0:
        // frutas[0] é o primeiro elemento
        // frutas[1] é o segundo elemento
        // frutas[2] é o terceiro elemento
        // frutas[3] é o quarto elemento

        //🔁 Percorrendo um array com for
        for (int i = 0; i < nomes.length; i++) {
            System.out.println(nomes[i]);
        }

        //🔁 Usando for-each
        for (String nome : nomes) {
            System.out.println(nome);
        }
        //------------------------------------------------------------------------------------------
        //🧵 Strings em Java
        //✅ O que são?
        //String é uma classe em Java usada para representar sequências de caracteres. É imutável, ou seja, uma vez criada, seu conteúdo não pode ser alterado (qualquer modificação gera uma nova String).

        //📌 Declarando Strings
        String nome = "Arthur";
        String outra = new String("Exemplo");

        //🔧 Métodos úteis da classe String
        String texto = "Java é incrível";

        // Tamanho da string
        System.out.println(texto.length()); // 15

        // Substring
        System.out.println(texto.substring(0, 4)); // Java

        // Letra maiúscula
        System.out.println(texto.toUpperCase()); // JAVA É INCRÍVEL

        // Letra minúscula
        System.out.println(texto.toLowerCase()); // java é incrível

        // Verifica se contém algo
        System.out.println(texto.contains("incrível")); // true

        // Comparações
        String a = "abc";
        String b = "abc";

        System.out.println(a == b); // true (mesma referência)
        System.out.println(a.equals(b)); // true (mesmo conteúdo)

        //🔄 Concatenando Strings
        String name = "Arthur";
        int idade = 25;

        String frase = nome + " tem " + idade + " anos.";
        System.out.println(frase);





    }
}
