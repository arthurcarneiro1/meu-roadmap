
---

### **Desafio 1 – Filtragem simples**

**Problema:**
Você tem uma lista de números:

```java
List<Integer> numeros = List.of(1, 2, 3, 4, 5, 6);
```

* Use **Stream** para filtrar apenas os números **pares** e imprimir o resultado.

**Pergunta lógica:** Como usar `filter` e `forEach` em uma Stream para processar dados de forma funcional?

---

### **Desafio 2 – Mapeamento e soma**

**Problema:**
Você tem uma lista de preços:

```java
List<Double> precos = List.of(10.0, 20.0, 30.0);
```

* Use **Stream** para **somar todos os preços** aplicando **um aumento de 10% em cada um**.

**Pergunta lógica:** Como usar `map` para transformar dados e `reduce` ou `sum` para agregá-los?

---

### **Desafio 3 – Operações complexas**

**Problema:**
Você tem uma lista de nomes:

```java
List<String> nomes = List.of("Ana", "Bruno", "Carlos", "Beatriz");
```

* Use **Stream** para:

  1. Filtrar nomes que começam com `"B"`
  2. Converter para **letras maiúsculas**
  3. Ordenar alfabeticamente
  4. Imprimir o resultado

**Pergunta lógica:** Como combinar múltiplas operações de Stream de forma encadeada para processar dados de maneira declarativa?

---


