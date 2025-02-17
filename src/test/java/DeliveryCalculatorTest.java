import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DeliveryCalculatorTest {
    @Test
    @Tag("Positive")
    @DisplayName("Тестирование минимальной стоимости доставки")
    void testMinimalOrder() {
        Delivery delivery = new Delivery(1, CargoDimension.SMALL, false, ServiceWorkload.NORMAL);
        assertEquals(400, delivery.calculateDeliveryCost());

    }

    @Test
    @Tag("Positive")
    @DisplayName("Тестирование максимальной стоимости доставки c нехрупким грузом")
    void testMaximalOrderWithNotFragileCargo(){
        Delivery delivery = new Delivery(50, CargoDimension.LARGE, false, ServiceWorkload.VERY_HIGH);
        assertEquals(800, delivery.calculateDeliveryCost());
    }

    @ParameterizedTest
    @Tag("Positive")
    @DisplayName("Тестирование комбинаций различных параметров")
    @CsvSource({
            "2, SMALL, false, NORMAL, 400.0",
            "2, SMALL, false, HIGH, 400.0",
            "2, LARGE, false, HIGH, 400.0",
            "10, SMALL, false, NORMAL, 400.0",
            "10, LARGE, false, VERY_HIGH, 480.0",
            "10, SMALL, true, NORMAL, 500.0",
            "30, SMALL, false, INCREASED, 400.0"
    })
    void testCombination(int destination, CargoDimension cargoDimension, boolean isFragile, ServiceWorkload serviceWorkload, Double finalConstExpected){
        Delivery delivery = new Delivery(destination, cargoDimension, isFragile, serviceWorkload);
        Double finalCostCalculated = delivery.calculateDeliveryCost();
        assertEquals(finalConstExpected, finalCostCalculated);
    }

    @Test
    @Tag("Negative")
    @DisplayName("Тестирование исключения для хрупкого груза более 30 км")
    void testFragileCargoDelivery31Km() {
        Delivery delivery = new Delivery(31, CargoDimension.SMALL, true, ServiceWorkload.NORMAL);
        Throwable exception = assertThrows(
                UnsupportedOperationException.class,
                delivery::calculateDeliveryCost
        );
        assertEquals("Fragile cargo cannot be delivered for the distance more than 30", exception.getMessage());
    }
}
