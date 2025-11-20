
---

### **Desafio 1 – Classe interna simples**

**Problema:**
Crie uma classe `Pessoa` com um atributo `nome`.
Dentro dela, crie uma **classe interna** `Endereco` com atributo `rua`.

* Instancie `Pessoa` e `Endereco` e imprima o nome e a rua.

**Pergunta lógica:** Como instanciar uma **inner class** ligada a um objeto da classe externa?

---

### **Desafio 2 – Inner class estática**

**Problema:**
Crie uma classe `Calculadora` com uma **inner class estática** chamada `Operacoes`.

* Adicione um método `somar(int a, int b)` que retorna a soma.
* Chame o método sem precisar criar uma instância de `Calculadora`.

**Pergunta lógica:** Qual a diferença entre uma inner class **estática** e não estática em relação à instância da classe externa?

---

### **Desafio 3 – Inner class privada e encapsulamento**

**Problema:**
Crie uma classe `Carro` com uma inner class **privada** `Motor`.

* `Motor` tem um método `ligar()` que imprime `"Motor ligado"`
* Crie um método público em `Carro` chamado `ligarMotor()` que **chame o método da inner class**.
* Teste chamando `ligarMotor()` de fora da classe `Carro`.

**Pergunta lógica:** Como inner classes privadas ajudam a encapsular lógica que só a classe externa deve acessar?

---



