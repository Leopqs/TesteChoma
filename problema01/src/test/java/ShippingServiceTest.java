import org.example.checkout.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ShippingServiceTest {
    @Test
    public void testeDeFretesSemDesconto(){
        var couponService = new CouponService();
        var ShippingService = new ShippingService();
        var service = new CheckoutService(couponService, ShippingService);
        var item = List.of(new Item("ACESSORIO", 80.00, 1));

        ///////////////////////////////////////FRETES NORTE
        var resNorte1 = service.checkout(
                item,
                CustomerTier.BASIC,
                false,
                "NORTE",
                1.0,
                null,
                LocalDate.now(),
                null
        );

        assertEquals(80.00, resNorte1.subtotal);
        assertEquals(0.00, resNorte1.discountValue);
        // imposto 12% sobre parte tributável: 80 (acessorio)
        assertEquals(9.60, resNorte1.tax);
        // frete NORTE com peso 1 → 30
        assertEquals(30.00, resNorte1.shipping);
        assertEquals(119.60, resNorte1.total);

        var resNorte2 = service.checkout(
                item,
                CustomerTier.BASIC,
                false,
                "NORTE",
                4.0,
                null,
                LocalDate.now(),
                null
        );

        assertEquals(80.00, resNorte2.subtotal);
        assertEquals(0.00, resNorte2.discountValue);
        // imposto 12% sobre parte tributável: 80 (acessorio)
        assertEquals(9.60, resNorte2.tax);
        // frete NORTE com peso 4 → 55
        assertEquals(55.00, resNorte2.shipping);
        assertEquals(144.6, resNorte2.total);

        var resNorte3 = service.checkout(
                item,
                CustomerTier.BASIC,
                false,
                "NORTE",
                6.0,
                null,
                LocalDate.now(),
                null
        );

        assertEquals(80.00, resNorte3.subtotal);
        assertEquals(0.00, resNorte3.discountValue);
        // imposto 12% sobre parte tributável: 80 (acessorio)
        assertEquals(9.60, resNorte3.tax);
        // frete NORTE com peso 6 → 80
        assertEquals(80.00, resNorte3.shipping);
        assertEquals(169.60, resNorte3.total);


        ///////////////////////////////////////FRETES SUL

        var resSul1 = service.checkout(
                item,
                CustomerTier.BASIC,
                false,
                "SUL",
                1.0,
                null,
                LocalDate.now(),
                null
        );

        assertEquals(80.00, resSul1.subtotal);
        // imposto 12% sobre parte tributável: 80 (acessorio)
        assertEquals(9.60, resSul1.tax);
        // frete SUL com peso 1 → 20
        assertEquals(20.00, resSul1.shipping);
        assertEquals(109.60, resSul1.total);

        var resSul2 = service.checkout(
                item,
                CustomerTier.BASIC,
                false,
                "SUL",
                4.0,
                null,
                LocalDate.now(),
                null
        );

        assertEquals(80.00, resSul2.subtotal);
        // imposto 12% sobre parte tributável: 80 (acessorio)
        assertEquals(9.60, resSul2.tax);
        // frete SUL com peso 4 → 35
        assertEquals(35.00, resSul2.shipping);
        assertEquals(124.60, resSul2.total);

        var resSul3 = service.checkout(
                item,
                CustomerTier.BASIC,
                false,
                "SUL",
                6.0,
                null,
                LocalDate.now(),
                null
        );

        assertEquals(80.00, resSul3.subtotal);
        // imposto 12% sobre parte tributável: 80 (acessorio)
        assertEquals(9.60, resSul3.tax);
        // frete SUL com peso 6 → 50
        assertEquals(50.00, resSul3.shipping);
        assertEquals(139.60, resSul3.total);


        ///////////////////////////////////////FRETES SUDESTE

        var resSudeste1 = service.checkout(
                item,
                CustomerTier.BASIC,
                false,
                "SUDESTE",
                1.0,
                null,
                LocalDate.now(),
                null
        );

        assertEquals(80.00, resSudeste1.subtotal);
        // imposto 12% sobre parte tributável: 80 (acessorio)
        assertEquals(9.60, resSudeste1.tax);
        // frete SUDESTE com peso 1 → 20
        assertEquals(20.00, resSudeste1.shipping);
        assertEquals(109.60, resSudeste1.total);

        var resSudeste2 = service.checkout(
                item,
                CustomerTier.BASIC,
                false,
                "SUDESTE",
                4.0,
                null,
                LocalDate.now(),
                null
        );

        assertEquals(80.00, resSudeste2.subtotal);
        // imposto 12% sobre parte tributável: 80 (acessorio)
        assertEquals(9.60, resSudeste2.tax);
        // frete SUDESTE com peso 4 → 35
        assertEquals(35.00, resSudeste2.shipping);
        assertEquals(124.60, resSudeste2.total);

        var resSudeste3 = service.checkout(
                item,
                CustomerTier.BASIC,
                false,
                "SUDESTE",
                6.0,
                null,
                LocalDate.now(),
                null
        );

        assertEquals(80.00, resSudeste3.subtotal);
        // imposto 12% sobre parte tributável: 80 (acessorio)
        assertEquals(9.60, resSudeste3.tax);
        // frete SUDESTE com peso 6 → 50
        assertEquals(50.00, resSudeste3.shipping);
        assertEquals(139.60, resSudeste3.total);


        ///////////////////////////////////////FRETES OUTRA REGIAO

        var resNordeste = service.checkout(
                item,
                CustomerTier.BASIC,
                false,
                "NORDESTE",
                1.0,
                null,
                LocalDate.now(),
                null
        );

        assertEquals(80.00, resNordeste.subtotal);
        // imposto 12% sobre parte tributável: 80 (acessorio)
        assertEquals(9.60, resNordeste.tax);
        // frete NORDESTE -> 40
        assertEquals(40.00, resNordeste.shipping);
        assertEquals(129.60, resNordeste.total);

        var resNordeste2 = service.checkout(
                item,
                CustomerTier.BASIC,
                false,
                "SUDOESTE",
                4.0,
                null,
                LocalDate.now(),
                null
        );

        assertEquals(80.00, resNordeste2.subtotal);
        // imposto 12% sobre parte tributável: 80 (acessorio)
        assertEquals(9.60, resNordeste2.tax);
        // frete SUDOESTE → 40
        assertEquals(40.00, resNordeste2.shipping);
        assertEquals(129.60, resNordeste2.total);

        var resNordeste3 = service.checkout(
                item,
                CustomerTier.BASIC,
                false,
                "CENTRO-OESTE",
                6.0,
                null,
                LocalDate.now(),
                null
        );

        assertEquals(80.00, resNordeste3.subtotal);
        // imposto 12% sobre parte tributável: 80 (acessorio)
        assertEquals(9.60, resNordeste3.tax);
        // frete CENTRO-OESTE → 40
        assertEquals(40.00, resNordeste3.shipping);
        assertEquals(129.60, resNordeste3.total);
    }
}
