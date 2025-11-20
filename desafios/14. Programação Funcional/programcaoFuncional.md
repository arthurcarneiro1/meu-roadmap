
---

### **Desafio 1 – Lambda simples**

**Problema:**
Você tem uma lista de números:

```java
List<Integer> numeros = List.of(1, 2, 3, 4, 5);
```

* Use **lambda** para imprimir cada número ao quadrado.

**Pergunta lógica:** Como usar `forEach` com uma expressão lambda para aplicar uma operação a cada elemento?

---

### **Desafio 2 – Filtragem e mapeamento**

**Problema:**
Você tem uma lista de nomes:

```java
List<String> nomes = List.of("Ana", "Bruno", "Carlos", "Beatriz");
```

* Use **Streams e lambdas** para:

  1. Filtrar nomes que começam com `"B"`
  2. Transformar os nomes filtrados em **maiúsculas**
  3. Imprimir o resultado

**Pergunta lógica:** Como encadear operações de filtro e mapeamento de forma funcional?

---

### **Desafio 3 – Redução e agregação**

**Problema:**
Você tem uma lista de preços:

```java
List<Double> precos = List.of(10.0, 20.0, 30.0);
```

* Use **Streams** para calcular o **total com 10% de aumento** em cada preço usando `map` e `reduce`.

**Pergunta lógica:** Como combinar transformação (`map`) e agregação (`reduce`) de forma funcional?

---
