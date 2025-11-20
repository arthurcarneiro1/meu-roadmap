

---

### **Desafio 1 – Conversão de tipos**

Você recebeu duas variáveis:

```java
int idade = 25;
double altura = 1.75;
```

**Problema:**
Crie uma variável `resultado` do tipo `double` que seja a soma da idade com a altura, mas faça **uma conversão explícita apenas da idade**.

**Pense:** Como converter `int` para `double` para que a operação seja precisa?

---

### **Desafio 2 – Overflow de byte**

O tipo `byte` em Java varia de `-128` a `127`.
**Problema:**
Crie um `byte` com valor `120`. Some `10` a ele e explique o resultado.

**Pergunta lógica:** O que acontece quando o valor ultrapassa o limite de um tipo primitivo?

---

### **Desafio 3 – Comparação de float e double**

Você tem:

```java
float f = 0.1f;
double d = 0.1;
```

**Problema:**
Crie um `boolean` que armazene se `f == d`.

**Pergunta lógica:** Por que comparar `float` com `double` diretamente pode gerar `false` mesmo que visualmente os números sejam iguais?

---

