import org.example.checkout.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CouponServiceTest {
    @Test
    public void usoDeCupons() {
        var couponService = new CouponService();
        var ShippingService = new ShippingService();
        var service = new CheckoutService(couponService, ShippingService);
        var item = List.of(new Item("ACESSORIO", 80.00, 1));

        var resNorte1 = service.checkout(
                item,
                CustomerTier.SILVER,
                false,
                "NORTE",
                1.0,
                "DES10",
                LocalDate.now(),
                null
        );

        assertEquals(80.00, resNorte1.subtotal);
        assertEquals(4.00, resNorte1.discountValue);
        assertEquals(9.12, resNorte1.tax);
        // frete NORTE com peso 1 → 30
        assertEquals(30.00, resNorte1.shipping);
        assertEquals(115.12, resNorte1.total);

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
        assertEquals(9.60, resNorte3.tax);
        // frete NORTE com peso 6 → 80
        assertEquals(80.00, resNorte3.shipping);
        assertEquals(169.60, resNorte3.total);

        var result = service.checkout(
                item,
                CustomerTier.GOLD,
                false,
                "NORDESTE",
                7.0,
                "DES20",
                LocalDate.now(),
                null
        );

        assertEquals(80.00, result.subtotal);
        assertEquals(8.00, result.discountValue);
        assertEquals(8.64, result.tax);
        // frete NORDESTE com peso 7 → 40
        assertEquals(40.00, result.shipping);
        assertEquals(120.64, result.total);

    }
}
