

---

### Explicação Feynman: Concorrência - Conditions

Imagine que você está em uma fábrica, e essa fábrica tem uma **Sala Crítica** onde apenas um trabalhador pode entrar por vez. Para garantir isso, você usa uma **chave especial**, que em Java chamamos de `Lock` (como o `ReentrantLock`).

Quando uma thread (o trabalhador) precisa acessar essa sala, ela pega a chave (chama `lock()`).

Mas e se essa thread entra na Sala Crítica e descobre que não pode continuar o trabalho porque está faltando material? Em vez de ficar parada, segurando a chave e impedindo que todos os outros entrem (o que seria ociosidade de CPU, um desperdício), ela precisa de um lugar para esperar.

No modelo de sincronização mais antigo (`synchronized`), a thread usaria o método `wait()` diretamente no objeto, liberando a chave automaticamente.

No entanto, quando você usa um **Lock mais sofisticado** (como o `ReentrantLock`), que te dá mais controle sobre a chave (por exemplo, você pode tentar pegá-la por um certo tempo, usando `tryLock(timeout, unit)`), precisamos de um **mecanismo de espera separado**. É aí que entra a `Condition`.

Uma **Condition** é, essencialmente, a **Sala de Espera** vinculada àquela chave específica (`Lock`).

1. **A Espera (`await()`):** Se a thread entra na Sala Crítica (segurando o `Lock`) e percebe que falta material, ela chama o método `await()` na `Condition`. Ao fazer isso, ela automaticamente **libera a chave** do `Lock` e vai para a Sala de Espera, ficando em um estado de bloqueio (`WAITING` ou `TIMED_WAITING`).
2. **A Notificação (`signal()` ou `signalAll()`):** Outra thread, que talvez seja responsável por trazer o material, quando termina o seu trabalho, notifica a Sala de Espera chamando `signal()` (acorda uma thread) ou `signalAll()` (acorda todas as threads esperando).
3. **O Retorno:** A thread acordada, que estava na Sala de Espera, volta a disputar a chave do `Lock` e, ao obtê-la novamente, **continua exatamente de onde parou**.

Você obtém uma `Condition` usando o método `newCondition()` do seu `ReentrantLock`. Essa é a principal diferença de design: em vez de usar os métodos `wait`, `notify` e `notifyAll` de todo objeto em Java, você usa os métodos `await`, `signal` e `signalAll` específicos de uma instância de `Condition` criada pelo seu `Lock`.

**Em resumo, a `Condition` permite que threads suspendam sua execução de forma eficiente, liberando o lock crucial para que outras threads progridam, e que sejam acordadas apenas quando uma condição que elas esperam for atendida.**

---

### Exemplo com código:

Vamos adaptar o exemplo de sincronização de lista (Produtor/Consumidor) que envolve espera para usar `ReentrantLock` e `Condition`.

Neste exemplo, temos uma classe `FilaCompartilhada` que usa um `ReentrantLock` para proteger o acesso e uma `Condition` para coordenar a espera. O Consumidor espera (`await`) se a fila estiver vazia, e o Produtor notifica (`signalAll`) quando adiciona um item.

```
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class FilaCompartilhada {

    // 1. O Lock explícito (a "chave especial")
    private final Lock lock = new ReentrantLock();

    // 2. A Condition para esperar quando a fila está vazia (a "Sala de Espera")
    private final Condition naoVazia = lock.newCondition(); //

    private final Queue<String> fila = new LinkedList<>();

    // Método que o Produtor chama para adicionar itens
    public void produzir(String item) throws InterruptedException {
        // Tenta pegar a chave do Lock
        lock.lock(); //
        try {
            fila.add(item);
            System.out.println(Thread.currentThread().getName() + " produziu: " + item + ". Tamanho: " + fila.size());

            // Notifica as threads que estão esperando na 'Condition' que algo mudou
            naoVazia.signalAll(); // (equivalente ao notifyAll())

        } finally {
            // Garante que a chave seja sempre liberada, mesmo em caso de exceção
            lock.unlock(); //
        }
    }

    // Método que o Consumidor chama para consumir itens
    public String consumir() throws InterruptedException {
        // Tenta pegar a chave do Lock
        lock.lock();
        try {
            // Se a fila estiver vazia, a thread deve esperar
            while (fila.isEmpty()) {
                System.out.println(Thread.currentThread().getName() + " sem itens, esperando...");

                // A thread libera o 'lock' e entra no estado de espera na 'Condition'
                naoVazia.await(); // (equivalente ao wait())
            }

            String item = fila.poll();
            System.out.println(Thread.currentThread().getName() + " consumiu: " + item + ". Tamanho: " + fila.size());
            return item;

        } finally {
            // Garante a liberação da chave
            lock.unlock();
        }
    }
}

public class ConditionTest {
    public static void main(String[] args) {
        FilaCompartilhada fila = new FilaCompartilhada();

        // 1. Thread Consumidora
        Thread consumidor = new Thread(() -> {
            try {
                // A thread tentará consumir imediatamente, mas a fila estará vazia, então ela chamará await()
                fila.consumir();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Consumidor-1");

        // 2. Thread Produtora (adiciona após um pequeno atraso)
        Thread produtor = new Thread(() -> {
            try {
                // Dorme um pouco para garantir que o consumidor comece a esperar primeiro
                Thread.sleep(100);
                fila.produzir("Mensagem-1");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }, "Produtor-A");

        consumidor.start();
        produtor.start();
    }
}
```

#### Explicação Linha por Linha:

|Código|Explicação (FilaCompartilhada)|Fonte(s)|
|:--|:--|:--|
|`private final Lock lock = new ReentrantLock();`|Declaramos um `ReentrantLock`. Em concorrência Java, este é o objeto que garante a exclusão mútua, substituindo o `synchronized`.||
|`private final Condition naoVazia = lock.newCondition();`|Criamos uma instância de `Condition` através do nosso `Lock`. É esta instância que vamos usar para dizer às threads para esperarem ou acordarem.||
|`lock.lock();`|A thread adquire o lock. É obrigatório ter o lock antes de usar `Condition.await()` ou `Condition.signal()`.||
|`naoVazia.signalAll();`|O Produtor, após adicionar um item, notifica todas as threads que estão presas na condição `naoVazia` para que acordem e tentem pegar o lock novamente. É o equivalente ao `notifyAll()`.||
|`lock.unlock();`|A thread libera o lock. Isto deve ser feito sempre em um bloco `finally` para garantir que o lock seja liberado, independentemente de exceções.||
|`while (fila.isEmpty()) { ... }`|O Consumidor verifica a condição. É fundamental usar um `while` (em vez de um `if`) para verificar novamente a condição após acordar, pois as threads podem ser acordadas "espontaneamente" (spurious wakeups) ou por um `signal` não relacionado, e precisam reconfirmar se o recurso está disponível.||
|`naoVazia.await();`|A thread libera o lock e entra em modo de espera indefinidamente (WAITING), até ser notificada por `signal` ou `signalAll()`.||

---

### Aplicação no mundo real

No mundo real, as **Conditions** e o `ReentrantLock` são usados em cenários onde a sincronização nativa (`synchronized`/`wait`/`notify`) não oferece controle suficiente, ou onde é necessário um gerenciamento mais complexo da coordenação.

1. **Algoritmos de Produtor/Consumidor em Filas de Mensagens:** Este é o exemplo clássico. Em sistemas de processamento de dados (como pipelines de logs ou processamento de pedidos), você tem threads Produtoras (que colocam tarefas na fila) e threads Consumidoras (que executam as tarefas).
    
    - **Problema Resolvido:** Se um Consumidor encontra a fila vazia, ele não deve ficar em um "loop ocupado" (`busy-waiting`), gastando CPU. Usando `Condition.await()`, ele entra em estado de espera, liberando o lock e o processador. Quando o Produtor adiciona uma nova tarefa, ele usa `signal()` ou `signalAll()` para acordar eficientemente os Consumidores.
2. **Pools de Conexão ou Recursos Limitados:** Imagine um pool de conexões de banco de dados. Quando todas as conexões estão em uso e uma nova thread tenta obter uma, essa thread deve esperar.
    
    - **Uso da Condition:** A thread que precisa de uma conexão chama `await()` se o pool estiver vazio. Quando uma thread **devolve** uma conexão (liberando o recurso), ela chama `signalAll()` na condição de "Conexão Disponível", notificando as threads que estão esperando para usá-la.
3. **Controle de Múltiplas Condições de Espera (Vantagem do `ReentrantLock`):** O `ReentrantLock` permite criar **várias instâncias de `Condition`**.
    
    - **Exemplo:** Se você tem uma fila com limite máximo e mínimo. Uma `Condition` pode ser para threads Consumidoras esperando por itens (fila vazia). Outra `Condition` pode ser para threads Produtoras esperando espaço na fila (fila cheia). Se você usar `Object.wait()`, todas as threads esperam no mesmo monitor. Com _Conditions_, você pode notificar seletivamente apenas os Consumidores (quando um item é adicionado) ou apenas os Produtores (quando um item é removido), otimizando a concorrência e o desempenho.

A utilização de `Lock` e `Condition` torna o código um pouco mais "nervoso" devido à necessidade de gerenciar `try-finally` para garantir que o `unlock()` seja chamado, mas oferece a **precisão e o controle** necessários para desenvolver bibliotecas e sistemas concorrentes de alto desempenho em Java.

---

### Resumo rápido

|Conceito|Descrição|
|:--|:--|
|**Conditions**|Mecanismos de coordenação (`await`, `signal`) usados com Locks explícitos (e.g., `ReentrantLock`).|
|**Função**|Permitem que threads suspendam a execução, **liberem o Lock** (para evitar Deadlock e ocupação) e sejam notificadas quando a condição mudar.|
|**Uso Comum**|Essencial em padrões Produtor/Consumidor ou para gerenciar recursos, garantindo espera eficiente.|