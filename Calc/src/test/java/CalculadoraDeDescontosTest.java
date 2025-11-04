import static org.junit.jupiter.api.Assertions.*;

import org.example.CalculadoraDeDescontos;
import org.junit.jupiter.api.Test;

public class CalculadoraDeDescontosTest {

    @Test
    void deveRetornarValorSemDescontoParaComprasAbaixoDe100() {
        CalculadoraDeDescontos calc = new CalculadoraDeDescontos();
        double resultado = calc.calcular(80.0);
        assertEquals(80.0, resultado);
    }

    @Test
    void deveAplicar5PorCentoDeDescontoParaComprasEntre100e500() {
        CalculadoraDeDescontos calc = new CalculadoraDeDescontos();
        double resultado = calc.calcular(200.0);
        assertEquals(190.0, resultado); // 200 - 5%
    }

    @Test
    void deveAplicar10PorCentoDeDescontoParaComprasAcimaDe500() {
        CalculadoraDeDescontos calc = new CalculadoraDeDescontos();
        double resultado = calc.calcular(1000.0);
        assertEquals(900.0, resultado); // 1000 - 10%
    }

    @Test
    void deveLancarExcecaoParaValoresNegativos() {
        CalculadoraDeDescontos calc = new CalculadoraDeDescontos();
        assertThrows(IllegalArgumentException.class, () -> calc.calcular(-50.0));
    }
}
