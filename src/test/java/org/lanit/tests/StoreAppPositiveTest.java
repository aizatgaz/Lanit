package org.lanit.tests;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.lanit.helpers.Constants;
import org.lanit.helpers.ObjectFillFaker;
import org.lanit.helpers.RestAssuredConfig;
import org.lanit.pojo.OrderPojo;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

/**
 * Positive api tests for petstore.
 */
@Tag("positive")
public class StoreAppPositiveTest {

    /**
     * Order id.
     */
    private Long orderId;

    /**
     * Test for receipt of pet inventory.
     */
    @Test
    public void getPetInventoriesTest() {
        RestAssuredConfig.setUp(HttpStatus.SC_OK);
        given()
                .when()
                .get(Constants.INVENTORY_URL)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .body(Matchers.notNullValue());
    }

    /**
     * Test for creating an order for a pet.
     */
    @Test
    public void postOrderTest() {
        RestAssuredConfig.setUp(HttpStatus.SC_OK);
        OrderPojo randomOrderPojo = ObjectFillFaker.createRandomOrderPojo();
        OrderPojo orderPojo = given()
                .body(randomOrderPojo)
                .when()
                .post(Constants.ORDER_URL)
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath(Constants.ORDER_SCHEMA_PATH))
                .body("id", Matchers.notNullValue())
                .extract()
                .as(OrderPojo.class);
        orderId = orderPojo.getId();
    }

    /**
     * Test for receiving a pet order by ID.
     */
    @Test
    public void getOrderById() {
        RestAssuredConfig.setUp(HttpStatus.SC_OK);
        OrderPojo randomOrderPojo = ObjectFillFaker.createRandomOrderPojo();
        OrderPojo orderPojo = given()
                .body(randomOrderPojo)
                .when()
                .post(Constants.ORDER_URL)
                .then()
                .extract()
                .as(OrderPojo.class);
        orderId = orderPojo.getId();
        OrderPojo result = given()
                .when()
                .pathParam("orderId", orderId)
                .get(Constants.ORDER_URL_WITH_PARAM)
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath(Constants.ORDER_SCHEMA_PATH))
                .extract()
                .as(OrderPojo.class);
        Assertions.assertEquals(orderPojo, result);
    }

    /**
     * Test for deleting a pet order by ID.
     */
    @Test
    public void deleteOrderById() {
        RestAssuredConfig.setUp(HttpStatus.SC_OK);
        OrderPojo randomOrderPojo = ObjectFillFaker.createRandomOrderPojo();
        OrderPojo orderPojo = given()
                .body(randomOrderPojo)
                .when()
                .post(Constants.ORDER_URL)
                .then()
                .extract()
                .as(OrderPojo.class);
        given()
                .when()
                .pathParam("orderId", orderPojo.getId())
                .delete(Constants.ORDER_URL_WITH_PARAM)
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath(Constants.MESSAGE_SCHEMA_PATH))
                .body("message", Matchers.equalToIgnoringCase(orderPojo.getId().toString()));
    }

    /**
     * Clear data after test.
     */
    @AfterEach
    public void clearData() {
        if (orderId != null) {
            given()
                    .when()
                    .pathParam("orderId", orderId)
                    .delete(Constants.ORDER_URL_WITH_PARAM);
        }
    }
}
