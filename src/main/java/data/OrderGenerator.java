package data;

import com.github.javafaker.Faker;
import model.Order;

import java.util.List;
import java.util.Locale;

public class OrderGenerator {

    private static final Faker faker = new Faker(new Locale("ru"));

    public static Order getRandomOrder(List<String> colors) {
        return new Order(
                faker.name().firstName(),
                faker.name().lastName(),
                faker.address().streetAddress(),
                "2",
                faker.phoneNumber().phoneNumber(),
                faker.number().numberBetween(1, 7),
                "2026-10-08",
                faker.lorem().sentence(),
                colors
        );
    }
}
