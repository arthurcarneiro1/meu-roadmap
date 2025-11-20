
---

### **Desafio 1 – ExecutorService básico**

**Problema:**

* Crie um `ExecutorService` com **2 threads**.
* Submeta duas tarefas (`Runnable`) que imprimam `"Tarefa 1"` e `"Tarefa 2"`.
* Feche o `ExecutorService` após a execução.

**Pergunta lógica:** Como usar um pool de threads para gerenciar múltiplas tarefas sem criar Threads manualmente?

---

### **Desafio 2 – Future e Callable**

**Problema:**

* Crie uma tarefa `Callable<Integer>` que retorne a soma de 1 a 10.
* Submeta a tarefa a um `ExecutorService` e obtenha o resultado usando `Future.get()`.

**Pergunta lógica:** Qual a diferença entre `Runnable` e `Callable`? Por que `Callable` permite retornar valores?

---

### **Desafio 3 – Sincronização com ReentrantLock**

**Problema:**

* Crie uma classe `Conta` com método `depositar(double valor)` e saldo inicial 0.
* Crie **duas Threads** que depositam simultaneamente na mesma conta.
* Use `ReentrantLock` para sincronizar os depósitos e evitar inconsistência de saldo.

**Pergunta lógica:** Como `ReentrantLock` oferece mais controle sobre exclusão mútua do que `synchronized`, permitindo maior flexibilidade em programas concorrentes?

---
