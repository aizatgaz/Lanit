package org.lanit.helpers;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;

/**
 * Rest Assured configuration for testing.
 */
public final class RestAssuredConfig {

    public static void setUp(final Integer statusCode) {
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setContentType(ContentType.JSON)
                .setBaseUri(Constants.BASE_URL)
                .log(LogDetail.ALL)
                .build();
        RestAssured.responseSpecification = new ResponseSpecBuilder()
                .expectStatusCode(statusCode)
                .build();
    }

    private RestAssuredConfig() {}

}
