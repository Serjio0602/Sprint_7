package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.Order;

import static io.restassured.RestAssured.given;

public class OrderSteps {

    @Step("Создание заказа")
    public static Response createOrder(Order order) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(order)
                .when()
                .post("/api/v1/orders")
                .then()
                .extract().response();
    }

    @Step("Отмена заказа по треку {track}")
    public static Response cancelOrder(int track) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .queryParam("track", track)
                .when()
                .put("/api/v1/orders/cancel")
                .then()
                .extract().response();
    }
}
