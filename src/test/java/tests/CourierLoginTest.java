package tests;

import io.qameta.allure.junit4.DisplayName;
import model.Courier;
import org.junit.After;
import org.junit.Test;

import static data.CourierData.*;
import static org.hamcrest.Matchers.equalTo;
import static steps.CourierSteps.*;

public class CourierLoginTest extends BaseApiTest {

    private int courierId;

    @Test
    @DisplayName("Успешная авторизация курьера")
    public void testLoginCourier() {

        String courierLogin = "CL_" + System.currentTimeMillis();
        Courier courier = new Courier(courierLogin, COURIER_PASSWORD, COURIER_FIRST_NAME);
        createCourier(courier)
                .then()
                .log().all()
                .statusCode(201)
                .body("ok", equalTo(true));

        courierId = loginCourier(courier)
                .then()
                .statusCode(200)
                .extract().path("id");

        loginCourier(courier)
                .then()
                .log().all()
                .statusCode(200)
                .body("id", equalTo(courierId));
    }

    @Test


    @After
    public  void cleanUp() {
        if (courierId != 0) {
            deleteCourier(courierId)
                    .then()
                    .statusCode(200);
        }
    }
}
