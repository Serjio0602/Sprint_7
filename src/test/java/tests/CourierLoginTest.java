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
    @DisplayName("Невозможно авторизоваться без логина")
    public void testLoginCourierWithoutLogin() {

        Courier courierWithoutLogin = new Courier("", COURIER_PASSWORD);
        loginCourier(courierWithoutLogin)
                .then()
                .log().all()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Невозможно авторизоваться без пароля")
    public void testLoginCourierWithoutPassword() {

        String courierLogin = "CL_" + System.currentTimeMillis();
        Courier courierWithoutPassword = new Courier(courierLogin, "");
        loginCourier(courierWithoutPassword)
                .then()
                .log().all()
                .statusCode(400)
                .body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Ошибка при вводе неправильного логина")
    public void testWrongLogin() {

        String courierLogin = "CL_" + System.currentTimeMillis();
        Courier courier = new Courier(courierLogin, COURIER_PASSWORD, COURIER_FIRST_NAME);
        createCourier(courier)
                .then()
                .statusCode(201);

        courierId = loginCourier(courier)
                .then()
                .statusCode(200)
                .extract().path("id");

        Courier courierWithWrongLogin = new Courier("Wrong_" + courierLogin, COURIER_PASSWORD);
        loginCourier(courierWithWrongLogin)
                .then()
                .log().all()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Ошибка при вводе неправильного пароля")
    public void testWrongPassword() {

        String courierLogin = "CL_" + System.currentTimeMillis();
        Courier courier = new Courier(courierLogin, COURIER_PASSWORD, COURIER_FIRST_NAME);
        createCourier(courier)
                .then()
                .statusCode(201);

        courierId = loginCourier(courier)
                .then()
                .statusCode(200)
                .extract().path("id");

        Courier courierWithWrongPassword = new Courier(courierLogin, COURIER_PASSWORD + System.currentTimeMillis());
        loginCourier(courierWithWrongPassword)
                .then()
                .log().all()
                .statusCode(404)
                .body("message", equalTo("Учетная запись не найдена"));
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
