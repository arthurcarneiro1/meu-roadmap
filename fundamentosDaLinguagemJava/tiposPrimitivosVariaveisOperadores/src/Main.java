//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {
//Java possui 8 tipos primitivos, que são tipos de dados básicos — ou seja, não são objetos. Eles são
        // byte: número inteiro pequeno, de -128 a 127 (8 bits)
        byte ida  = 25;

        // short: número inteiro maior que byte, de -32.768 a 32.767 (16 bits)
        short populacaoPequenaCidade = 15000;

        //mais usado
        // int: número inteiro padrão em Java, de -2 bilhões a 2 bilhões (32 bits)
        int populacaoBrasil = 210000000;

        // long: número inteiro muito grande, precisa do 'L' no final (64 bits)
        long distanciaEstrelas = 9876543210L;

        // float: número decimal de precisão simples, precisa do 'f' no final (32 bits)
        float precoProduto = 19.99f;

        //mais usado
        // double: número decimal de precisão dupla, mais usado que float (64 bits)
        double saldoBancario = 15345.67;
        //mais usado
        // char: armazena um único caractere Unicode (16 bits)
        char letraInicial = 'A';
        //mais usado
        // boolean: armazena verdadeiro (true) ou falso (false) (1 bit)
        boolean estaAtivo = true;


        //Variáveis em Java
        //Uma variável é um espaço reservado na memória para armazenar um valor. Toda variável precisa ser declarada com um tipo.
        int idade = 25;
        double altura = 1.75;
        char sexo = 'M';
        boolean ativo = true;
        //🧠 Regras:
        //O nome da variável não pode começar com número.
        //
        //Não pode conter espaços ou caracteres especiais (exceto _ e $).
        //
        //Evite usar palavras reservadas como class, public, int, etc.


        // ---------------------------
        // 🧮 Operadores Aritméticos
        // ---------------------------
        int a = 10;
        int b = 3;

        int soma = a + b;       // Adição: 10 + 3 = 13
        int subtracao = a - b;  // Subtração: 10 - 3 = 7
        int multiplicacao = a * b; // Multiplicação: 10 * 3 = 30
        int divisao = a / b;    // Divisão inteira: 10 / 3 = 3
        int resto = a % b;      // Módulo (resto da divisão): 10 % 3 = 1

        // ---------------------------
        // 📝 Operadores de Atribuição
        // ---------------------------
        int x = 5;
        x += 3;  // Equivale a: x = x + 3 → x = 8
        x -= 2;  // Equivale a: x = x - 2 → x = 6
        x *= 4;  // Equivale a: x = x * 4 → x = 24
        x /= 6;  // Equivale a: x = x / 6 → x = 4
        x %= 3;  // Equivale a: x = x % 3 → x = 1

        // ---------------------------
        // 🔍 Operadores de Comparação
        // ---------------------------
        int n1 = 10;
        int n2 = 20;

        boolean igual = n1 == n2;     // false: 10 não é igual a 20
        boolean diferente = n1 != n2; // true: 10 é diferente de 20
        boolean maior = n1 > n2;      // false
        boolean menor = n1 < n2;      // true
        boolean maiorOuIgual = n1 >= n2; // false
        boolean menorOuIgual = n1 <= n2; // true

        // ---------------------------
        // ⚙️ Operadores Lógicos
        // ---------------------------
        boolean condicao1 = true;
        boolean condicao2 = false;

        boolean eLogico = condicao1 && condicao2; // false: true E false = false
        boolean ouLogico = condicao1 || condicao2; // true: true OU false = true
        boolean naoLogico = !condicao1;            // false: negação de true = false

        //operadores ternarios
        //condição ? valorSeVerdadeiro : valorSeFalso;
        // 📌 Exemplo 1: Verificando se a pessoa é maior de idade
        int year = 18;
        // Se idade for maior ou igual a 18, retorna "Maior de idade", senão "Menor de idade"
        String status = (idade >= 18) ? "Maior de idade" : "Menor de idade";
        System.out.println("Status: " + status);

        // 📌 Exemplo 2: Verificando se um número é par ou ímpar
        int numero = 7;
        // Se o número dividido por 2 tiver resto 0, é par; senão, ímpar
        String parOuImpar = (numero % 2 == 0) ? "Par" : "Ímpar";
        System.out.println("O número " + numero + " é: " + parOuImpar);



    }
}