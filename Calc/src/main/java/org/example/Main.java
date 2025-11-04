package org.example;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        CalculadoraDeDescontos calculadora = new CalculadoraDeDescontos();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Digite o valor da compra:");

        double valorCompra = scanner.nextDouble();

        try {
            double valorComDesconto = calculadora.calcular(valorCompra);

            System.out.printf("Valor da compra com desconto: R$ %.2f\n", valorComDesconto);
        } catch (IllegalArgumentException e) {
            System.out.println("Erro: " + e.getMessage());
        } finally {
            scanner.close();
        }
    }
}
