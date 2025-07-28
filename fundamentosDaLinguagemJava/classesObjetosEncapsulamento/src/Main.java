// ☕ 1. Classes em Java
// Uma classe funciona como um molde ou uma "fábrica" para criar objetos.
// Nela, definimos os atributos (características) e os métodos (ações) que os objetos terão.
public class Carro {
    // Atributos
    String modelo;
    int ano;

    // Método para simular o som da buzina
    void buzinar() {
        System.out.println("Biiiiiii!");
    }

    // Método para exibir as informações do carro
    void exibirInfo() {
        System.out.println("Modelo: " + modelo + " | Ano: " + ano);
    }
}

// 🚗 2. Objetos em Java
// Um objeto é uma instância (um exemplar real) de uma classe.
// Ele possui os dados e os comportamentos definidos na classe que o originou.
public class Main {
    public static void main(String[] args) {
        // Criando um objeto da classe Carro
        Carro meuCarro = new Carro();

        // Atribuindo valores aos atributos
        meuCarro.modelo = "Fusca";
        meuCarro.ano = 1975;

        // Chamando os métodos do objeto
        meuCarro.buzinar();       // Saída: Biiiiiii!
        meuCarro.exibirInfo();    // Saída: Modelo: Fusca | Ano: 1975
    }
}

// 🔒 3. Encapsulamento
// Encapsulamento é o princípio de ocultar os detalhes internos de uma classe
// e permitir o acesso aos dados apenas por meio de métodos públicos (getters e setters).
public class Pessoa {
    // Atributo privado (só acessível dentro da própria classe)
    private String nome;

    // Getter: retorna o valor do nome
    public String getNome() {
        return nome;
    }

    // Setter: define ou altera o valor do nome
    public void setNome(String novoNome) {
        this.nome = novoNome;
    }
}
