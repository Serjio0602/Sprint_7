package tests;

import data.OrderGenerator;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Order;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import steps.OrderSteps;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class OrderCreatingParamTest extends BaseApiTest {

    private List<String> colors;
    private Response response;
    private int track;

    public OrderCreatingParamTest(List<String> colors) {
        this.colors = colors;
    }

    @Parameterized.Parameters(name = "Цвет самоката: {0}")
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                { List.of("BLACK")},
                { List.of("GREY")},
                { List.of("BLACK", "GREY")},
                { null}
        });
    }

    @Test
    @DisplayName("Выбор цвета самоката")
    public void testCreateOrder() {

        Order order = OrderGenerator.getRandomOrder(colors);
        response = OrderSteps.createOrder(order);

        response.then()
                .log().all()
                .statusCode(201);

        track = response.path("track");
    }

    @After
    public void cleanUp() {
        if (track > 0) {
            OrderSteps.cancelOrder(track);
        }
    }
}
