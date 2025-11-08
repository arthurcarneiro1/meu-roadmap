Classes ultilitarias

wapperes

transformar tipos primitivos em class
e so colocar a letra maisculas

tipos primitivos ainda tem preferencia

unboxing e boxing

boxing 

Integer i = 10;

unboxing
int f = i;

wapper pssuem parse pra conservçaõ de string

string

metodos
![[Captura de tela 2025-09-24 094833.png]]


String biulder
preciamos estanciar
stringbuilder ssb= new srtringbulder();

sb.append(); pra adicionar strings
.delete(0,3) deleta carateres
.reverse(); invert
.subtring()


class

Locale
usado pra definir o local que vc quer trablhar
locele brasil = new localce)(br);

usamos tbm pra padrao de numeros e moedas de cada pais


Date usual em  java
localdate usadao pra trabalhr com datas
localdate data = localdate(ano,mes,dia)
get vc pega informaçoes

localdate.now pega data de agora

plus adicionar 

localtime pra tarblhar com time maximo 00:00
get pega os dados

localdatetima junta os dois anteriores

parse pra converte stringg em date

instant
pega os segundos

duration 
usada pra trabalhar com intervalo de tempo
beetween pega o intervalo

period 
pra trabalhar com intervalo de dias

ChronoUnit para intervalos
mas podo

temporalladjuste serve pra ajustar datas
tipo pegar a proxima quinta feira, o primeiro dia daquele mes, o ultimo

dateformat
pra transformar data em string

Perfeito 🚀. Aqui está a **documentação completa** em **Markdown** com explicações e exemplos práticos:

---

# 📘 Documentação: Classes Utilitárias em Java

---

## 1. Wrappers

### 🔹 O que são

- **Classes que encapsulam tipos primitivos**, permitindo tratá-los como objetos.
    
- Necessários para uso em **coleções** (List, Set, etc.).
    
- Possuem **métodos utilitários** (conversão, parsing, comparação).
    

### 🔹 Tabela de Wrappers

|Primitivo|Classe Wrapper|
|---|---|
|byte|Byte|
|short|Short|
|int|Integer|
|long|Long|
|float|Float|
|double|Double|
|char|Character|
|boolean|Boolean|

### 🔹 Autoboxing e Unboxing

```java
public class WrapperExample {
    public static void main(String[] args) {
        // Autoboxing
        int primitivo = 10;
        Integer wrapper = primitivo; // int -> Integer

        // Unboxing
        Double wDouble = 45.6;
        double pDouble = wDouble; // Double -> double

        // Métodos úteis
        String valor = "123";
        int numero = Integer.parseInt(valor); // String -> int
        double num2 = Double.valueOf("45.78"); // String -> Double

        System.out.println("Numero: " + numero);
        System.out.println("Double: " + num2);
    }
}
```

---

## 2. Strings

### 🔹 Características

- `String` é **imutável**.
    
- Cada alteração cria um **novo objeto em memória**.
    
- Para muitas modificações, use `StringBuilder` ou `StringBuffer`.
    

### 🔹 Operações comuns

```java
public class StringExample {
    public static void main(String[] args) {
        String nome = "Arthur";
        
        System.out.println(nome.length());          // 6
        System.out.println(nome.toUpperCase());     // "ARTHUR"
        System.out.println(nome.toLowerCase());     // "arthur"
        System.out.println(nome.charAt(2));         // 't'
        System.out.println(nome.substring(0, 3));   // "Art"
        System.out.println(nome.contains("thur"));  // true
    }
}
```

### 🔹 Comparação de Strings

```java
String a = "Java";
String b = "Java";
String c = new String("Java");

System.out.println(a == b);      // true (mesma referência do pool)
System.out.println(a == c);      // false (objetos diferentes)
System.out.println(a.equals(c)); // true (conteúdo igual)
```

### 🔹 StringBuilder

```java
public class StringBuilderExample {
    public static void main(String[] args) {
        StringBuilder sb = new StringBuilder("Olá");
        sb.append(" Mundo");
        sb.insert(3, " Dev");
        sb.replace(0, 3, "Oi");
        sb.delete(0, 2);

        System.out.println(sb.toString()); // " Dev Mundo"
    }
}
```

---

## 3. Internacionalização (Locale)

### 🔹 Locale

Define **formato regional** (idioma, moeda, números, datas).

```java
import java.text.NumberFormat;
import java.util.Locale;

public class LocaleExample {
    public static void main(String[] args) {
        Locale brasil = new Locale("pt", "BR");
        Locale us = Locale.US;

        double valor = 12345.67;

        NumberFormat nfBr = NumberFormat.getCurrencyInstance(brasil);
        NumberFormat nfUs = NumberFormat.getCurrencyInstance(us);

        System.out.println("Brasil: " + nfBr.format(valor)); // R$ 12.345,67
        System.out.println("EUA: " + nfUs.format(valor));    // $12,345.67
    }
}
```

---

## 4. API de Datas Nova (`java.time`)

### 🔹 LocalDate

```java
import java.time.LocalDate;

public class LocalDateExample {
    public static void main(String[] args) {
        LocalDate hoje = LocalDate.now();
        LocalDate natal = LocalDate.of(2025, 12, 25);

        System.out.println("Hoje: " + hoje);
        System.out.println("Natal: " + natal);
        System.out.println("Ano: " + hoje.getYear());
    }
}
```

### 🔹 LocalTime

```java
import java.time.LocalTime;

public class LocalTimeExample {
    public static void main(String[] args) {
        LocalTime agora = LocalTime.now();
        LocalTime horaFixa = LocalTime.of(14, 30);

        System.out.println("Agora: " + agora);
        System.out.println("Hora fixa: " + horaFixa);
    }
}
```

### 🔹 LocalDateTime

```java
import java.time.LocalDateTime;

public class LocalDateTimeExample {
    public static void main(String[] args) {
        LocalDateTime agora = LocalDateTime.now();
        System.out.println("Data e hora: " + agora);
    }
}
```

### 🔹 Instant

```java
import java.time.Instant;

public class InstantExample {
    public static void main(String[] args) {
        Instant agora = Instant.now();
        System.out.println("Instant: " + agora);
    }
}
```

### 🔹 Duration e Period

```java
import java.time.*;
import java.time.temporal.ChronoUnit;

public class DurationPeriodExample {
    public static void main(String[] args) {
        LocalDate hoje = LocalDate.now();
        LocalDate futuro = hoje.plusDays(40);

        Period p = Period.between(hoje, futuro);
        System.out.println("Diferença: " + p.getDays() + " dias");

        LocalTime t1 = LocalTime.of(10, 0);
        LocalTime t2 = LocalTime.of(15, 30);
        Duration d = Duration.between(t1, t2);
        System.out.println("Duração: " + d.toHours() + " horas");
    }
}
```

### 🔹 ChronoUnit

```java
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class ChronoUnitExample {
    public static void main(String[] args) {
        long dias = ChronoUnit.DAYS.between(LocalDate.now(), LocalDate.now().plusMonths(1));
        System.out.println("Diferença em dias: " + dias);
    }
}
```

### 🔹 TemporalAdjusters

```java
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.time.DayOfWeek;

public class TemporalAdjustersExample {
    public static void main(String[] args) {
        LocalDate hoje = LocalDate.now();
        LocalDate proximaSegunda = hoje.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
        System.out.println("Próxima segunda: " + proximaSegunda);
    }
}
```

### 🔹 ZonedDateTime e ZoneId

```java
import java.time.*;

public class ZonedExample {
    public static void main(String[] args) {
        ZonedDateTime agora = ZonedDateTime.now(ZoneId.of("America/Sao_Paulo"));
        System.out.println("São Paulo: " + agora);
    }
}
```

### 🔹 DateTimeFormatter

```java
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatterExample {
    public static void main(String[] args) {
        LocalDateTime agora = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        String formatado = agora.format(formatter);

        System.out.println("Data formatada: " + formatado);
    }
}
```

---

# ✅ Conclusão

Com esses tópicos, você dominou:

- **Wrappers**: encapsulamento de primitivos.
    
- **Strings**: manipulação e performance (`StringBuilder`).
    
- **Internacionalização**: `Locale`, números, moedas e datas.
    
- **API moderna de datas (`java.time`)**: LocalDate, LocalTime, LocalDateTime, Instant, Duration, Period, ChronoUnit, TemporalAdjusters, ZonedDateTime e DateTimeFormatter.
    

---

👉 Quer que eu transforme esse material em um **pacote de flashcards estilo Anki** (perguntas e respostas) para reforçar a memorização?

# aula 130
resoucerbulde
usado pra definir qual lingua sera  mostradao determiado bloco, como string
pra criar usamos o padraão propeties

nome_pt_BR.propeties = para cada lingua

ai temos as keys e valores
 hi = oi

pra usar
temos que ter o locale

Locale br = new Locale(pt,br)
ResourBuild brasil = ResourBuild.getBueld(nome,br);

# aula 131
Regex
Usado pra trazer string baseado nos caractere que vc ta procurando 

String regex = "ab";
String text = "abaab";

Patten patten = Patten.compile(regex);
Matche match = patten.matcher(text);

While(match.find){
Sout(match.start match.group)
}
Start mostra o índice inicial 
Grupo mostra o grupo

# expressões regulares 


---

📘 Notas de Expressões Regulares (Regex)

🔢 Dígitos

\d → Representa todos os dígitos (0-9)
Exemplo:

Regex: \d

Texto: "A sala 25 está no 3º andar"

Match: 2, 5, 3


\D → Representa tudo o que não for dígito
Exemplo:

Regex: \D+

Texto: "Rua 45B"

Match: "Rua ", "B"




---

␣ Espaços

\s → Representa espaços em branco (espaço, tab \t, nova linha \n, etc.)
Exemplo:

Regex: \s

Texto: "Olá mundo"

Match: " "


\S → Representa qualquer caractere que não seja espaço em branco
Exemplo:

Regex: \S+

Texto: "Oi 😊"

Match: "Oi", "😊"




---

🔡 Palavras

\w → Representa letras (a-z, A-Z), dígitos (0-9) e underscore _
Exemplo:

Regex: \w+

Texto: "user_123"

Match: "user_123"


\W → Representa tudo o que não for incluído em \w
Exemplo:

Regex: \W

Texto: "C# e Java!"

Match: "#", " ", "!"




---

🔢 Quantificadores

? → Zero ou uma ocorrência
Exemplo:

Regex: colou?r

Texto: "color, colour"

Match: "color", "colour"


* → Zero ou mais ocorrências
Exemplo:

Regex: go*

Texto: "g, go, goo, gooo"

Match: "g", "go", "goo", "gooo"


+ → Uma ou mais ocorrências
Exemplo:

Regex: ha+

Texto: "ha, haa, haaa"

Match: "ha", "haa", "haaa"


{n,m} → De n até m ocorrências
Exemplo:

Regex: a{2,4}

Texto: "a aa aaa aaaa aaaaa"

Match: "aa", "aaa", "aaaa"




---

🔀 Agrupamentos

() → Agrupa expressões
Exemplo:

Regex: (ab)+

Texto: "ababab xyz"

Match: "ababab"


| → Ou lógico
Exemplo:

Regex: cachorro|gato

Texto: "Eu tenho um gato e um cachorro"

Match: "gato", "cachorro"




---

🎯 Posições

^ → Início da string
Exemplo:

Regex: ^Olá

Texto: "Olá mundo"

Match: "Olá"


$ → Final da string
Exemplo:

Regex: mundo$

Texto: "Olá mundo"

Match: "mundo"




---

📍 Outros

. → Representa qualquer caractere, exceto quebra de linha
Exemplo:

Regex: 1.3

Texto: "123, 133, 1@3, 1A3"

Match: "123", "133", "1@3", "1A3"


[] → Define um conjunto de caracteres permitidos
Exemplo:

Regex: [aeiou]

Texto: "regex"

Match: "e", "e"




---

# aula 139
IO trabalhar com arquivos 

Class file
File nome  = new file(text.tx);

Nome.createFile()
Nome.delete
Nome.getpath

Class fileWriter
Usada pra escrever 

Try(FileWitre arquivo = new FileWrite(file);){


Arquivo.write(string)
Arquivo.flush
}

Precismo usar um bloco trys pq a file gera uma exceção 

BuffeterWrite
Pra escrita
Ele usa um Filw Write pra escrever
Br.newline

Buffetereader
Recebe readaer

Criar diretorio
File.mkdir