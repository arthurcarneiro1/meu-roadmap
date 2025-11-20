
---

### Explicação Feynman: Lock e ReentrantLock

Imagine que você tem uma receita de bolo (o seu **código**) e um pote de farinha (o seu **recurso compartilhado**). Se você tiver várias pessoas (as **threads**) tentando pegar farinha ao mesmo tempo, o resultado é o caos: a farinha espalha, a contagem de quanto sobrou fica errada, e o bolo desanda (isso é a **condição de corrida**, ou _race condition_).

Para evitar essa bagunça, inventamos o **Lock** (cadeado).

**Lock Implícito (`synchronized`)**

Em Java, a maneira mais simples de proteger um pedaço de código é usando a palavra-chave `synchronized`. Pense nisso como um cadeado automático.

Quando uma _Thread_ entra em um bloco `synchronized`, ela pega a **chave** (o _lock_) do objeto. Nenhuma outra _Thread_ pode entrar lá até que a primeira saia e **libere a chave**. O Java (a JVM, para ser técnico) gerencia essa chave por você. Isso é simples e eficiente.

**O Problema do Lock Implícito**

O cadeado automático (`synchronized`) é ótimo, mas te dá pouco controle. Se uma _Thread_ está esperando a chave, ela simplesmente espera, e você não tem como dizer: "Olha, se você não conseguir a chave em 5 segundos, desista e vá fazer outra coisa".

**ReentrantLock (O Lock Expresso e Cheio de Recursos)**

É aí que entra o `ReentrantLock`. Ele faz a mesma coisa que o `synchronized` — garante que apenas uma _Thread_ acesse a seção crítica (exclusão mútua). Mas, em vez de ser automático, ele é **manual**.

1. **Reentrant (Reentrante):** O nome "reentrante" significa que se uma _Thread_ já tem a chave, ela pode pedi-la novamente (reentrar) sem ficar bloqueada esperando por si mesma. É como um passe de reentrada no cinema.
2. **Manual:** Você precisa chamar o método `lock()` para pegar a chave e, crucialmente, você **DEVE** chamar `unlock()` para liberá-la, idealmente dentro de um bloco `finally` para garantir que ela seja liberada, mesmo que ocorra um erro (_Exception_).
3. **Vantagens:** Por ser manual, ele oferece recursos extras, como a capacidade de **tentar obter o lock** com um tempo limite (`tryLock(timeout)`) ou de **interromper uma thread** que está esperando pelo lock (`lockInterruptibly`).

> O `ReentrantLock` é o seu kit de ferramentas suíço de sincronização, dando controle total, enquanto o `synchronized` é o cadeado padrão que funciona na maioria dos casos.

---

### Exemplo com Código (Java)

Vamos criar um contador simples onde várias _threads_ tentam incrementar um valor, usando o `ReentrantLock` para garantir que a contagem seja sempre correta, evitando a _race condition_.

```
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

// 1. Classe para gerenciar o recurso compartilhado (o contador)
class ContadorComLock {
    private int contador = 0;
    // 2. Definimos o nosso Lock
    private final Lock lock = new ReentrantLock();

    public void incrementar() {
        // 3. Adquire o lock (trava o acesso)
        lock.lock();

        try {
            // 4. Seção crítica: apenas uma thread por vez pode executar este bloco.
            contador++;
            System.out.println(Thread.currentThread().getName() + " incrementou para: " + contador);
        } finally {
            // 5. Libera o lock. ESSENCIAL, deve estar no finally!
            lock.unlock();
        }
    }

    public int getContador() {
        return contador;
    }
}

public class ReentrantLockTest {
    public static void main(String[] args) throws InterruptedException {
        // 6. Recurso compartilhado: Apenas uma instância do contador.
        ContadorComLock recursoCompartilhado = new ContadorComLock();

        // 7. Criação das Threads (tarefas que executam a função incrementar 5 vezes)
        Runnable tarefa = () -> {
            for (int i = 0; i < 5; i++) {
                recursoCompartilhado.incrementar();
            }
        };

        Thread t1 = new Thread(tarefa, "Thread-A");
        Thread t2 = new Thread(tarefa, "Thread-B");

        // 8. Inicia a execução concorrente
        t1.start();
        t2.start();

        // 9. Espera as threads terminarem para garantir o resultado final (usando join)
        // O método join() faz a thread principal (main) esperar até que as threads t1 e t2 terminem.
        t1.join();
        t2.join();

        // 10. O resultado final é garantido (10, pois 2 threads incrementaram 5 vezes cada)
        System.out.println("Resultado final garantido: " + recursoCompartilhado.getContador());
    }
}
```

**Explicação Linha por Linha Chave:**

- `private final Lock lock = new ReentrantLock();`: Criamos uma instância do `ReentrantLock`.
- `lock.lock();`: A _Thread_ chama este método para tentar adquirir o lock. Se outra _Thread_ já o tiver, ela fica esperando (no estado `BLOCKED` ou `WAITING`) até que seja liberado.
- `try { ... } finally { ... }`: A estrutura é obrigatória. Se o código dentro do `try` lançar uma exceção, a _Thread_ ainda assim precisa liberar o lock.
- `lock.unlock();`: Libera o lock. Se você esquecer essa linha, nenhuma outra _Thread_ conseguirá o lock, resultando em um **lock permanente** (_livelock_ ou _deadlock_ dependendo da situação).
- `t1.join();`: Garante que a thread principal espere a execução de `t1` terminar.

---

### Aplicação no Mundo Real

O `ReentrantLock` e outros mecanismos de _concurrency_ do pacote `java.util.concurrent.locks` são essenciais em sistemas de alto desempenho e alta concorrência.

1. **Sistemas de Estoque e Comércio Eletrônico:** Imagine um sistema de vendas online. Várias pessoas (`Threads`) tentam comprar o último item em estoque (`Recurso Compartilhado`).
    - O `Lock` é usado para garantir a **atomicidade** do saque/compra. A lógica de "verificar se há saldo/estoque E só então realizar a transação" deve ser executada de forma atômica (sem interrupção). Se a _Thread_ A verifica que há 1 item, mas é interrompida antes de sacar, e a _Thread_ B verifica que há 1 item, ambas tentarão sacar, resultando em estoque negativo (inconsistência). O `ReentrantLock` impede que isso ocorra, forçando a exclusão mútua.
2. **Prevenção de Deadlock em Aplicações Financeiras:** Deadlock é quando duas _Threads_ ficam presas em uma dependência circular, esperando eternamente uma pela outra.
    - A classe `ReentrantLock` é útil por causa do método `tryLock(long timeout, TimeUnit unit)`. Em cenários complexos (como transferência de fundos entre contas, onde você precisa travar duas contas), se uma _Thread_ não consegue obter o segundo lock em um tempo razoável, ela pode liberar o primeiro lock e tentar novamente, evitando o **deadlock**. O `synchronized` não oferece essa capacidade de "tentar e desistir".
3. **Gerenciamento de Cache e Pool de Conexões:** Em _backends_ Java, pools de conexões ou caches de dados são recursos limitados.
    - O `ReentrantLock` permite implementar políticas de **fairness** (justiça), onde as _Threads_ que esperaram por mais tempo têm prioridade para adquirir o lock. Isso ajuda a evitar que uma _Thread_ seja "esquecida" (_starvation_), embora o modo _fair_ possa reduzir a performance geral.

---

### Resumo Rápido

**Lock e ReentrantLock** garantem que apenas uma _Thread_ por vez acesse recursos compartilhados (Exclusão Mútua). O `synchronized` é o Lock implícito (automático) do Java, fácil de usar. O **ReentrantLock** é o Lock explícito do pacote de Concorrência, que oferece **mais controle** (como espera interruptível ou com tempo limite), mas exige que o desenvolvedor **libere o lock manualmente** no `finally`.