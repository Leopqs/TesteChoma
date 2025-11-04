package org.example;

public class CalculadoraDeDescontos {

    public double calcular(double valor) {
        if (valor < 0) {
            throw new IllegalArgumentException("Valor não pode ser negativo");
        }

        if (valor < 100) {
            return valor;
        }

        if (valor <= 500) {
            return valor * 0.95; // 5% off
        }

        return valor * 0.90; // 10% off
    }
}

