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
 * Api tests for petstore.
 */
public class StoreAppTest {

    /**
     * Order id.
     */
    private Long orderId;

    /**
     * Test for receipt of pet inventory.
     */
    @Test
    @Tag("positive")
    public void getPetInventoriesTest() {
        RestAssuredConfig.setUp(HttpStatus.SC_OK);
        given()
                .when()
                .get(Constants.INVENTORY_URL)
                .then()
                .assertThat()
                .contentType(ContentType.JSON)
                .body(Matchers.notNullValue())
                .body("sold", Matchers.greaterThanOrEqualTo(0))
                .body("pending", Matchers.greaterThanOrEqualTo(0))
                .body("available", Matchers.greaterThanOrEqualTo(0));
    }

    /**
     * Test for creating an order for a pet.
     */
    @Test
    @Tag("positive")
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
                .body("petId", Matchers.equalTo(randomOrderPojo.getPetId()))
                .body("quantity", Matchers.equalTo(randomOrderPojo.getQuantity()))
                .body("shipDate", Matchers.containsString(randomOrderPojo.getShipDate().toInstant().toString().substring(0, 23)))
                .body("status", Matchers.equalTo(randomOrderPojo.getStatus()))
                .body("complete", Matchers.equalTo(randomOrderPojo.isComplete()))
                .extract()
                .as(OrderPojo.class);
        orderId = orderPojo.getId();
    }

    /**
     * Test for receiving a pet order by ID.
     */
    @Test
    @Tag("positive")
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
    @Tag("positive")
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
     * Test for creating an order without a request body.
     */
    @Test
    @Tag("negative")
    public void postOrderWithoutBodyTest() {
        RestAssuredConfig.setUp(HttpStatus.SC_BAD_REQUEST);
        given()
                .when()
                .post(Constants.ORDER_URL)
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath(Constants.MESSAGE_SCHEMA_PATH))
                .body("message", Matchers.equalToIgnoringCase(Constants.NO_DATA))
                .body("type", Matchers.equalToIgnoringCase(Constants.STATUS_TYPE_ERROR));
    }

    /**
     * Test for creating an order without a request body.
     */
    @Test
    @Tag("negative")
    public void postOrderWithIncorrectBodyTest() {
        RestAssuredConfig.setUp(HttpStatus.SC_BAD_REQUEST);
        given()
                .body(ObjectFillFaker.getRandomString())
                .when()
                .post(Constants.ORDER_URL)
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath(Constants.MESSAGE_SCHEMA_PATH))
                .body("message", Matchers.equalToIgnoringCase(Constants.BAD_INPUT))
                .body("type", Matchers.equalToIgnoringCase(Constants.STATUS_TYPE_UNKNOWN));
    }

    /**
     * Test for retrieving an order by a non-existing ID.
     */
    @Test
    @Tag("negative")
    public void getOrderByNonExistingIdTest() {
        RestAssuredConfig.setUp(HttpStatus.SC_NOT_FOUND);
        given()
                .pathParam("orderId", Constants.NON_EXISTING_ID)
                .when()
                .get(Constants.ORDER_URL_WITH_PARAM)
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath(Constants.MESSAGE_SCHEMA_PATH))
                .body("message", Matchers.equalToIgnoringCase(Constants.ORDER_NOT_FOUND))
                .body("type", Matchers.equalToIgnoringCase(Constants.STATUS_TYPE_ERROR));
    }

    /**
     * Test for retrieving an order by ID equal to symbols.
     */
    @Test
    @Tag("negative")
    public void getOrderBySymbolsTest() {
        RestAssuredConfig.setUp(HttpStatus.SC_NOT_FOUND);
        given()
                .pathParam("orderId", ObjectFillFaker.getRandomString())
                .when()
                .get(Constants.ORDER_URL_WITH_PARAM)
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath(Constants.MESSAGE_SCHEMA_PATH))
                .body("message", Matchers.containsString(Constants.NUMBER_FORMAT_EXCEPTION))
                .body("type", Matchers.equalToIgnoringCase(Constants.STATUS_TYPE_UNKNOWN));
    }

    /**
     * Test for retrieving an order without ID.
     */
    @Test
    @Tag("negative")
    public void getOrderWithoutIdTest() {
        RestAssuredConfig.setUp(HttpStatus.SC_METHOD_NOT_ALLOWED);
        given()
                .when()
                .get(Constants.ORDER_URL)
                .then()
                .assertThat()
                .contentType(ContentType.XML)
                .body("apiResponse.type", Matchers.equalToIgnoringCase(Constants.STATUS_TYPE_UNKNOWN));
    }

    /**
     * Test for deleting an order by a non-existing ID.
     */
    @Test
    @Tag("negative")
    public void deleteOrderByNonExistingIdTest() {
        RestAssuredConfig.setUp(HttpStatus.SC_NOT_FOUND);
        given()
                .pathParam("orderId", Constants.NON_EXISTING_ID)
                .when()
                .delete(Constants.ORDER_URL_WITH_PARAM)
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath(Constants.MESSAGE_SCHEMA_PATH))
                .body("message", Matchers.equalToIgnoringCase(Constants.ORDER_NOT_FOUND))
                .body("type", Matchers.equalToIgnoringCase(Constants.STATUS_TYPE_UNKNOWN));
    }

    /**
     * Test for deleting an order by ID equal to symbols.
     */
    @Test
    @Tag("negative")
    public void deleteOrderBySymbolsTest() {
        RestAssuredConfig.setUp(HttpStatus.SC_NOT_FOUND);
        given()
                .pathParam("orderId", ObjectFillFaker.getRandomString())
                .when()
                .get(Constants.ORDER_URL_WITH_PARAM)
                .then()
                .assertThat()
                .body(matchesJsonSchemaInClasspath(Constants.MESSAGE_SCHEMA_PATH))
                .body("message", Matchers.containsString(Constants.NUMBER_FORMAT_EXCEPTION))
                .body("type", Matchers.equalToIgnoringCase(Constants.STATUS_TYPE_UNKNOWN));
    }

    /**
     * Test for deleting an order without ID.
     */
    @Test
    @Tag("negative")
    public void deleteOrderWithoutIdTest() {
        RestAssuredConfig.setUp(HttpStatus.SC_METHOD_NOT_ALLOWED);
        given()
                .when()
                .delete(Constants.ORDER_URL)
                .then()
                .assertThat()
                .contentType(ContentType.XML)
                .body("apiResponse.type", Matchers.equalToIgnoringCase(Constants.STATUS_TYPE_UNKNOWN));
    }

    /**
     * Test for getting inventories with different 'accept' header.
     */
    @Test
    @Tag("negative")
    public void getInventoryWithDifferentAcceptHeaderTest() {
        RestAssuredConfig.setUp(HttpStatus.SC_NOT_ACCEPTABLE);
        given()
                .header("accept", "application/xml")
                .when()
                .get(Constants.INVENTORY_URL)
                .then()
                .assertThat()
                .contentType(ContentType.XML)
                .body("apiResponse.type", Matchers.equalToIgnoringCase(Constants.STATUS_TYPE_UNKNOWN));
    }

    /**
     * Test for getting inventories with different 'accept' header.
     */
    @Test
    @Tag("negative")
    public void getInventoryWithIncorrectAcceptHeaderTest() {
        RestAssuredConfig.setUp(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        given()
                .header("accept", ObjectFillFaker.getRandomString())
                .when()
                .get(Constants.INVENTORY_URL)
                .then()
                .assertThat()
                .contentType(ContentType.HTML)
                .body("html.head.title", Matchers.equalToIgnoringCase(Constants.STATUS_TYPE_SERVER_ERROR));
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
