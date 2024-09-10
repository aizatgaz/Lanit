package org.lanit.helpers;

import com.github.javafaker.Faker;
import org.lanit.pojo.OrderPojo;

import java.util.Date;

public class ObjectFillFaker {

    public static OrderPojo createRandomOrderPojo() {
        Faker faker = new Faker();
        OrderPojo order = new OrderPojo();
        order.setPetId(faker.number().numberBetween(1, 500));
        order.setQuantity(faker.number().numberBetween(1, 100));
        order.setShipDate(faker.date().between(new Date(0), new Date()));
        order.setStatus(faker.options().option("placed", "approved", "delivered"));
        order.setComplete(faker.bool().bool());
        return order;
    }

    public static String getRandomString() {
        Faker faker = new Faker();
        return faker.random().hex();
    }

}
