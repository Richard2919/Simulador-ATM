package entity;

import excessoes.SaldoInsuficienteException;

public class Conta {
    private String titular;
    private double saldo;

    public Conta(){
    }

    public Conta(String titular) {
        this.titular = titular;
        this.saldo = 0.0;
    }

    public String getTitular() {
        return titular;
    }

    public void setTitular(String titular) {
        this.titular = titular;
    }

    public double getSaldo() {
        return saldo;
    }

    public void depositar(double valor){
        if (valor < 0){
            throw new IllegalArgumentException("O valor do depósito deve ser maior que zero.");
        }
        this.saldo += valor;
    }

    public void sacar(double valor){
        if(valor > saldo){
            throw new SaldoInsuficienteException("Saldo insuficiente! Saldo atual: R$ " + saldo);
        }
        this.saldo -= valor;
    }
}
