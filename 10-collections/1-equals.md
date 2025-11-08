Explicação Feynman

Imagine que você tem duas canetas iguais na aparência. Pergunta: “elas são a mesma caneta?”
Em Java há duas formas de responder isso: == e equals(). == pergunta se é o mesmo objeto na memória (mesmo endereço). equals() pergunta se os objetos são “equivalentes” segundo uma definição de igualdade que faz sentido para o tipo (por exemplo: mesma cor, mesmo modelo, mesmo número de série).

Por padrão, a implementação de equals() em Object faz exatamente o que == faz (compara referências). Mas a maioria das classes de domínio (Pessoa, Pedido, Produto) precisa de uma definição de igualdade lógica — então você precisa sobrescrever equals().
Importante: sempre que sobrescrever equals() você deve sobrescrever hashCode() para manter o contrato entre eles — coleções baseadas em hash (ex: HashMap, HashSet) usam hashCode() para organizar e localizar objetos; se equals() diz que dois objetos são iguais, o hashCode() deles também precisa ser igual, senão você terá bugs difíceis de achar.

Regras (contrato) de equals() — seja o “juiz” justo:

Reflexivo: x.equals(x) deve ser true.

Simétrico: se x.equals(y) então y.equals(x).

Transitivo: se x.equals(y) e y.equals(z) então x.equals(z).

Consistente: chamadas repetidas retornam o mesmo resultado enquanto os objetos não mudarem.

x.equals(null) deve ser false.


Use Objects.equals(a, b) quando quiser comparar podendo aceitar null sem NPE.


---

Exemplo com código (Java) — completo e comentado linha a linha
```java
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class EqualsDemo {

    // Classe de domínio: Person
    static class Person {
        private final String cpf; // identificador único (imagine um CPF)
        private final String name;

        public Person(String cpf, String name) {
            this.cpf = cpf;
            this.name = name;
        }

        // Sobrescrita de equals: define igualdade lógica por CPF
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;               // mesma referência → true rápido
            if (o == null) return false;              // comparar com null → false
            if (getClass() != o.getClass()) return false; // garante simetria com classes
            Person other = (Person) o;                // cast seguro
            return Objects.equals(this.cpf, other.cpf); // compara CPF (tratando nulls)
        }

        // Sobrescrita de hashCode compatível com equals
        @Override
        public int hashCode() {
            return Objects.hash(cpf);
        }

        @Override
        public String toString() {
            return "Person{" + name + ", cpf=" + cpf + "}";
        }
    }

    public static void main(String[] args) {
        Person a = new Person("12345678900", "Alice");  // pessoa A
        Person b = new Person("12345678900", "Alicia"); // pessoa B, mesmo CPF, nome diferente

        // == compara referência (não o que queremos aqui)
        System.out.println("a == b ? " + (a == b)); // false (objetos distintos em memória)

        // equals usa nossa lógica (CPF) — deve ser true
        System.out.println("a.equals(b) ? " + a.equals(b)); // true

        // Teste com HashSet: somente um elemento deve ser mantido se equals/hashCode corretos
        Set<Person> set = new HashSet<>();
        set.add(a);
        set.add(b);
        System.out.println("HashSet size: " + set.size()); // 1 se hashCode/equals corretos
        System.out.println("HashSet contents: " + set);
    }
}
```
Linha a linha (resumo do que fizemos):

Definimos Person com cpf e name. Usamos cpf como identidade lógica.

equals(Object o):

this == o: resposta rápida quando é o mesmo objeto.

o == null: protege contra null.

getClass() != o.getClass(): evita problemas com herança (garante simetria).

convertemos e comparamos cpf com Objects.equals(...) para evitar NPE.


hashCode() retorna Objects.hash(cpf) para manter consistência com equals.

No main mostramos diferença entre == e equals, e porque é importante para HashSet.


Se omitíssemos hashCode() (ou retornássemos algo inconsistente), set.size() poderia ser 2 mesmo que a.equals(b) seja true — isso quebra a semântica de conjunto e causa bugs.


---

Aplicação no mundo real (onde e por que você usa isso como desenvolvedor Java)

1. Coleções e caches: em sistemas que usam HashMap, HashSet ou ConcurrentHashMap (cache de sessões, índices em memória, deduplicação de logs), objetos precisam ter equals e hashCode corretos para serem encontrados e removidos corretamente. Exemplo: verificação se um usuário já está cadastrado em uma lista rápida por CPF/email.


2. ORM / Persistência: bibliotecas como Hibernate usam equals() (às vezes id da entidade) para comparar entidades em contexto de persistência; uma implementação ruim pode provocar comportamento inesperado ao unir/detach/merge entidades.


3. Testes e asserts: ao comparar resultados esperados vs atual em testes unitários/integrados, equals() bem definido torna as asserções confiáveis.


4. Mensageria e sistemas distribuídos: ao comparar eventos ou chaves de mensagem (IDs), equals() determina se duas mensagens referem-se ao mesmo recurso.


5. APIs públicas / bibliotecas: se você cria uma classe usada por outros (SDK, DTOs), definir equals/hashCode previsíveis facilita o uso correto pelos consumidores.



Exemplos concretos de problemas resolvidos:

Evitar duplicidade de clientes em um HashSet ao importar cadastros massivos (empresa de telecom, ERP).

Garantir que uma busca em cache por chave composta (ex: <accountId, date>) encontre a entrada correta.

Comparar DTOs em testes de integração de API (por exemplo, comparar resposta JSON convertida em objetos).



---

Termos técnicos — explicados curto e direto

==: compara referências (mesmo objeto).

equals(): método para comparar igualdade lógica; pode ser sobrescrito.

hashCode(): número inteiro usado por estruturas de hash; precisa ser compatível com equals().

Objects.equals(a,b): compara segurando null sem lançar NPE.

getClass() vs instanceof: getClass() garante igualdade de classe exata (evita violações do contrato simétrico ao lidar com herança); instanceof permite subclasses equivalentes (pode causar problemas de simetria).



---

Resumo rápido (até 3 linhas)

equals() define igualdade lógica enquanto == compara referência. Sempre que sobrescrever equals() também sobrescreva hashCode() para manter o contrato e evitar bugs em coleções baseadas em hash. Use campos imutáveis (ou cuidadosamente tratados) como base para equals().


---

Se quiser, eu posso:

gerar um exemplo que mostra o erro quando hashCode() não é sobrescrito;

mostrar como gerar equals/hashCode com IDE (ex: IntelliJ) ou usar record (Java 14+) para obter equals/hashCode automáticos. Quer que eu siga com algum desses?


