package tests;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.Courier;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static data.CourierData.*;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static steps.CourierSteps.*;

public class CourierLoginTest extends BaseApiTest {

    private Courier courier;

    @Before
    public void setUpCourier() {

        String courierLogin = "CL_" + System.currentTimeMillis();
        courier = new Courier(courierLogin, COURIER_PASSWORD, COURIER_FIRST_NAME);

        createCourier(courier)
                .then()
                .statusCode(SC_CREATED);
    }

    @Test
    @DisplayName("Успешная авторизация курьера")
    public void testLoginCourier() {
        loginCourier(courier)
                .then()
                .log().all()
                .statusCode(SC_OK)
                .body("id", notNullValue());
    }

    @Test
    @DisplayName("Невозможно авторизоваться без логина")
    public void testLoginCourierWithoutLogin() {

        Courier courierWithoutLogin = new Courier("", COURIER_PASSWORD);
        loginCourier(courierWithoutLogin)
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Невозможно авторизоваться без пароля")
    public void testLoginCourierWithoutPassword() {

        Courier courierWithoutPassword = new Courier(courier.getLogin(), "");
        loginCourier(courierWithoutPassword)
                .then()
                .log().all()
                .statusCode(SC_BAD_REQUEST)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка при вводе неправильного логина")
    public void testWrongLogin() {

        Courier courierWithWrongLogin = new Courier("Wrong_" + courier.getLogin(), courier.getPassword());

        loginCourier(courierWithWrongLogin)
                .then()
                .log().all()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Ошибка при вводе неправильного пароля")
    public void testWrongPassword() {

        Courier courierWithWrongPassword = new Courier(courier.getLogin(), courier.getPassword() + System.currentTimeMillis());
        loginCourier(courierWithWrongPassword)
                .then()
                .log().all()
                .statusCode(SC_NOT_FOUND)
                .body("message", equalTo("Учетная запись не найдена"));
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
