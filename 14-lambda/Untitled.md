Excelente pergunta 👏

💡 As lambdas mais usadas no Java (na prática do dia a dia)

Aqui vão as quatro mais usadas, com o tipo de interface funcional que representam:


---

1. Consumer<T>

> 🔹 Usada quando você consome um valor, mas não retorna nada.



Exemplo:

List<String> nomes = List.of("Ana", "Bruno", "Carlos");

nomes.forEach(nome -> System.out.println(nome));

➡️ Aqui, nome -> System.out.println(nome) é uma lambda do tipo Consumer.
Ela recebe um parâmetro e executa uma ação (imprimir), sem retorno.


---

2. Predicate<T>

> 🔹 Usada quando você testa algo e retorna um booleano (true ou false).



Exemplo:

List<Integer> numeros = List.of(1, 2, 3, 4, 5);

numeros.stream()
       .filter(n -> n % 2 == 0)
       .forEach(System.out::println);

➡️ Aqui, n -> n % 2 == 0 é uma lambda do tipo Predicate.
Ela filtra apenas os números pares.


---

3. Function<T, R>

> 🔹 Usada quando você transforma um valor em outro (retorna algo diferente do que recebe).



Exemplo:

List<String> nomes = List.of("ana", "bruno", "carlos");

List<String> nomesMaiusculos = nomes.stream()
    .map(nome -> nome.toUpperCase())
    .toList();

➡️ Aqui, nome -> nome.toUpperCase() é uma lambda do tipo Function.
Ela transforma uma String em outra (String → String).


---

4. Supplier<T>

> 🔹 Usada quando você fornece um valor, sem receber nada.



Exemplo:

Supplier<Double> gerarAleatorio = () -> Math.random();

System.out.println(gerarAleatorio.get());

➡️ Aqui, () -> Math.random() é uma lambda do tipo Supplier, pois não recebe parâmetro, mas retorna um valor.


---

🏆 Conclusão — A mais usada de todas

No dia a dia de desenvolvimento Java moderno (com Streams), a lambda mais usada é a do tipo Predicate, especialmente com .filter():

.filter(x -> x > 10)

Mas logo atrás vêm as de Consumer (para .forEach()), e Function (para .map()).


---

Se quiser, posso te mostrar um mini resumo visual (como uma tabela) mostrando cada tipo de lambda, assinatura e exemplo real — quer que eu monte?

