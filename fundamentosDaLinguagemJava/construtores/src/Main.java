// 🏗️ O que é um Construtor?
// Um construtor é um método especial responsável por inicializar objetos de uma classe.
// Ele é executado automaticamente quando usamos a palavra-chave `new` para criar um novo objeto.

// 🔑 Características principais dos construtores:
// - O nome do construtor deve ser exatamente igual ao nome da classe.
// - Construtores **não possuem tipo de retorno**, nem mesmo `void`.
// - Eles podem receber **parâmetros**, permitindo configurar os valores do objeto já na criação.
// - É possível definir **vários construtores com diferentes parâmetros** (isso se chama *sobrecarga*).

public class Carro {
    String modelo;
    int ano;

    // Construtor da classe Carro
    public Carro(String modelo, int ano) {
        // A palavra-chave 'this' se refere ao atributo da própria classe
        this.modelo = modelo;
        this.ano = ano;
    }

    void exibirInfo() {
        System.out.println("Modelo: " + modelo + " | Ano: " + ano);
    }
}

public class Main {
    public static void main(String[] args) {
        // Criando um objeto da classe Carro usando o construtor
        Carro carro1 = new Carro("Civic", 2022);
        carro1.exibirInfo(); // Saída: Modelo: Civic | Ano: 2022
    }
}
// 🔁 Sobrecarga de Construtores
// É possível criar mais de um construtor dentro da mesma classe, desde que tenham parâmetros diferentes.
// Isso permite oferecer formas variadas de instanciar objetos.

public class Pessoa {
    String nome;
    int idade;

    // Construtor com dois parâmetros
    public Pessoa(String nome, int idade) {
        this.nome = nome;
        this.idade = idade;
    }

    // Construtor com apenas um parâmetro
    public Pessoa(String nome) {
        this.nome = nome;
        this.idade = 0; // valor padrão para idade
    }

    void exibirInfo() {
        System.out.println("Nome: " + nome + " | Idade: " + idade);
    }
}

public class Main {
    public static void main(String[] args) {
        // Utilizando diferentes construtores
        Pessoa p1 = new Pessoa("Arthur", 25);
        Pessoa p2 = new Pessoa("Lucas");

        p1.exibirInfo(); // Saída: Nome: Arthur | Idade: 25
        p2.exibirInfo(); // Saída: Nome: Lucas | Idade: 0
    }
}
