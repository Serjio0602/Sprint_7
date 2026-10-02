package tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.Test;
import steps.OrderSteps;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class OrderListTest extends BaseApiTest {

    @Test
    @DisplayName("Получение списка заказов возвращает массив")
    public void testOrderList() {

        Response response = OrderSteps.getOrderList();

        response.then().statusCode(200);
        List<Object> orders = response.path("orders");
        assertThat("Массив не должен быть null", orders, is(notNullValue()));
    }
}
