
## CompletableFuture e ThreadFactory

### Explicação Feynman

#### CompletableFuture: O Garçom Rápido da Computação Assíncrona

Imagine que você está em uma lanchonete (seu programa) e pede um sanduíche gourmet (uma tarefa que leva tempo, como buscar dados em uma API).

Na programação síncrona tradicional, você faria o pedido e ficaria parado na frente do balcão, bloqueando a fila, até o sanduíche ficar pronto. Nada mais poderia ser feito.

O **`CompletableFuture`** muda esse jogo. Ele é como um garçom super eficiente. Você faz o pedido (`supplyAsync`), e o garçom imediatamente lhe dá um bilhete (`CompletableFuture`) e avisa: "Seu sanduíche será preparado por alguém na cozinha (outra _thread_) e eu te aviso quando estiver pronto".

Com esse bilhete, você pode ir fazer outras coisas (executar código na _thread_ principal). Quando a tarefa na cozinha terminar, você usa o bilhete (`join()` ou `get()`) para pegar o resultado. Se o resultado demorar, você pode até definir um limite de tempo para esperar (`get(timeout, unit)`).

O grande truque é que ele permite que o seu código **não fique bloqueado** esperando, melhorando a responsividade e a escalabilidade da sua aplicação.

#### ThreadFactory: O Mestre Construtor de Threads

O **`ThreadFactory`** é a fábrica de operários (Threads). Quando você usa um `ExecutorService` (um gerenciador de pools de threads), ele precisa de trabalhadores para executar as tarefas assíncronas.

O Java pode usar o pool de threads padrão (`ForkJoinPool.commonPool()`). Mas, se você quiser que seus trabalhadores tenham características específicas – talvez todos precisem de um crachá com um nome especial (para facilitar o _debugging_) ou precisem ser threads "daemon" (para não impedirem o programa de fechar) – você precisa de uma `ThreadFactory` personalizada.

A **`ThreadFactory`** é uma interface simples que tem a responsabilidade de **criar threads sob demanda** para o `ExecutorService`. Ela garante que todas as threads criadas sigam o mesmo padrão que você definiu, permitindo que você personalize o comportamento e os atributos dessas threads, como o nome, a prioridade ou se elas são threads _daemon_.

### Exemplo com Código

Vamos simular o cálculo de preços em um serviço, utilizando um `CompletableFuture` para executar a tarefa de forma assíncrona e um `ThreadFactory` customizado para nomear as threads do pool.

```java
import java.util.concurrent.*;

public class CompletableFutureExample {

    // 1. Definição da ThreadFactory Customizada
    static class CustomThreadFactory implements ThreadFactory {
        private final String nomeBase;
        private int contador = 0;

        public CustomThreadFactory(String nomeBase) {
            this.nomeBase = nomeBase;
        }

        // 2. Método responsável por criar a nova Thread
        @Override
        public Thread newThread(Runnable r) {
            // Cria a Thread e atribui um nome customizado e um status daemon
            Thread t = new Thread(r, nomeBase + "-" + contador++);
            // Indica que esta thread não deve impedir o encerramento da JVM
            t.setDaemon(true);
            return t;
        }
    }

    // 3. Serviço que simula uma operação demorada (Callable/Supplier)
    public Double calcularPreco() {
        try {
            // Simula uma requisição de 2 segundos
            Thread.sleep(2000);
            // Retorna um valor simulado
            return 499.99;
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return 0.0;
        }
    }

    public static void main(String[] args) {
        CompletableFutureExample service = new CompletableFutureExample();

        // 4. Criação de um ExecutorService com a ThreadFactory customizada
        // Um FixedThreadPool de 4 threads
        ExecutorService executor = Executors.newFixedThreadPool(4, new CustomThreadFactory("COMPLETABLE_WORKER"));

        System.out.println("Thread Principal: " + Thread.currentThread().getName() + " - Iniciando busca de preço.");

        // 5. Submete a tarefa assíncrona ao ExecutorService customizado
        // supplyAsync é usado quando a tarefa retorna um valor
        CompletableFuture<Double> futuroPreco = CompletableFuture.supplyAsync(
            service::calcularPreco,
            executor
        );

        System.out.println("Thread Principal: Fazendo outras coisas enquanto o preço é buscado...");

        try {
            // 6. Bloqueia (espera) a Thread Principal até que o resultado esteja pronto.
            // join() é preferido pois lança unchecked exception (CompletionException)
            Double preco = futuroPreco.join();

            System.out.println("Thread Principal: Preço recebido: R$" + preco);

        } catch (CompletionException e) {
            // Tratamento de exceção em join()
            System.err.println("Ocorreu um erro na busca: " + e.getCause().getMessage());
        } finally {
            // 7. Desliga o Executor Service para liberar os recursos
            executor.shutdown();
        }
    }
}
```

**Explicação Linha por Linha:**

1. **`static class CustomThreadFactory implements ThreadFactory`**: Definimos nossa própria fábrica, que implementa a interface `ThreadFactory`.
2. **`public Thread newThread(Runnable r)`**: Este é o único método que a interface exige. É aqui que criamos a thread, passando o `Runnable` (`r`) para ela. Customizamos o nome (`nomeBase + "-" + contador++`) e definimos a thread como _daemon_.
3. **`public Double calcularPreco()`**: Este método simula uma operação demorada (como uma chamada de rede), introduzindo um `Thread.sleep(2000)` para simular a latência de 2 segundos.
4. **`ExecutorService executor = Executors.newFixedThreadPool(4, new CustomThreadFactory("COMPLETABLE_WORKER"));`**: Criamos um pool de threads fixo com 4 threads. O segundo argumento é nossa `CustomThreadFactory`, garantindo que todas as threads desse pool terão o nome que especificamos.
5. **`CompletableFuture<Double> futuroPreco = CompletableFuture.supplyAsync(service::calcularPreco, executor);`**: Submetemos a tarefa de `calcularPreco` para ser executada de forma assíncrona. O Java usará uma das threads do nosso `executor` customizado.
6. **`Double preco = futuroPreco.join();`**: A thread principal (Main) espera neste ponto até que a tarefa assíncrona termine e retorna o resultado.
7. **`executor.shutdown();`**: É crucial desligar o `ExecutorService` para que o programa principal termine e as threads _daemon_ sejam encerradas de forma graciosa.

### Aplicação no Mundo Real

A combinação de `CompletableFuture` e `ThreadFactory` é a base para sistemas de **alta performance e arquiteturas assíncronas** em Java:

1. **Orquestração de Microserviços e Agregação de Dados:**
    - **Problema:** Um usuário acessa a página de um produto em um e-commerce. Para montar a página, o sistema precisa do preço (API 1), do estoque (API 2) e das avaliações (API 3). Fazer essas chamadas sequencialmente levaria muito tempo.
    - **Solução com `CompletableFuture`:** O sistema lança os três pedidos de API em paralelo usando `supplyAsync` para cada um. O `CompletableFuture` permite que o sistema espere o resultado das três chamadas simultaneamente, reduzindo o tempo de resposta (latência) de forma drástica.
2. **Gerenciamento de Recursos em Servidores Web (Thread Pools):**
    - Sistemas de alta carga (como servidores Tomcat internos em Spring Boot) utilizam `ExecutorService` para gerenciar pools de threads.
    - **`ThreadFactory`** é essencial aqui:
        - **Nomeação:** Permite nomear as threads (ex: `http-request-worker-123`) para facilitar o monitoramento em logs e ferramentas de APM (Application Performance Monitoring).
        - **Isolamento de ClassLoader (Problemas de Inicialização):** Em ambientes complexos, como aplicações Spring Boot executando em contêineres, o `CompletableFuture` pode usar o `ForkJoinPool.commonPool()`. Em certas versões do Java (a partir do Java 9), esse pool pode ser inicializado com um `ClassLoader` incorreto (`PlatformClassLoader`), causando erros de `ClassNotFoundException` ao tentar acessar classes da aplicação (`AppClassLoader`). A solução robusta é criar um `ExecutorService` customizado com um `ThreadFactory` que explicitamente define o `ContextClassLoader` correto, resolvendo o problema de concorrência e inicialização.
3. **Processamento de Streams Paralelas Assíncronas:**
    - Para processar grandes coleções de forma paralela e assíncrona (como calcular o preço de 100 itens em uma lista), o `CompletableFuture` é combinado com a API Stream, mapeando cada elemento para uma operação assíncrona (`supplyAsync`).

Pense na `CompletableFuture` como um controle de tráfego aéreo para tarefas demoradas, onde você gerencia muitos voos simultaneamente sem que um bloqueie a pista para o próximo. O `ThreadFactory` garante que todos os aviões decolando estão devidamente registrados e configurados para a missão.

### Resumo Rápido

|Conceito|Resumo|
|:--|:--|
|**CompletableFuture**|Permite programação assíncrona e não-bloqueante em Java, retornando o resultado de uma computação futura. É ideal para executar tarefas em paralelo e melhorar a responsividade, utilizando pools de threads por padrão ou customizados.|
|**ThreadFactory**|Interface usada para criar e configurar threads de forma padronizada dentro de um `ExecutorService`, permitindo definir nomes, prioridades ou `ContextClassLoader` das threads.|
|**Uso em Concorrência**|Garante que as tarefas sejam executadas por _threads_ otimizadas e bem configuradas, essenciais para evitar gargalos e resolver problemas de compatibilidade/visibilidade de classes em arquiteturas complexas.|