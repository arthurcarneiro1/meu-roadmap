
introdutório do maratona Java
ele lista oque veremos aqui no curso

# Aula 01

aulas que explicam conceitos
sem lista de exercícios
aprenda a se virar como programador


# Aula 02

como Java funciona 

## Fluxo de execução em Java

- **Código Fonte (.java)**
  - Arquivo que nós escrevemos → ex: `App.java`
  - Contém a lógica da aplicação

    ↓ (compilado com `javac`)

- **Bytecode (.class)**
  - Gerado pelo compilador `javac` (presente no **JDK**)
  - Transforma `.java` em `.class`
  - O `.java` é legível para humanos  
  - O `.class` é bytecode legível pela máquina

    ↓ (interpretado pela JVM)

- **Execução (JVM)**
  - A **Java Virtual Machine (JVM)** interpreta o bytecode
  - Responsável por rodar sua aplicação nos sistemas operacionais:
    - Windows
    - Linux
    - MacOS
  - Torna o Java **portável** → *"Write Once, Run Anywhere"*

# Aula 03
## Linguagens de Alto Nível e Baixo Nível  

- **Linguagens de baixo nível**: são as linguagens mais próximas da máquina, como **C** e **C++**.  
  - Por estarem mais próximas do hardware, costumam ser mais extensas, complexas e exigem maior atenção aos detalhes da arquitetura do computador.  

- **Linguagens de alto nível**: são as linguagens mais próximas da linguagem humana, ou seja, mais fáceis de entender e de programar.  
  - Exemplos: **Java, Python, JavaScript**.  

---

## Suporte a Longo Prazo no Java  

- O **Java** possui versões chamadas **LTS (Long-Term Support)**.  
- Essas versões recebem **suporte da Oracle por vários anos**, sendo as mais utilizadas pelas empresas.  
- Já as versões regulares do Java recebem suporte por apenas **6 meses**.  
- Por isso, no mercado, é comum que as empresas adotem versões **LTS** para garantir **estabilidade e segurança** a longo prazo.  


# Aula 04
 ele apenas configou o java adicionando o path do java
# Aula 05
### Execução manual com `javac` e `java`

1. Criou o arquivo no **Bloco de Notas** com o método `main`.
    
2. Salvou o arquivo com a extensão **`.java`**.
    
3. Usou o comando `javac` (compilador do **JDK**) para compilar o código e gerar o arquivo **`.class`**.
    
4. Executou o programa com o comando `java`, tudo isso diretamente pelo **CMD**.
    

📌 **Exemplo:**

`javac MeuPrograma.java   # compila o código java MeuPrograma         # executa o programa`
# Aula 6
 instalaçao do IDE intelij a interface grafica que usamos para programar em java
# Aula 7
uma apresentação rasa ao intelij apenas criando um projeto e o main
# Aula 08

pacotes
usando o . para criar direotrios
criou um pacote para cada modulo
falou sobre o uso do package pra dizer onde fica o caminho da importação

# Aula 09
### Comentários no IntelliJ

- `//` → comentário de linha
    
- `/* ... */` → comentário de várias linhas
    
- `/** ... */` → comentário **Javadoc** (único usado para documentação)
    

⚠️ **Atenção:** não devemos abusar de comentários no código, pois isso pode indicar que ele não está limpo nem legível o suficiente. O ideal é escrever um código claro, que "se explique" sozinho.