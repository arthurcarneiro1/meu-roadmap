// 🔐 Modificadores de Acesso em Java

// ======================================
// 1. public 🌎
// → Acesso livre de qualquer lugar do código.
// → Pode ser acessado por qualquer classe, mesmo fora do pacote.
public class Pessoa {
    public String nome;

    public void dizerOi() {
        System.out.println("Oi!");
    }
}

// ======================================
// 2. private 🔒
// → Acesso restrito à própria classe.
// → Não pode ser acessado nem mesmo por subclasses.
public class Conta {
    private double saldo;

    private void atualizarSaldo() {
        // Método só acessível dentro da própria classe
    }
}

// ======================================
// 3. protected 🛡️
// → Acesso permitido:
//    • Dentro da mesma classe
//    • Dentro do mesmo pacote
//    • Por subclasses (mesmo em pacotes diferentes)
public class Animal {
    protected void emitirSom() {
        System.out.println("Som do animal");
    }
}

public class Cachorro extends Animal {
    public void latir() {
        emitirSom(); // Pode acessar porque é protected
    }
}

// ======================================
// 4. (default) 📦
// → Sem modificador (também chamado de package-private).
// → Acesso permitido apenas dentro do mesmo pacote.
class Produto {
    String no
