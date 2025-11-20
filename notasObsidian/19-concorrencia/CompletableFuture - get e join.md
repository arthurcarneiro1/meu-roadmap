
---

### Explicação Feynman: CompletableFuture - get e join

Pense no `CompletableFuture` como um bilhete que você recebe quando encomenda um hambúrguer. Ele representa um **resultado futuro de uma computação assíncrona**. Você deu a tarefa para a cozinha (uma _thread_ separada), e a _thread_ principal pode ir fazer outra coisa enquanto espera.

O que são `get()` e `join()`?

Eles são como ir ao balcão e dizer: "Me dá o meu hambúrguer _agora_!". Ambos são métodos usados para **esperar que a computação assíncrona termine e obter o resultado**. Ao chamá-los, a _thread_ que está chamando (a _thread_ principal, por exemplo) **bloqueia** e fica parada, esperando o resultado do `CompletableFuture`.

**A GRANDE diferença está no "como" eles lidam com problemas (exceções):**

1. **`get()`:**
    
    - Este método é o cara das regras antigas. Ele foi herdado da interface `Future`, que é mais antiga (Java 5).
    - Se algo der errado, o `get()` te obriga a lidar com a burocracia. Ele lança **exceções checadas** (_checked exceptions_), como `InterruptedException` e `ExecutionException`.
    - No Java, isso significa que você é **obrigado a declarar ou tratar essas exceções** explicitamente (usando `try-catch` ou `throws` na assinatura do método).
    - O `get()` também é flexível e permite que você defina um **tempo limite** (_timeout_) para a espera. Se o resultado não vier a tempo, ele lança uma `TimeoutException`.
2. **`join()`:**
    
    - Este é o método moderno, introduzido junto com o `CompletableFuture` no Java 8.
    - Ele é mais simples porque **não lança exceções checadas**. Se a computação falhar, ele lança uma **exceção não checada** (_unchecked exception_), especificamente a `CompletionException`.
    - Como é uma exceção não checada, **você não é obrigado a tratá-la**. Isso torna o código mais limpo e conciso.
    - Entretanto, o `join()` **não oferece suporte nativo para tempo limite** (_timeout_).

Em resumo, `join()` é **preferível para código novo** devido à sua simplicidade, evitando o incômodo do `try-catch` obrigatório.

---

### Exemplo com Código: `get` vs. `join`

Abaixo, um exemplo simples em Java demonstrando o uso e a principal diferença no tratamento de exceções.

```
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.CompletionException;

public class CompletableFutureExample {

    // Método que simula uma computação assíncrona que retorna um Double
    public static CompletableFuture<Double> calcularPrecoAsync(String loja, long delaySegundos) {
        // supplyAsync executa a lógica em uma thread separada, geralmente de um ForkJoinPool
        return CompletableFuture.supplyAsync(() -> { //
            System.out.println("Thread " + Thread.currentThread().getName() + " buscando preço para " + loja);
            try {
                // Simula um delay de rede ou processamento
                Thread.sleep(delaySegundos * 1000);

                if (loja.equals("Loja_Erro")) {
                    // Simula uma falha de conexão ou erro de negócio
                    throw new RuntimeException("Falha ao consultar Loja_Erro");
                }

                return 100.0 * delaySegundos; // Retorna um preço
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new CompletionException(e);
            }
        });
    }

    public static void main(String[] args) throws InterruptedException {

        // 1. Usando join(): Simples e sem necessidade de try-catch obrigatório
        System.out.println("--- Testando join() ---");
        CompletableFuture<Double> futuroJoin = calcularPrecoAsync("Loja_A", 1);

        // O método join() bloqueia a thread 'main' esperando o resultado
        // Se houver exceção, ela seria lançada como CompletionException (unchecked)
        try {
            Double resultadoJoin = futuroJoin.join();
            System.out.println("Resultado com join(): " + resultadoJoin);
        } catch (CompletionException e) {
             // Tratamento opcional para a CompletionException
             System.out.println("ERRO (join): " + e.getCause().getMessage());
        }

        System.out.println("--- Testando get() ---");
        CompletableFuture<Double> futuroGet = calcularPrecoAsync("Loja_Erro", 1);

        // 2. Usando get(): Requer tratamento das exceções checadas
        // A assinatura do método main precisaria declarar 'throws InterruptedException, ExecutionException'
        try {
            // O método get() bloqueia a thread 'main' esperando o resultado
            Double resultadoGet = futuroGet.get();
            System.out.println("Resultado com get(): " + resultadoGet);
        } catch (InterruptedException | ExecutionException e) { // Obrigatoriamente precisamos tratar ou declarar estas exceções checadas
            System.out.println("ERRO (get): " + e.getCause().getMessage());
        }

        System.out.println("--- Testando get(timeout) ---");
        CompletableFuture<Double> futuroTimeout = calcularPrecoAsync("Loja_Lenta", 5);

        try {
            // Tentamos esperar apenas por 1 segundo, mas a tarefa levará 5 segundos
            // O get() suporta timeout
            Double resultadoTimeout = futuroTimeout.get(1, TimeUnit.SECONDS);
            System.out.println("Resultado com get(timeout): " + resultadoTimeout);
        } catch (InterruptedException | ExecutionException e) {
            System.out.println("ERRO: " + e.getCause().getMessage());
        } catch (TimeoutException e) { // Captura a exceção de timeout
            System.out.println("TIMEOUT: O cálculo demorou demais e a thread principal parou de esperar.");
        }

        // Se estivéssemos usando um ExecutorService que criamos (e não o pool padrão),
        // precisaríamos garantir que ele seja encerrado.
    }
}
```

**Explicação Linha por Linha:**

- `calcularPrecoAsync(String loja, long delaySegundos)`: Define uma tarefa que será executada de forma assíncrona. O `CompletableFuture.supplyAsync()` é usado para iniciar a execução em uma _thread_ do _thread pool_ (como o `ForkJoinPool` padrão).
- `Thread.sleep(delaySegundos * 1000)`: Simula um trabalho demorado, como uma requisição de rede.
- `futuroJoin.join()`: **Bloqueia** a _thread_ principal até que o preço esteja disponível. Se a tarefa falhar (`Loja_Erro`), ele lança a `CompletionException` (não checada).
- `futuroGet.get()`: **Bloqueia** a _thread_ principal até o resultado. Se a tarefa falhar, ele lança a `ExecutionException` (checadas), exigindo o `try-catch` explícito.
- `futuroTimeout.get(1, TimeUnit.SECONDS)`: Demonstra a capacidade do `get()` de definir um limite de espera. Se o tempo esgotar antes da conclusão, uma `TimeoutException` (checadas) é lançada.

---

### Aplicação no Mundo Real

Na prática profissional de um desenvolvedor Java, especialmente em sistemas concorrentes e de alta performance, o `CompletableFuture` é crucial.

O principal uso de `get()` e `join()` é em situações onde o código assíncrono precisa ser sincronizado de volta ao fluxo principal, mas você quer o mínimo de bloqueio possível, ou precisa de um controle estrito sobre exceções.

**Exemplo: Agregadores de Preços ou Microssegmentação (E-commerce/Finanças)**

Imagine que sua aplicação precisa consultar os preços de um produto em **quatro lojas** diferentes (A, B, C e D) para retornar o melhor preço ao usuário.

- **Abordagem Síncrona (Sem CompletableFuture):** Sua _thread_ principal consulta Loja A (2s), depois Loja B (2s), depois Loja C (2s) e Loja D (2s). Tempo total de resposta: 8 segundos. Você perde o cliente.
- **Abordagem Assíncrona (Com CompletableFuture):**
    1. A _thread_ principal dispara 4 tarefas assíncronas simultaneamente (Loja A, B, C, D) usando `CompletableFuture`.
    2. Cada loja é consultada por uma _thread_ separada (gerenciada por um _Executor_).
    3. A _thread_ principal usa uma função como `CompletableFuture.allOf()` para esperar que todas as tarefas terminem, e em seguida, usa **`join()`** para coletar os resultados de forma não burocrática e combiná-los.
    4. Tempo total de resposta (assumindo que todas levam 2s): **Aproximadamente 2 segundos** (tempo da loja mais lenta).

Neste cenário, **`join()` é amplamente preferido em código novo** porque a burocracia de declarar `throws ExecutionException` em toda a cadeia de chamadas ao usar `get()` é desnecessária e suja o código.

Se, no entanto, você estivesse em um sistema de controle de missão crítica (como um sistema bancário que precisa de tempos de espera estritos), e quisesse garantir que a consulta não levasse mais de 5 segundos, você usaria o **`get(5, TimeUnit.SECONDS)`**. Se o tempo estourar, a _thread_ principal lida imediatamente com a `TimeoutException`.

---

### Resumo Rápido

O `join()` e o `get()` são usados para **bloquear** a _thread_ atual e obter o resultado de um `CompletableFuture`. O **`join()`** é preferido em código moderno, lançando apenas a exceção **não checada** `CompletionException`. O **`get()`** exige tratamento de exceções **checadas** (`ExecutionException`, `InterruptedException`), mas suporta a funcionalidade de **tempo limite** (_timeout_).

---

**Analogia Final:**

Imagine que você está no meio de uma reunião importante (sua _thread_ principal). Você manda um colega ir buscar um relatório que está demorado (tarefa assíncrona).

- Chamar **`join()`** é como esperar pelo colega e, se ele voltar com o relatório rasgado, você lida com a confusão imediatamente, sem ter que avisar o chefe antes (exceção não checada).
- Chamar **`get()`** é como esperar pelo colega, mas se ele voltar com o relatório rasgado, você precisa parar a reunião, pegar uma autorização assinada pelo chefe para registrar o erro na ata (exceção checada obrigatória), para só então voltar ao trabalho. Mas, se você usou o `get(timeout)`, você pode dizer: "Se ele não voltar em 10 minutos, eu saio e continuo sem o relatório" (suporte a _timeout_).