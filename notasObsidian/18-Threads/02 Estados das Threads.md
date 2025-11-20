
---

## Explicação Feynman: Estados das Threads

Imagine que uma _thread_ é como um pequeno operário que a Máquina Virtual Java (JVM) contrata para executar uma lista específica de tarefas. Como um bom gerente, você precisa saber o que seu operário está fazendo a cada instante: Ele está esperando? Está trabalhando duro? Está de folga?

Em Java, o ciclo de vida de uma _thread_ é dividido em seis estados fundamentais, definidos pela enumeração `Thread.State`. Esses estados ditam como a _thread_ interage com o **Escalonador** (o "chefe" da JVM que decide quem usa a CPU).

Aqui estão os seis estados, do nascimento à morte:

1. **NEW (Nova):**
    
    - A _thread_ foi criada, mas ainda é apenas uma ideia, um objeto. Ela é como um novo funcionário que acabou de ser contratado, mas ainda não recebeu a ordem de começar a trabalhar (`start()`).
2. **RUNNABLE (Executável):**
    
    - Quando você chama o método `start()`, a _thread_ entra neste estado. _Runnable_ significa "pronta para executar". Ela está na fila para receber tempo de CPU. Pode ser que ela esteja **realmente rodando** naquele milissegundo, ou pode ser que ela esteja **esperando** o Escalonador dar uma fatia do processador. A JVM não tem um estado separado chamado _RUNNING_; _RUNNABLE_ engloba as duas situações.
3. **BLOCKED (Bloqueada):**
    
    - O operário quer entrar em uma sala trancada, um trecho de código crítico (`synchronized`). No entanto, outro operário já pegou a chave (o _lock_ ou a trava). A _thread_ _BLOCKED_ fica parada, aguardando que a outra _thread_ **solte a trava do objeto** para que ela possa pegá-la.
4. **WAITING (Esperando):**
    
    - Essa é uma parada voluntária e _indefinida_. A _thread_ parou e, crucialmente, **liberou a chave (`lock`)** do objeto que estava usando. Ela entra neste estado tipicamente ao chamar `Object.wait()` ou `Thread.join()` (sem timeout). Ela só voltará para _RUNNABLE_ se outra _thread_ a notificar usando `Object.notify()` ou `Object.notifyAll()`, ou se a _thread_ que ela estava esperando (`join`) terminar.
5. **TIMED_WAITING (Esperando com Tempo):**
    
    - É como a _WAITING_, mas com um despertador. A _thread_ parou por um período específico. Isso ocorre com métodos como `Thread.sleep(long millis)` ou `Thread.join(long millis)` (com tempo limite). Quando o tempo acaba, ela automaticamente retorna ao estado _RUNNABLE_. O `sleep()` é uma das poucas coisas que o Java _garante_ que acontecerá no mundo das _threads_.
6. **TERMINATED (Terminada):**
    
    - A _thread_ completou sua execução do método `run()` e morreu. O objeto _thread_ ainda existe na memória, mas não está mais ativo.

> **Analogia Feynman:** Pense nos estados como se fossem as fases de uma viagem espacial.
> 
> - **NEW:** O foguete está montado, mas ainda no chão.
> - **RUNNABLE:** O lançamento foi autorizado, ele está na rampa ou voando ativamente.
> - **BLOCKED:** Ele precisa reabastecer em uma estação espacial que está ocupada por outra nave (esperando a trava).
> - **WAITING/TIMED_WAITING:** Ele para para esperar um sinal da Terra (WAITING) ou dorme por 24 horas (TIMED_WAITING).
> - **TERMINATED:** Ele pousou em Marte e desligou os motores.

## Exemplo com código:

O código Java a seguir demonstra a transição entre os estados NEW, RUNNABLE, TIMED_WAITING e TERMINATED usando `Thread.sleep()` e verificando o estado com `t.getState()`:

```java
import java.lang.Thread.State;

public class ThreadStatesDemo {

    public static void main(String[] args) throws InterruptedException {
        // Criamos uma Thread usando uma Lambda (forma concisa de Runnable)
        Thread t1 = new Thread(() -> {
            try {
                // Dentro do run(), a thread já está em RUNNABLE
                System.out.println(Thread.currentThread().getName() + " começou.");

                // 3. Thread chama sleep(), entrando em TIMED_WAITING
                Thread.sleep(500);

            } catch (InterruptedException e) {
                // Tratamento de InterruptedException, necessário para sleep() e join()
                Thread.currentThread().interrupt();
            }
            System.out.println(Thread.currentThread().getName() + " finalizou a execução.");
        }, "Feynman-Operario");

        // 1. Estado NEW
        System.out.println("Estado 1 (Pré-start): " + t1.getState()); // Saída: NEW

        t1.start(); // Dá o start: Thread move para RUNNABLE

        // 2. Espera um pouco para garantir que t1 esteja executando (RUNNABLE ou TIMED_WAITING)
        Thread.sleep(100);

        // 3. Estado TIMED_WAITING (devido ao sleep(500) dentro da thread)
        // O método sleep(millis) coloca a thread atual em estado de sono pelo tempo especificado.
        System.out.println("Estado 2 (Durante o sleep): " + t1.getState()); // Saída: TIMED_WAITING

        // 4. Usando join() para esperar que t1 termine. O main() entra em WAITING/TIMED_WAITING.
        t1.join();

        // 5. Estado TERMINATED (após o join() retornar, significa que t1 morreu)
        System.out.println("Estado 3 (Pós-execução): " + t1.getState()); // Saída: TERMINATED
    }
}
```

**Explicação Linha por Linha:**

|Código|Explicação do Estado|
|:--|:--|
|`Thread t1 = new Thread(...)`|A _thread_ é criada. Seu estado é **NEW**.|
|`System.out.println("Estado 1...")`|Confirma o estado **NEW**.|
|`t1.start();`|A _thread_ é iniciada, entrando em **RUNNABLE** (pronta para ser agendada pelo Escalonador).|
|`Thread.sleep(500);` (dentro de t1)|A _thread_ `t1` se coloca em **TIMED_WAITING** por 500 milissegundos. O `sleep` é um método estático que faz a _thread_ que o chamou dormir.|
|`System.out.println("Estado 2...")`|A _thread_ principal verifica e encontra **TIMED_WAITING**.|
|`t1.join();`|A _thread_ principal (`main`) para e espera indefinidamente que `t1` termine, entrando em **WAITING** (ou `TIMED_WAITING` se tivesse um timeout).|
|`System.out.println("Estado 3...")`|Após `t1` terminar, seu estado final é **TERMINATED**.|

## Aplicação no Mundo Real:

O entendimento dos estados é crucial para desenvolvedores Java, especialmente em sistemas concorrentes, pois permite diagnosticar e prevenir problemas.

1. **Sincronismo e Exclusão Mútua (Estados BLOCKED):**
    
    - **Cenário:** Sistemas de transação financeira ou estoque (`Account` class). Múltiplas _threads_ (usuários diferentes) tentam sacar dinheiro da mesma conta simultaneamente.
    - **Resolução:** Usar `synchronized` (em métodos ou blocos) garante a exclusão mútua. Se duas _threads_ (T1 e T2) tentarem sacar, uma delas (T1) pega o _lock_ e T2 entra no estado **BLOCKED**, aguardando a T1 liberar o recurso. Isso evita a _race condition_ (condição de corrida) e garante a integridade do saldo.
2. **Comunicação entre Threads (Estados WAITING/NOTIFY):**
    
    - **Cenário:** O padrão Produtor/Consumidor. Imagine um sistema de processamento de pedidos em um e-commerce.
        - O **Produtor** (Thread A) coloca pedidos em uma fila.
        - O **Consumidor** (Thread B) processa os pedidos.
    - **Resolução:** Se a fila de pedidos estiver vazia, o Consumidor (Thread B) não tem o que fazer. Em vez de ficar em um _loop_ gastando CPU, ele chama `Object.wait()` e entra em estado **WAITING**, liberando a chave. Quando o Produtor (Thread A) adiciona um novo pedido, ele chama `Object.notifyAll()` para acordar o Consumidor.
3. **Fluxos de Trabalho Dependentes (Estado JOIN/WAITING):**
    
    - **Cenário:** Geração de relatórios complexos. Uma _thread_ principal (T-Main) é responsável por compilar o relatório final, mas primeiro precisa que três sub-threads (T1, T2, T3) calculem as seções financeiras, de vendas e de estoque.
    - **Resolução:** A T-Main chama `T1.join()`, `T2.join()` e `T3.join()`. Isso força a T-Main a entrar no estado **WAITING** e só prosseguir (compilar o relatório) após as sub-threads completarem 100% do seu trabalho.
4. **Otimização de Recursos (Estado TIMED_WAITING/SLEEP):**
    
    - **Cenário:** Um subsistema de monitoramento precisa verificar o status de um hardware externo a cada 30 segundos.
    - **Resolução:** A _thread_ de monitoramento, após cada verificação, chama `Thread.sleep(30000)`. Isso a move para **TIMED_WAITING**, liberando o processador para outras tarefas durante o _delay_. Caso contrário, ela ficaria em um _busy loop_ (laço infinito) consumindo recursos desnecessariamente.

## Resumo Rápido:

As _threads_ Java transitam por 6 estados oficiais. **NEW** é criada; **RUNNABLE** está pronta ou executando. **BLOCKED** espera por uma trava (`synchronized`). **WAITING** e **TIMED_WAITING** (com `sleep` ou `wait`) esperam por um evento ou tempo. **TERMINATED** significa que o trabalho foi concluído.