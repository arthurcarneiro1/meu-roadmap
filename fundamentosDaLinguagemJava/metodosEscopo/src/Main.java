// 🧠 MÉTODOS EM JAVA

// 📌 O que são métodos?
// Métodos são blocos de código que executam tarefas específicas.
// Eles ajudam a organizar a lógica do programa, evitar repetição de código
// e facilitar a manutenção e reuso de funcionalidades.

// 📎 Estrutura básica de um método:
public tipoDeRetorno nomeDoMetodo(parâmetros) {
    // Instruções
    return valor; // Somente se o tipoDeRetorno não for void
}

// ✅ Exemplo prático:
public class Calculadora {

    // Método que soma dois números inteiros e retorna o resultado
    public int somar(int a, int b) {
        return a + b;
    }

    public static void main(String[] args) {
        Calculadora calc = new Calculadora();
        int resultado = calc.somar(10, 5);
        System.out.println("Resultado: " + resultado); // Resultado: 15
    }
}

// 🔹 Tipos de métodos:
// - void → não retorna nenhum valor.
// - int, String, boolean, etc. → retornam valores do tipo especificado.
// - static → podem ser chamados sem precisar instanciar a classe.


// 🧭 ESCOPO EM JAVA

// 📌 O que é escopo?
// Escopo é a área onde uma variável pode ser usada/acessada dentro do código.
// Existem três tipos principais de escopo em Java:

// 🔹 1. Escopo de Classe (variáveis de instância)
// Disponíveis para todos os métodos da classe.
public class Pessoa {
    String nome; // Variável de instância (escopo da classe)
}

// 🔹 2. Escopo de Método (variáveis locais)
// Existência limitada ao método onde são declaradas.
public void mostrarIdade() {
    int idade = 25; // Só pode ser usada dentro deste método
    System.out.println(idade);
}

// 🔹 3. Escopo de Bloco (condições e laços)
// Válidas apenas dentro do bloco onde foram declaradas.
public void exemplo() {
    if (true) {
        int x = 10;
        System.out.println(x); // OK
    }
    // System.out.println(x); // ERRO: x está fora do escopo do bloco
}

// 🎯 Dica: Sempre declare variáveis no menor escopo possível!
// Isso evita conflitos, melhora a legibilidade e reduz chances de erro.
