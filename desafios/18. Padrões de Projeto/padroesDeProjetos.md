
---

### **Desafio 1 – Singleton**

**Problema:**

* Crie uma classe `Configuracao` que **garanta apenas uma instância** durante a execução do programa.
* Crie um método `getInstancia()` para obter a instância única.
* Teste criando duas variáveis e verifique se apontam para o mesmo objeto.

**Pergunta lógica:** Como garantir que uma classe tenha **uma única instância** globalmente acessível?

---

### **Desafio 2 – Factory Method**

**Problema:**

* Crie uma interface `Produto` com método `exibir()`.
* Crie duas classes `ProdutoA` e `ProdutoB` que implementam `Produto`.
* Crie uma classe `FabricaProduto` com um método `criarProduto(String tipo)` que retorna o produto correto.
* Teste criando `ProdutoA` e `ProdutoB` via fábrica e chamando `exibir()`.

**Pergunta lógica:** Como usar o Factory Method para **delegar a criação de objetos** e desacoplar código?

---

### **Desafio 3 – Observer**

**Problema:**

* Crie uma interface `Observer` com método `atualizar(String mensagem)`.
* Crie uma classe `Sujeito` que mantém uma lista de observers e um método `notificarObservers()`.
* Crie pelo menos dois observers que imprimem a mensagem recebida.
* Teste adicionando observers e enviando notificações.

**Pergunta lógica:** Como o padrão Observer permite que objetos sejam **notificados automaticamente de mudanças** em outro objeto sem acoplamento forte?

---


