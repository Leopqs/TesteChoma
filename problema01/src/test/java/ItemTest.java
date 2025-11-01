import org.example.checkout.*;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ItemTest {
    @Test
    public void getsItem(){
        var item = List.of( new Item("ACESSORIO", 80.00, 1));

        assertEquals("ACESSORIO", item.getFirst().getCategoria());
        assertEquals(80.00, item.getFirst().getPrecoUnitario());
        assertEquals(1, item.getFirst().getQuantidade());
    }
}
