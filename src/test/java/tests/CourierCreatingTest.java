package tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Courier;
import org.junit.After;
import org.junit.Test;

import static data.CourierData.*;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static steps.CourierSteps.*;

public class CourierCreatingTest extends BaseApiTest {

    private Courier courier;

    @Test
    @DisplayName("Успешное создание курьера")
    public void testCreateCourier() {

        String courierLogin = "CL_" + System.currentTimeMillis();
        courier = new Courier(courierLogin, COURIER_PASSWORD, COURIER_FIRST_NAME);
        createCourier(courier)
                .then()
                .log().all()
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));

    }

    @Test
    @DisplayName("Успешное создание курьера без имени")
    public void testCreateCourierWithoutFirstName() {

        String courierLogin = "CL_" + System.currentTimeMillis();
        courier = new Courier(courierLogin, COURIER_PASSWORD, null);
        createCourier(courier)
                .then()
                .log().all()
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));

    }

    @Test
    @DisplayName("Невозможно создать двух одинаковых курьеров")
    public void testImpossibleToCreateSameCourier() {

        String courierLogin = "CL_" + System.currentTimeMillis();
        courier = new Courier(courierLogin, COURIER_PASSWORD, COURIER_FIRST_NAME);
        createCourier(courier)
                .then()
                .log().all()
                .statusCode(SC_CREATED)
                .body("ok", equalTo(true));

        createCourier(courier)
                .then()
                .log().all()
                .statusCode(SC_CONFLICT)
                .body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

    }

    @Test
    @DisplayName("Невозможно создать курьера без логина")
    public void testCreateCourierWithoutLogin() {

        courier = new Courier(null, COURIER_PASSWORD, COURIER_FIRST_NAME);
        createCourier(courier)
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Невозможно создать курьера без пароля")
    public void testCreateCourierWithoutPassword() {

        String courierLogin = "CL_" + System.currentTimeMillis();
        courier = new Courier(courierLogin, null, COURIER_FIRST_NAME);
        createCourier(courier)
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @After
    public  void cleanUp() {
        if (courier != null && courier.getLogin() != null && courier.getPassword() != null) {
            Response loginResponse = loginCourier(courier);

            if (loginResponse.statusCode() == SC_OK) {
                int courierId = loginResponse.path("id");
                deleteCourier(courierId)
                        .then()
                        .statusCode(SC_OK);
            }

        }
    }
}
