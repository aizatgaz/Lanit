package org.lanit.tests;

import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.lanit.helpers.Constants;
import org.lanit.helpers.ObjectFillFaker;
import org.lanit.helpers.RestAssuredConfig;

import static io.restassured.RestAssured.given;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;

/**
 * Negative API tests for Petstore.
 */
@Tag("negative")
public class StoreAppNegativeTest {

    /**
     * Test for creating an order without a request body.
     */
    @Test
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

}
