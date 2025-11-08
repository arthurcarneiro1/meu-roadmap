# 📝 Notas de Java – Aulas 39 até 93 (com exemplos)

- **Classes e Objetos**
    
    `class Pessoa {     String nome;     int idade; }  Pessoa zoro = new Pessoa(); zoro.nome = "Zoro"; zoro.idade = 19;`
    
    - Classe = modelo.
        
    - Objeto = instância.
        
    - Atributos armazenam dados.
        

---

- **Coesão**
    
    - Cada classe deve ter responsabilidade clara.
        
    
    `class Calculadora {     int soma(int a, int b) {         return a + b;     } }`
    

---

- **Tipo Referência**
    
    `Carro c1 = new Carro(); Carro c2 = c1; // ambos apontam para o mesmo objeto`
    

---

- **Métodos**
    
    `public void falaNome(String nome) {     System.out.println("Meu nome é " + nome); }  public int dobro(int x) {     return x * 2; }`
    
    - `void` → sem retorno.
        
    - Tipos → retornam valores.
        

---

- **Parâmetros e Return**
    
    `public int soma(int a, int b) {     return a + b; }  int resultado = soma(5, 3); // 8`
    

---

- **Passagem por Valor vs Referência**
    
    `// Primitivo (cópia) void altera(int x) { x = 10; }  int num = 5; altera(num); System.out.println(num); // 5  // Objeto (referência) void alteraNome(Pessoa p) { p.nome = "Luffy"; }  Pessoa p1 = new Pessoa(); p1.nome = "Zoro"; alteraNome(p1); System.out.println(p1.nome); // Luffy`
    

---

- **This**
    
    `class Pessoa {     String nome;     Pessoa(String nome) {         this.nome = nome; // diferencia atributo do parâmetro     } }`
    

---

- **VarArgs**
    
    `void imprimeNomes(String... nomes) {     for(String n : nomes) {         System.out.println(n);     } }  imprimeNomes("Zoro", "Luffy", "Nami");`
    

---

- **Encapsulamento (private, getters e setters)**
    
    `class Conta {     private double saldo;      public double getSaldo() {         return saldo;     }      public void depositar(double valor) {         this.saldo += valor;     } }`
    

---

- **Sobrecarga**
    
    `class Calc {     int soma(int a, int b) { return a + b; }     int soma(int a, int b, int c) { return a + b + c; } }`
    

---

- **Construtores**
    
    `class Carro {     String modelo;     int ano;      Carro(String modelo) {         this.modelo = modelo;     }      Carro(String modelo, int ano) {         this(modelo); // chama outro construtor         this.ano = ano;     } }`
    

---

- **Blocos de Inicialização**
    
    `class Pessoa {     int idade;     { idade = 9; } // executado antes do construtor }`
    

---

- **Static**
    
    `class Util {     static int soma(int a, int b) {         return a + b;     } }  int r = Util.soma(2, 3);`
    

---

- **Associações**
    
    `class Professor {     String nome; }  class Escola {     Professor prof; // associação unidirecional }`
    

---

- **Scanner (entrada de dados)**
    
    `Scanner sc = new Scanner(System.in); int numero = sc.nextInt(); String palavra = sc.next(); char c = sc.next().charAt(0);`
    

---

- **Herança**
    
    `class Animal {     void som() { System.out.println("Som genérico"); } }  class Cachorro extends Animal {     @Override     void som() { System.out.println("Au au"); } }  Animal a = new Cachorro(); a.som(); // "Au au"`
    

---

- **Final**
    
    `final class Constantes {     final double PI = 3.14; }`
    

---

- **Enums**
    
    `enum Dia {     SEGUNDA, TERCA, QUARTA }  Dia d = Dia.SEGUNDA;`
    

---

- **Classe Abstrata**
    
    `abstract class Animal {     abstract void som(); }  class Gato extends Animal {     void som() { System.out.println("Miau"); } }`
    

---

- **Interfaces**
    
    `interface Animal {     void som(); }  class Cachorro implements Animal {     public void som() { System.out.println("Au au"); } }`
    

---

- **Polimorfismo**
    
    `Animal a = new Cachorro(); a.som(); // "Au au"  if (a instanceof Cachorro) {     Cachorro c = (Cachorro) a; // cast     c.som(); }`
    

---

- **Instanceof**
    
    `if (a instanceof Cachorro) {     System.out.println("É um cachorro"); }`