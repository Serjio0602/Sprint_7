package tests;

import io.restassured.RestAssured;
import org.junit.Before;

import static data.CourierData.BASE_URI;

public class BaseApiTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = BASE_URI;
    }
}
