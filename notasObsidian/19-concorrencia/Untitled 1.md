

---

### Explicação Feynman

Vamos começar pelo básico. O que é uma **Thread**?

Pense no seu programa como uma linha de montagem. Uma _thread_ é um operário que executa uma tarefa (pode ser um objeto ou uma linha de execução). Se você tem apenas um operário (uma thread), ele faz as tarefas uma após a outra, de forma **síncrona**. Se ele precisa ir buscar uma peça que demora 2 segundos para chegar, ele para, espera os 2 segundos, pega a peça e só então continua. O programa fica _bloqueado_.

O **CompletableFuture** é a solução para isso, e é uma ferramenta de **programação assíncrona**.

Quando você usa um `CompletableFuture`, você não está esperando pela peça. Você está dizendo: "Eu preciso desta peça, mas ela vai demorar. Vou contratar um **assistente** (uma outra thread) para ir buscar. Ele me entrega um 'cheque pré-datado' (o `CompletableFuture`) e eu continuo meu trabalho principal.". O seu programa principal não para e continua executando outras coisas.

O `CompletableFuture` representa o resultado que virá no futuro de um cálculo assíncrono. Quando você precisa do resultado desse "cheque pré-datado", você pode usar o método:

1. **`.get()`**: Ele te força a tratar as exceções _verificadas_ (`InterruptedException`, `ExecutionException`, `TimeoutException`).
2. **`.join()`**: Este é geralmente o preferido para novos códigos. Ele faz o mesmo que o `.get()`, mas lança uma exceção _não verificada_ (`CompletionException`) em caso de erro, simplificando o código, pois você não precisa declarar ou capturar explicitamente as exceções verificadas.

#### A Receita Secreta: CompletableFuture e Streams

Agora, o pulo do gato é combinar isso com **Streams**.

Imagine que você tem uma **lista de tarefas** (suas lojas) e quer disparar todas elas assincronamente usando `CompletableFuture` e processar a lista usando a API de `Stream` (Java 8).

Você pode cair em uma armadilha: as operações _intermediárias_ de um _Stream_ são geralmente _lazy_ (preguiçosas). Se você tentar obter o resultado final (`.join()`) dentro da mesma cadeia de Stream que iniciou o trabalho assíncrono, a operação final (que precisa do resultado) pode acabar forçando a execução a se tornar **síncrona** (uma tarefa por vez), anulando o benefício da concorrência.

**A solução é a separação de responsabilidades:**

1. **Crie as Promessas (Asynchronous Initiation):** Use o `Stream` para mapear sua lista inicial e _iniciar_ o trabalho assíncrono para cada item (gerando uma `List<CompletableFuture<T>>`).
2. **Obtenha os Resultados (Blocking Retrieval):** Use um _novo Stream_ sobre a lista de `CompletableFuture`s para então chamar o `.join()` em cada um, bloqueando apenas pelo tempo necessário para que **todas** as tarefas em segundo plano terminem.

Dessa forma, você inicia todos os assistentes ao mesmo tempo, e a espera total será o tempo da tarefa mais longa, não a soma de todas elas.

---

### Exemplo com código

Vamos simular o problema de buscar preços em várias lojas (que é uma operação lenta devido à latência de rede) de forma síncrona e assíncrona usando `CompletableFuture` com `Stream`.

```
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

// Classe de Serviço que simula uma chamada externa lenta
class StoreService {
    // Simula um delay de 2 segundos (conexão lenta)
    private void delay() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    // Método que gera um preço de forma assíncrona
    // (usado com CompletableFuture)
    public CompletableFuture<Double> getPriceAsync(String storeName) {
        // supplyAsync executa a lógica de forma assíncrona, usando o ForkJoinPool
        // ou um ExecutorService definido.
        return CompletableFuture.supplyAsync(() -> {
            System.out.println("Thread " + Thread.currentThread().getName() + " buscando preço na " + storeName);
            delay(); // Simula a latência de rede
            // Retorna um preço aleatório
            return Math.random() * 100;
        });
    }

    // Método síncrono (para comparação)
    public Double getPriceSync(String storeName) {
        System.out.println("Thread " + Thread.currentThread().getName() + " buscando preço (síncrono) na " + storeName);
        delay();
        return Math.random() * 100;
    }
}

public class CompletableFutureStreamTest {

    public static void main(String[] args) {
        StoreService storeService = new StoreService();
        List<String> stores = Arrays.asList("Loja A", "Loja B", "Loja C", "Loja D");

        long start = System.currentTimeMillis();

        // 1. EXECUÇÃO SÍNCRONA (para comparação)
        // System.out.println("\n--- Iniciando Busca Síncrona ---");
        // stores.forEach(storeService::getPriceSync);
        // System.out.println("Tempo Síncrono: " + (System.currentTimeMillis() - start) / 1000.0 + " segundos");

        // 2. EXECUÇÃO ASSÍNCRONA CORRETA COM STREAMS (em paralelo)
        System.out.println("\n--- Iniciando Busca Assíncrona ---");

        // PASSO 1: Criamos o Stream e disparamos as tarefas assíncronas.
        // O map transforma cada nome de loja em um CompletableFuture (uma promessa).
        List<CompletableFuture<Double>> priceFutures = stores.stream()
                .map(storeService::getPriceAsync) // Dispara 4 tarefas em paralelo
                .collect(Collectors.toList()); // Coleta as promessas (CompletableFutures)

        // Neste ponto, a thread principal NÃO está bloqueada.
        // As 4 buscas estão ocorrendo em paralelo.

        // PASSO 2: Criamos um novo Stream na lista de CompletableFuture's e esperamos
        // pelo resultado de cada um.
        List<Double> prices = priceFutures.stream()
                .map(CompletableFuture::join) // Espera (bloqueia) até que cada promessa se complete
                .collect(Collectors.toList());

        long end = System.currentTimeMillis();

        System.out.println("Preços encontrados: " + prices);
        System.out.println("Tempo Assíncrono: " + (end - start) / 1000.0 + " segundos");
    }
}

/*
SAÍDA ESPERADA:

--- Iniciando Busca Assíncrona ---
Thread ForkJoinPool.commonPool-1 buscando preço na Loja A
Thread ForkJoinPool.commonPool-2 buscando preço na Loja B
Thread ForkJoinPool.commonPool-3 buscando preço na Loja C
Thread ForkJoinPool.commonPool-4 buscando preço na Loja D
Preços encontrados: [23.45, 87.65, 45.32, 12.98]
Tempo Assíncrono: ~2.00 segundos
*/
```

**Explicação Linha por Linha do Fluxo Assíncrono:**

- `List<CompletableFuture<Double>> priceFutures = stores.stream().map(storeService::getPriceAsync).collect(Collectors.toList());`:
    - Esta é a parte crucial. O `map` recebe cada nome de loja.
    - O `storeService::getPriceAsync` é executado, retornando imediatamente um `CompletableFuture` para cada loja.
    - O `supplyAsync` dentro de `getPriceAsync` delega a busca (e os 2 segundos de `delay()`) para uma _thread_ separada (geralmente do `ForkJoinPool` do Java).
    - A lista `priceFutures` armazena as **quatro promessas** que estão sendo resolvidas em paralelo no fundo.
- `List<Double> prices = priceFutures.stream().map(CompletableFuture::join).collect(Collectors.toList());`:
    - Aqui, a thread principal agora precisa dos resultados.
    - O `.join()` bloqueia a thread de execução até que a promessa associada esteja completa. Como todas as quatro promessas foram iniciadas quase simultaneamente no Passo 1, a espera total é determinada pela tarefa mais lenta (2 segundos), e não a soma (8 segundos).

---

### Aplicação no mundo real

O conceito de assincronismo com `CompletableFuture` e a gestão eficiente de coleções via `Streams` é fundamental em qualquer sistema **escalável**:

1. **Microsserviços e APIs (O Problema da Latência):**
    
    - **Cenário:** Um sistema de e-commerce precisa buscar o preço final de um produto. Para isso, ele precisa consultar o estoque (Serviço A), calcular o frete (Serviço B) e aplicar cupons (Serviço C). Se esses serviços forem chamados sequencialmente, a latência se soma (e.g., 500ms + 300ms + 400ms = 1.2 segundos).
    - **Solução:** Usando `CompletableFuture`, o sistema dispara as consultas A, B e C em paralelo. Se as buscas levarem 500ms, 300ms e 400ms, a resposta total virá em cerca de 500ms. Isso melhora drasticamente a **responsividade** do aplicativo.
2. **Processamento de Lotes Assíncronos:**
    
    - Em sistemas de ETL (Extração, Transformação e Carga) ou envio de notificações, é comum ter que processar uma grande lista de tarefas (e.g., 1000 e-mails).
    - Em vez de iterar de forma síncrona, as tarefas são mapeadas para uma `List<CompletableFuture<Void>>` (se não houver retorno) e monitoradas. Isso aproveita melhor os recursos do processador, mantendo o sistema responsivo, especialmente em operações que envolvem I/O (entrada/saída).
3. **Interfaces de Usuário (Antigos Monolitos ou Aplicações Desktop):**
    
    - Em sistemas que gerenciam uma interface gráfica (UI), se uma operação lenta (como gerar um relatório complexo) for executada na thread principal (UI thread), a tela do usuário **trava**.
    - `CompletableFuture` permite executar o processamento do relatório em uma thread de _background_ (assíncrona) enquanto a thread da UI permanece livre, garantindo que a interface continue interativa.

---

### Resumo rápido

|Conceito|Descrição|
|:--|:--|
|**CompletableFuture**|É uma "promessa" para o resultado de uma operação assíncrona, permitindo que a thread principal continue trabalhando.|
|**CF + Streams**|Permite iniciar múltiplas tarefas assíncronas em paralelo a partir de uma coleção, reduzindo a latência total ao invés de somá-la.|
|**Melhor Prática**|Para garantir o paralelismo em Streams, separe o mapeamento que inicia as promessas do mapeamento final que chama `.join()` para coletar os resultados.|

O uso eficaz de `CompletableFuture` com `Streams` é como ter uma equipe de assistentes trabalhando simultaneamente para buscar informações, garantindo que a sua linha de produção não pare e que você entregue resultados muito mais rapidamente. É a chave para a programação moderna orientada à performance em Java.