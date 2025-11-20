
---

### Explicação Feynman

Imagine que você tem uma pilha gigantesca de documentos para processar (sua coleção de dados).

Um _Stream Sequencial_ (ou serial) é como se você tivesse **apenas uma pessoa** (uma _thread_) trabalhando na pilha, processando documento por documento em ordem. Se a pilha for enorme, isso será muito lento.

Um **Parallel Stream** (Stream Paralela) é uma abstração fornecida pela API Streams do Java 8 que permite que essa tarefa seja **dividida automaticamente** em subtarefas e executada simultaneamente em **múltiplos trabalhadores** (_threads_). A Java esconde a complexidade de gerenciar essas _threads_.

#### Como Processam Elementos Simultaneamente (Múltiplos Núcleos)

1. **Divisão da Tarefa (Fork):** O _stream_ grande é quebrado em _substreams_ menores. Se você tem 8 núcleos de CPU (que é o padrão para o número de _threads_ usadas, obtido por `Runtime.getRuntime().availableProcessors()`), o Java tentará dividir o trabalho em 8 partes ou mais.
2. **Processamento Distribuído:** Cada sub-tarefa é designada a uma _thread_ separada, que pode ser executada em um núcleo de processador diferente.
3. **Balanceamento de Carga:** O pool de _threads_ padrão utilizado é o **ForkJoinPool**. Este pool usa uma técnica chamada "_work-stealing_" (roubo de trabalho), onde _threads_ que terminaram suas tarefas e estão ociosas ativamente **"roubam" tarefas** das filas de _threads_ mais ocupadas, garantindo que todos os núcleos permaneçam ocupados e o processamento seja eficiente.
4. **Combinação (Join):** No final, os resultados parciais de todas as _threads_ são combinados (_combiner_) para produzir um único resultado final.

#### Quando Usar ou Evitar

|Cenário|Uso de Parallel Streams|Explicação|
|:--|:--|:--|
|**Tarefas Intensivas em CPU**|**USE**|Se o seu cenário é _CPU bound_ (envolve muitos cálculos complexos, transformações ou processamento de dados). Nesses casos, utilizar múltiplos núcleos maximiza o uso dos recursos e melhora a performance, especialmente com **grandes massas de dados**.|
|**Pequena Massa de Dados**|**EVITE**|Se a quantidade de dados for muito pequena, o _overhead_ (custo de coordenação, inicialização do pool de _threads_, divisão de tarefas e combinação de resultados) **supera o ganho de paralelismo**, tornando o _stream_ sequencial mais rápido (pode ser até 200x mais lento em casos extremos).|
|**Operações de I/O**|**EVITE**|Se a operação é _I/O bound_ (escrita de arquivos, chamadas de rede/microserviços). O paralelismo é limitado pela espera por I/O, e não pela capacidade de cálculo da CPU.|
|**Operações com Dependência de Ordem**|**EVITE**|Funções como `findFirst()` ou `limit()` não são ideais, pois exigem manter a ordem ou a predição do resultado, o que anula os benefícios do processamento paralelo. `findAny()` é preferível em streams paralelas, pois não garante a ordem.|
|**Lambdas Stateful**|**EVITE**|Expressões _lambda_ que dependem de um estado externo que pode mudar durante a execução paralela (_stateful_), como adicionar elementos a uma coleção, podem levar a comportamentos indeterminados e inesperados.|

---

### Exemplo com Código

O exemplo mais eficaz para demonstrar o poder das _Parallel Streams_ é a **redução (soma)** em um volume grande de dados (onde o tempo de processamento é _CPU bound_).

A operação de redução (como a soma) em _streams_ paralelas utiliza o método `reduce(identidade, acumulador, combinador)`.

Vamos simular a soma de 10 milhões de números para ilustrar o conceito:

```java
import java.util.stream.LongStream;
import java.util.stream.IntStream;
import java.util.stream.Collectors;

public class ParallelStreamExample {

    public static void main(String[] args) {
        long limite = 10_000_000;

        // 1. Criando um LongStream sequencial com tamanho pré-definido
        // (Isso permite que o Java divida o trabalho de forma eficaz no modo paralelo)
        LongStream streamSequencial = LongStream.rangeClosed(1, limite);

        // 2. Criando o Stream Paralelo (adicionando a flag parallel)
        LongStream streamParalelo = LongStream.rangeClosed(1, limite).parallel();

        // Demonstração da Soma Sequencial (para comparação de performance)
        long inicioSequencial = System.currentTimeMillis();
        long somaSequencial = streamSequencial.sum();
        long tempoSequencial = System.currentTimeMillis() - inicioSequencial;
        System.out.println("Soma Sequencial: " + somaSequencial + " (Tempo: " + tempoSequencial + " ms)");

        // 3. Processar elementos em paralelo e coletar resultados (Reduce/Sum)
        // A operação terminal sum() é uma forma de reduce.
        long inicioParalelo = System.currentTimeMillis();
        long somaParalela = streamParalelo.sum();
        long tempoParalelo = System.currentTimeMillis() - inicioParalelo;
        System.out.println("Soma Paralela: " + somaParalela + " (Tempo: " + tempoParalelo + " ms)");

        // 4. Exemplo conceitual de Map e Filter (usando um stream de objetos simples)
        // Adaptando a lista para um exemplo conceitual de Map/Filter/Reduce
        List<Integer> numeros = IntStream.rangeClosed(1, 100).boxed().collect(Collectors.toList());

        int resultadoReduzido = numeros.parallelStream() // Cria o stream paralelo
            .filter(n -> n % 2 == 0)                   // Filtra apenas números pares
            .map(n -> n * 2)                           // Mapeia (transforma) o elemento
            .reduce(0, Integer::sum);                  // Reduz (soma) os resultados parciais

        System.out.println("Resultado Map/Filter/Reduce Paralelo: " + resultadoReduzido);
    }
}
```

#### Explicação Linha a Linha das Operações (`.parallelStream()`, `.filter()`, `.map()`, `.reduce()`):

|Código|Explicação|Citação(ões)|
|:--|:--|:--|
|`LongStream.rangeClosed(1, limite).parallel()`|**Criação do Stream Paralelo:** Usa o método `parallel()` sobre um stream de longa (`LongStream`) para indicar que as operações subsequentes devem ser executadas em paralelo. Alternativamente, `List.parallelStream()` pode ser usado diretamente em uma coleção.||
|`.filter(n -> n % 2 == 0)`|**Operação Intermediária (Filter):** Filtra os elementos, permitindo que apenas aqueles que satisfazem o predicado passem para o próximo estágio. Em um _stream_ paralelo, essa filtragem pode ocorrer simultaneamente em diferentes subconjuntos de dados.||
|`.map(n -> n * 2)`|**Operação Intermediária (Map):** Transforma cada elemento de entrada (o número filtrado) em um resultado de saída (o número dobrado). Esta transformação é ideal para paralelismo (CPU bound).||
|`.reduce(0, Integer::sum)`|**Operação Terminal (Reduce):** Combina os elementos do _stream_ em um único resultado (neste caso, a soma). O primeiro argumento (`0`) é a identidade (valor inicial). O segundo argumento (`Integer::sum`) é o acumulador, que soma o resultado parcial com o próximo elemento. Embora não seja explicitamente necessário, para reduções em _streams_ paralelos, um terceiro parâmetro (o combinador) é crucial para mesclar os resultados parciais produzidos pelas diferentes _threads_.||
|`.sum()` (em `LongStream`)|**Operação Terminal (Redução Primitiva):** Em _streams_ de tipos primitivos (como `LongStream`), métodos como `sum()` realizam a redução de forma otimizada, evitando _boxing_ e _unboxing_ (o que é importante para alta performance).||

---

### Aplicação no Mundo Real

A utilização de _Parallel Streams_ é vantajosa em cenários que envolvem **grandes volumes de dados** e processamento intensivo de CPU, onde o custo do paralelismo é justificado.

1. **Processamento de Grandes Listas e Transformação de Dados (CPU-Bound):**
    
    - Empresas que lidam com milhões de registros em memória (como listas de clientes, produtos ou inventários) e precisam realizar cálculos complexos ou transformações em cada item se beneficiam do paralelismo. Por exemplo, recálculo de preços complexos que dependem de múltiplas variáveis.
2. **Análise de Vendas e Transações Financeiras:**
    
    - **Cálculo de Estatísticas:** Usar coletores (Collectors) para calcular rapidamente **estatísticas de resumo** (`summarizingDouble/Int/Long`) de grandes volumes de transações, como o valor mínimo, máximo, média e soma total de vendas ou custos.
    - **Agrupamento e Agregação:** Agrupar transações financeiras por categoria de produto ou tipo de conta (`groupingBy`) e, em seguida, calcular a soma ou média do valor transacionado para cada grupo. O uso de _streams_ paralelos pode acelerar drasticamente essa agregação em grandes _datasets_.
3. **Processamento de Logs (Filtragem e Contagem Rápida):**
    
    - Em sistemas que geram logs massivos, _Parallel Streams_ podem ser usadas para **filtrar rapidamente** (usando `.filter()`) os logs com base em critérios complexos e depois **contar** (`counting()`) ou **agrupar** (`groupingBy()`) os eventos encontrados.

---

### Resumo Rápido

O **Parallel Stream** divide o processamento de coleções em múltiplas _threads_ simultâneas, usando os núcleos da CPU. Use-o para operações **CPU-bound** (cálculos) em coleções **grandes** para obter ganhos de performance. Ele acelera operações ao distribuir a carga de trabalho intensiva, como um grande cálculo de soma, entre vários processadores.