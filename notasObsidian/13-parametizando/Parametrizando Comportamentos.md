
---

## Explicação Feynman: Parametrizando Comportamentos

Imagine que você tem uma máquina que faz uma coisa bem simples, tipo "filtrar coisas". Digamos, uma máquina de filtrar carros de uma garagem.

A primeira vez que você constrói essa máquina, o cliente pede: "Quero todos os carros verdes". Você escreve o código para isso: ele percorre a lista de carros e só deixa passar os que têm `cor == "verde"`.

Aí, o cliente volta e diz: "Ótimo! Agora eu quero os carros vermelhos!". O que o desenvolvedor preguiçoso faz? Copia e cola o código, muda o `"verde"` para `"vermelho"`, e cria um método novo: `filtrarCarrosVermelhos()`.

_Isso é horrível para a manutenção!_. Se a lógica de percorrer o catálogo mudar (por exemplo, se você começar a usar um banco de dados em vez de uma lista), você terá que mudar dois, três, dez lugares no seu código.

A ideia central da **Parametrização de Comportamentos** é: **não codifique a regra dentro da função, passe a regra como um ingrediente!**.

Pense assim: A função principal (o motor da sua máquina de filtrar) é o _loop_ que percorre a lista de carros. Essa parte é a mesma, não importa se você está procurando verde, vermelho ou carros fabricados antes de 2015.

O que muda é a **regra de teste**, a decisão: _Este carro passa no filtro ou não?_

Em Java, para passar uma "regra" ou um "comportamento" como se fosse um dado (como um número ou uma `String`), nós usamos **Interfaces**.

1. Você cria uma **interface** que define o _contrato_ da regra, por exemplo, `CarroPodePassar`. Essa interface tem um único método, tipo `boolean testar(Carro carro)`.
2. Sua função principal de filtragem (`filtrar(Lista<Carro> lista, CarroPodePassar regra)`) aceita essa interface como parâmetro.
3. O código de filtragem não se importa _qual_ é a regra; ele só chama o método `testar()` da regra que foi passada: `if (regra.testar(carro)) { // adiciona }`.

Você separou a **ação** (percorrer a lista) do **comportamento** (a condição de filtro). Essa separação permite a **reutilização de código** e melhora a **flexibilidade** do sistema. Você pode ter dez filtros diferentes (verde, vermelho, antigo, novo), mas usar apenas um método principal de filtragem.

Em Java moderno, essa interface com um único método é geralmente implementada de forma rápida usando **Expressões Lambda** (ou, tecnicamente, Classes Anônimas otimizadas).

## Exemplo com Código (Java)

Vamos criar um exemplo usando Generics (`<T>`) para que o método de filtragem possa funcionar com _qualquer_ tipo de lista (não apenas carros), e um **Predicate** (um contrato para a regra de teste, semelhante ao que o Java utiliza internamente em `java.util.function.Predicate`).

### 1. Classes e Interface de Comportamento

```java
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate; // Usaremos a interface funcional nativa do Java

// 1. Classe de Objeto (O 'Carro' que queremos filtrar)
class Carro {
    private String nome;
    private String cor;
    private int ano;

    public Carro(String nome, String cor, int ano) { //
        this.nome = nome;
        this.cor = cor;
        this.ano = ano;
    }

    // Getters para acessar os atributos (omiti setters por simplicidade)
    public String getCor() { return cor; }
    public int getAno() { return ano; }

    @Override
    public String toString() { // Sobrescrevemos para facilitar a impressão
        return "Carro{" + "nome='" + nome + '\'' + ", cor=" + cor + ", ano=" + ano + '}';
    }
}

// 2. Classe de Serviço com o Método Parametrizado
public class ComportamentoParametrizadoTest {

    // Método genérico <T> que aceita uma lista de qualquer tipo T
    // e um Predicate<T> (a 'regra' que será aplicada ao objeto T)
    public static <T> List<T> filtrar(List<T> lista, Predicate<T> condicao) { //
        List<T> listaFiltrada = new ArrayList<>(); // Cria uma nova lista vazia para o resultado

        // Percorre cada elemento da lista original (o loop, a 'ação' fixa)
        for (T elemento : lista) {
            // Aplica a condição/regra que foi passada como parâmetro ('comportamento')
            if (condicao.test(elemento)) {
                listaFiltrada.add(elemento); // Se a regra retornar true, o elemento é adicionado
            }
        }
        return listaFiltrada; // Retorna a nova lista com os elementos que passaram no teste
    }

    public static void main(String[] args) {
        // Criando a lista de carros (o nosso 'banco de dados')
        List<Carro> catalogo = List.of(
            new Carro("Audi", "Verde", 2011),
            new Carro("Fusca", "Preto", 1998),
            new Carro("Ferrari", "Vermelho", 2019),
            new Carro("Fiat", "Verde", 2014),
            new Carro("BMWros Antigos (Antes de 2015) ---
        // A 'ação' (o método filtrar) é a mesma, mas a 'regra' (o comportamento) é diferente
        Predicate<Carro> isOld = carro -> carro.getAno() < 2015;
        List<Carro> antigos = filtrar(catalogo, isOld);
        System.out.println("Carros Antigos: " + antigos);

        // --- Parametrizando o 3º Comportamento: Números Pares (Mostrando Generics com Inteiros) ---
        List<Integer> numeros = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

        // O método 'filtrar' funciona perfeitamente com Integer, graças ao <T>
        Predicate<Integer> isEven = num -> num % 2 == 0;
        List<Integer> pares = filtrar(numeros, isEven);
        System.out.println("Números Pares: " + pares);
    }
}
```

### Explicação Linha por Linha do Código Chave

- `public static <T> List<T> filtrar(List<T> lista, Predicate<T> condicao)`:
    - `static <T>`: Declara que este é um **método genérico** que trabalha com um tipo genérico `T` (Type). Isso torna o método reutilizável para _qualquer_ tipo de objeto, como `Carro` ou `Integer`.
    - `Predicate<T> condicao`: Este é o segredo! O método espera receber uma **regra de teste** (`Predicate`) que sabe como testar objetos do tipo `T`. `Predicate` é uma interface funcional do Java introduzida na versão 8, que define o comportamento.
- `if (condicao.test(elemento))`: Esta linha é a **parametrização em ação**. Em vez de usar um `if` fixo (como `if (elemento.getCor().equals("Verde"))`), o código chama o método `test()` da _regra_ que foi passada. O comportamento (a lógica do filtro) é externo e flexível.
- `Predicate<Carro> isGreen = carro -> carro.getCor().equalsIgnoreCase("Verde");`:
    - Aqui criamos a implementação específica da regra (o **comportamento**).
    - A sintaxe `carro -> carro.getCor().equalsIgnoreCase("Verde")` é uma **Expressão Lambda**. Ela é uma forma concisa de escrever uma implementação da interface `Predicate` (ou de qualquer interface funcional, que tenha um único método).
    - O código diz: "Para qualquer `carro` que você me der, teste se a cor dele é igual a 'Verde'".

A beleza é que você pode reescrever a regra (`isOld`, `isEven`) sem nunca ter que tocar ou duplicar o código do método `filtrar`.

## Aplicação no Mundo Real

A parametrização de comportamentos é essencial para escrever software de alta qualidade em Java, especialmente em sistemas grandes onde a **flexibilidade** e a **manutenibilidade** são cruciais.

1. **Frameworks de Coleções e Ordenação:**
    
    - No dia a dia, você não vai apenas filtrar, mas também ordenar. Para ordenar listas ou _arrays_ de objetos complexos (como uma lista de `Produto` por `preco` e depois por `nome`), você precisa passar a regra de comparação. Isso é feito parametrizando o comportamento de ordenação, geralmente com as interfaces **`Comparator`** ou **`Comparable`**.
    - A classe `Collections.sort()` aceita um `Comparator` como parâmetro para saber _como_ ordenar os objetos. Você define o comportamento de ordenação, e o método `sort()` aplica esse comportamento.
2. **Camada de Acesso a Dados (DAO/Repository):**
    
    - Em arquiteturas empresariais (como as que usam JPA/Hibernate), você frequentemente precisa criar uma camada de acesso a dados (DAO - Data Access Object) que lida com operações básicas de banco (buscar, salvar).
    - Para evitar métodos como `findByNome()`, `findByCpf()`, `findByDataNascimento()`, você pode ter um método genérico `buscarPorCondicao(String query, Map<String, Object> parametros)`.
    - Nesse caso, a **query** (a regra SQL) e os **parâmetros** (os dados de teste) são passados como parâmetros, parametrizando o comportamento de busca do DAO. Isso permite que a camada DAO seja **totalmente genérica** e atenda a todos os tipos de busca.
3. **Processamento de Eventos e Interfaces Gráficas (UI/UX):**
    
    - Em aplicações mais antigas de Java (Swing/JavaFX) e modernamente em _listeners_ de eventos, o comportamento do que deve acontecer quando um botão é clicado (`onClick`) é parametrizado.
    - O código que lida com o clique não sabe o que fazer; ele apenas executa o comportamento (`actionPerformed` ou similar) que lhe foi passado. Esse comportamento é frequentemente definido usando uma **Classe Anônima** ou uma **Expressão Lambda** que implementa uma interface de _listener_.
4. **Filas de Prioridade (`PriorityQueue`):**
    
    - A `PriorityQueue` (Fila de Prioridade) armazena elementos, mas quando você remove (`poll()` ou `remove()`), ela sempre remove o elemento de maior prioridade.
    - Mas como ela sabe qual elemento é mais importante? Você define a regra de "prioridade" passando um **`Comparator`**. Por exemplo, você pode querer que a prioridade seja o `preço` (removendo os mais caros primeiro, como em um carrinho de compras com prioridade de valor) ou a `data`. Você está parametrizando o comportamento da fila.

A parametrização de comportamento, impulsionada por **polimorfismo** (a capacidade de diferentes objetos responderem à mesma chamada de método, como o `test()`) e **generics** (que garantem a segurança de tipos para que você só passe regras para os objetos certos), leva a um código que é mais fácil de testar, manter e estender.

## Resumo Rápido

|Conceito|Descrição|
|:--|:--|
|**O que é?**|Passar a lógica ou a regra de decisão (o "o quê") como um parâmetro para um método (a "ação").|
|**Como fazer?**|Usando interfaces funcionais (`Predicate`, `Comparator`) implementadas via **Expressões Lambda** ou **Classes Anônimas**.|
|**Vantagem**|Alta reutilização de código e flexibilidade, separando a regra de negócio da lógica de infraestrutura (como _loops_ ou acesso a dados).|
