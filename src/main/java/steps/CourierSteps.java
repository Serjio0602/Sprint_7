package steps;

import io.qameta.allure.Step;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import model.Courier;

import static data.CourierData.*;
import static io.restassured.RestAssured.given;

public class CourierSteps {

    @Step("Создание курьера")
    public static Response createCourier(Courier courier) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(CREATE_COURIER_PATH)
                .then()
                .extract().response();
    }

    @Step("Логин курьера")
    public static Response loginCourier(Courier courier) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .body(courier)
                .when()
                .post(LOGIN_COURIER_PATH)
                .then()
                .extract().response();
    }

    @Step("Удаление курьера")
    public static Response deleteCourier(int courierId) {
        return given()
                .log().all()
                .contentType(ContentType.JSON)
                .when()
                .delete(CREATE_COURIER_PATH + "/" + courierId)
                .then()
                .extract().response();
    }
}
