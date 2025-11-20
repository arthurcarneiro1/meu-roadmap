Perfeito! Aqui vão **3 desafios lógicos sobre Threads em Java**, do mais simples ao mais avançado:

---

### **Desafio 1 – Criando uma Thread com Runnable**

**Problema:**

* Crie uma classe que implemente `Runnable` e sobrescreva o método `run()` para imprimir `"Thread em execução"`.
* Crie uma Thread usando essa classe e inicie-a.

**Pergunta lógica:** Qual a diferença entre criar uma Thread com `Runnable` e estendendo a classe `Thread`?

---

### **Desafio 2 – Thread com lambda**

**Problema:**

* Crie uma Thread usando **expressão lambda** que imprima os números de 1 a 5.
* Inicie a Thread.

**Pergunta lógica:** Como lambdas permitem escrever Threads de forma mais concisa e funcional?

---

### **Desafio 3 – Sincronização**

**Problema:**

* Crie uma classe `Conta` com um método `sacar(double valor)` que decrementa o saldo.
* Crie duas Threads que tentam sacar do mesmo objeto `Conta` simultaneamente.
* Use **synchronized** para evitar problemas de concorrência e garantir que o saldo não fique incorreto.

**Pergunta lógica:** Por que é importante sincronizar métodos em ambientes multithread para evitar condições de corrida?

---

Se quiser, posso criar **as soluções prontas e comentadas** desses 3 desafios de Threads em Java, mostrando o jeito mais moderno e seguro de trabalhar com concorrência.

Quer que eu faça?

