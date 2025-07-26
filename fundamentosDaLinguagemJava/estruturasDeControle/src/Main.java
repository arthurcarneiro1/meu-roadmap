
public class Main {
    public static void main(String[] args) {
        //Estrutura if, else if, else
        //Permite executar um bloco de código caso uma condição seja verdadeira.
        int idade = 18;

        // Verificamos se a idade é maior ou igual a 18
        if (idade >= 18) {
            System.out.println("Você é maior de idade."); // Executa se a condição for verdadeira
        } else {
            System.out.println("Você é menor de idade."); // Executa se a condição for falsa
        }
        //Você também pode usar else if para múltiplas verificações:
        int nota = 7;


        if (nota >= 9) {
            System.out.println("Excelente!");
        } else if (nota >= 7) {
            System.out.println("Aprovado!");
        } else {
            System.out.println("Reprovado!");
        }

        //--------------------------------------------------------------------------------------
        //🔁 switch — Condicional Múltipla
        //Ideal para testar uma variável contra vários valores.
        // O switch compara a variável 'dia' com os valores definidos nos cases
        int dia = 3;
        switch (dia) {
            case 1:
                System.out.println("Domingo");
                break; // Interrompe o switch após executar esse case
            case 2:
                System.out.println("Segunda-feira");
                break;
            case 3:
                System.out.println("Terça-feira");
                break;
            default:
                // Executa se nenhum dos casos anteriores for verdadeiro
                System.out.println("Dia inválido");
        }

        //--------------------------------------------------------------------------------------
        //🔁 while — Laço de Repetição com Verificação no Início
        //Repete o bloco enquanto a condição for verdadeira.
        int contador = 0;

        while (contador < 5) {
            System.out.println("Contador: " + contador);
            contador++; // Incrementa o valor para não ficar em loop infinito
        }

        //--------------------------------------------------------------------------------------
        //🔁 Estrutura do-while
        // O bloco será executado pelo menos uma vez, mesmo que a condição seja falsa
        int numero = 1;


        do {
            System.out.println("Número: " + numero);
            numero++; // Incrementa antes de verificar a condição
        } while (numero <= 3); // Verificação acontece após a execução do bloco


        //--------------------------------------------------------------------------------------
        //🔁 Estrutura for
        // for(inicialização; condição; incremento)
        for (int i = 0; i < 5; i++) {
            System.out.println("Valor de i: " + i);
            // O loop se repete até que i < 5 seja falso
        }
    }
}
