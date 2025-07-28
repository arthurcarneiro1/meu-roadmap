// 🔁 O que é Sobrecarga de Métodos?
// Sobrecarga de métodos (ou *method overloading*) acontece quando
// dois ou mais métodos em uma mesma classe têm o mesmo nome,
// mas **assinaturas diferentes**.
//
// Isso significa que eles diferem em:
// - Número de parâmetros
// - Tipo dos parâmetros
// - Ordem dos parâmetros

// 🧠 Para que serve?
// Permite reutilizar o mesmo nome de método para comportamentos semelhantes,
// tornando o código mais legível, organizado e reutilizável.

// 📌 Regras da sobrecarga:
// - Os métodos devem ter o mesmo nome.
// - Devem ter parâmetros diferentes (em tipo, quantidade ou ordem).
// - O tipo de retorno NÃO é considerado para sobrecarga.

//💻 Exemplo prático:
public class Calculadora {

    // Soma de dois inteiros
    public int somar(int a, int b) {
        return a + b;
    }

    // Soma de três inteiros
    public int somar(int a, int b, int c) {
        return a + b + c;
    }

    // Soma de dois números do tipo double
    public double somar(double a, double b) {
        return a + b;
    }

    // Soma de um número inteiro e um double
    public double somar(int a, double b) {
        return a + b;
    }
}
//🧪 Testando na prática:
public class Main {
    public static void main(String[] args) {
        Calculadora calc = new Calculadora();

        System.out.println(calc.somar(2, 3));         // somar(int, int)
        System.out.println(calc.somar(2, 3, 4));      // somar(int, int, int)
        System.out.println(calc.somar(2.5, 3.1));     // somar(double, double)
        System.out.println(calc.somar(2, 3.5));       // somar(int, double)
    }
}
