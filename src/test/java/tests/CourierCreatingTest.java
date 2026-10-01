package tests;

import io.qameta.allure.junit4.DisplayName;
import model.Courier;
import org.junit.After;
import org.junit.Test;

import static data.CourierData.*;
import static org.hamcrest.Matchers.equalTo;
import static steps.CourierSteps.*;

public class CourierCreatingTest extends BaseApiTest {

    private int courierId;

    @Test
    @DisplayName("Успешное создание курьера")
    public void testCreateCourier() {

        String courierLogin = "CL_" + System.currentTimeMillis();
        Courier courier = new Courier(courierLogin, COURIER_PASSWORD, COURIER_FIRST_NAME);
        createCourier(courier)
                .then()
                .log().all()
                .statusCode(201)
                .body("ok", equalTo(true));

        courierId = loginCourier(courier)
                        .then()
                        .log().all()
                        .statusCode(200)
                        .extract().path("id");
    }

    @Test
    @DisplayName("Успешное создание курьера без имени")
    public void testCreateCourierWithoutFirstName() {

        String courierLogin = "CL_" + System.currentTimeMillis();
        Courier courier = new Courier(courierLogin, COURIER_PASSWORD, null);
        createCourier(courier)
                .then()
                .log().all()
                .statusCode(201)
                .body("ok", equalTo(true));

        courierId = loginCourier(courier)
                .then()
                .log().all()
                .statusCode(200)
                .extract().path("id");
    }

    @Test
    @DisplayName("Невозможно создать двух одинаковых курьеров")
    public void testImpossibleToCreateSameCourier() {

        String courierLogin = "CL_" + System.currentTimeMillis();
        Courier courier = new Courier(courierLogin, COURIER_PASSWORD, COURIER_FIRST_NAME);
        createCourier(courier)
                .then()
                .log().all()
                .statusCode(201)
                .body("ok", equalTo(true));

        createCourier(courier)
                .then()
                .log().all()
                .statusCode(409)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        courierId = loginCourier(courier)
                .then()
                .log().all()
                .statusCode(200)
                .extract().path("id");
    }

    @Test
    @DisplayName("Невозможно создать курьера без логина")
    public void testCreateCourierWithoutLogin() {

        Courier courier = new Courier(null, COURIER_PASSWORD, COURIER_FIRST_NAME);
        createCourier(courier)
                .then()
                .log().all()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Невозможно создать курьера без пароля")
    public void testCreateCourierWithoutPassword() {

        String courierLogin = "CL_" + System.currentTimeMillis();
        Courier courier = new Courier(courierLogin, null, COURIER_FIRST_NAME);
        createCourier(courier)
                .then()
                .log().all()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public  void cleanUp() {
        if (courierId != 0) {
            deleteCourier(courierId)
                .then()
                .statusCode(200);
        }
    }
}
