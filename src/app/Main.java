package app;

import entity.Conta;
import excessoes.SaldoInsuficienteException;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        Map<String, Conta> contas = new HashMap<>();

        carregarDados(contas);

        int opcao = 0;

        while (opcao != 6){

            System.out.println("=== Sistema ATM ===");

            System.out.println("1. Criar conta");
            System.out.println("2. Fazer uma deposito");
            System.out.println("3. Sacar um valor");
            System.out.println("4. Consultar saldo");
            System.out.println("5. Tranferência");
            System.out.println("6. Sair");
            System.out.println("Escolha uma opção");
            opcao = Integer.parseInt(sc.nextLine());

            switch (opcao){
                case 1:
                    System.out.println("Digite o nome do titular");
                    String nomeTitular = sc.nextLine();

                    Conta novaConta = new Conta(nomeTitular);

                    contas.put(nomeTitular, novaConta); // contas.put guarda a "ficha"
                                                        // put para guardar ou substituir algo.

                    System.out.println("Conta criada com sucesso!" + nomeTitular);

                    break;

                case 2:

                    System.out.println("Digite o nome do titular para depósito");
                    String buscaNome = sc.nextLine();

                    // tenta achar a conta no Map
                    Conta contaEncontrada = contas.get(buscaNome); // contas.get a recupera pelo nome.
                                                                    // get para pegar algo que já existe

                    if(contaEncontrada != null){
                        System.out.println("Quanto deseja depositar?");
                        double valor = Double.parseDouble(sc.nextLine());

                        try {
                            contaEncontrada.depositar(valor);
                            System.out.println("Novo saldo: R$" + contaEncontrada.getSaldo());
                        }catch (IllegalArgumentException e){
                            System.out.println("Erro: " + e.getMessage());
                        }
                    }else {
                        System.out.println("Conta não encontrada");
                    }
                    break;

                case 3:

                    System.out.println("Digite o nome do titular para saque");
                    String titularSaque = sc.nextLine();

                    // tenta achar a conta no Map
                    Conta contaSaque = contas.get(titularSaque);

                    if(contaSaque != null){
                        try {
                            System.out.println("Valor do saque?");
                            double valor = Double.parseDouble(sc.nextLine());

                            contaSaque.sacar(valor);
                            System.out.println("Saque realizado! Saldo atual: R$" + contaSaque.getSaldo());

                        }catch (SaldoInsuficienteException e){
                            System.out.println("Erro: " + e.getMessage());
                        }catch (NumberFormatException e){
                            System.out.println("Erro: digite um número valido");
                        }
                    }else {
                        System.out.println("Conta não encontrada");
                    }

                    break;

                case 4:
                    System.out.println("=== Relatorio geral de saldo ===");
                    contas.forEach((n, c) -> {
                        System.out.println("Titular " + n + " | Saldo: R$ " + c.getSaldo());
                    });

                    break;

                case 5:
                    System.out.println("Nome da conta de ORIGEM");
                    String nomeOrigem = sc.nextLine();

                    System.out.println("Nome da conta de DESTINO");
                    String nomeDestino = sc.nextLine();

                    Conta origem = contas.get(nomeOrigem);
                    Conta destino = contas.get(nomeDestino);

                    if(origem != null && destino != null){
                        try {
                            System.out.println("Valor da transferência");
                            double valorTransferencia = Double.parseDouble(sc.nextLine());

                            origem.sacar(valorTransferencia);
                            destino.depositar(valorTransferencia);

                            System.out.println("Transferência realizada com sucesso!");
                            System.out.println("Novo saldo " + origem.getTitular() + ": R$ " + origem.getSaldo());
                        }catch (SaldoInsuficienteException e){
                            System.out.println("ERRO NA TRANSAÇÃO" + e.getMessage());
                        }catch (NumberFormatException e){
                            System.out.println("ERRO: digite um número valido");
                        }
                    }else {
                        System.out.println("Uma ou ambas as contas não encontrada, tente novamente");
                    }
                    break;

                case 6:
                    salvarDados(contas);
                    System.out.println("Saindo do sistema... Até logo.");
                    break;

                default:
                    System.out.println("ERRO: opção inválida");
            }
        }
        sc.close();
    }
    public static void carregarDados(Map<String, Conta> contas) {
        File arquivo = new File("contas.txt");
        if (!arquivo.exists()) return;

        try (Scanner leitor = new Scanner(arquivo)) {
            while (leitor.hasNextLine()) {
                String linha = leitor.nextLine();
                String[] partes = linha.split(";");
                if (partes.length < 2) continue;

                Conta c = new Conta(partes[0]);
                c.depositar(Double.parseDouble(partes[1]));
                contas.put(partes[0], c);
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar dados.");
        }
    }

    public static void salvarDados(Map<String, Conta> contas) {
        try (PrintWriter writer = new PrintWriter(new FileWriter("contas.txt"))) {
            for (Conta conta : contas.values()) {
                writer.println(conta.getTitular() + ";" + conta.getSaldo());
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar dados.");
        }
    }
}
