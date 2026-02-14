package com.autoflex.stockcontrol.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class ProductionSuggestionResourceTest {

    @Test
    void testSuggestionEndpoint() {
        given()
            .when().get("/api/production-suggestions")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON)
            .body("suggestedProducts", notNullValue())
            .body("totalValue", notNullValue());
    }

    @Test
    void testSuggestionWithData() {
        // Create raw materials
        Long woodId = given()
            .contentType(ContentType.JSON)
            .body("{\"name\": \"Wood-S\", \"stockQuantity\": 100.00}")
            .when().post("/api/raw-materials")
            .then().statusCode(201)
            .extract().jsonPath().getLong("id");

        Long nailId = given()
            .contentType(ContentType.JSON)
            .body("{\"name\": \"Nail-S\", \"stockQuantity\": 200.00}")
            .when().post("/api/raw-materials")
            .then().statusCode(201)
            .extract().jsonPath().getLong("id");

        // Create products
        Long expensiveId = given()
            .contentType(ContentType.JSON)
            .body("{\"name\": \"Expensive Chair\", \"value\": 500.00}")
            .when().post("/api/products")
            .then().statusCode(201)
            .extract().jsonPath().getLong("id");

        Long cheapId = given()
            .contentType(ContentType.JSON)
            .body("{\"name\": \"Cheap Stool\", \"value\": 50.00}")
            .when().post("/api/products")
            .then().statusCode(201)
            .extract().jsonPath().getLong("id");

        // Associate: Expensive Chair needs 10 wood, 20 nails
        given()
            .contentType(ContentType.JSON)
            .body("{\"productId\": " + expensiveId + ", \"rawMaterialId\": " + woodId + ", \"quantity\": 10.00}")
            .when().post("/api/product-raw-materials")
            .then().statusCode(201);

        given()
            .contentType(ContentType.JSON)
            .body("{\"productId\": " + expensiveId + ", \"rawMaterialId\": " + nailId + ", \"quantity\": 20.00}")
            .when().post("/api/product-raw-materials")
            .then().statusCode(201);

        // Associate: Cheap Stool needs 5 wood, 10 nails
        given()
            .contentType(ContentType.JSON)
            .body("{\"productId\": " + cheapId + ", \"rawMaterialId\": " + woodId + ", \"quantity\": 5.00}")
            .when().post("/api/product-raw-materials")
            .then().statusCode(201);

        given()
            .contentType(ContentType.JSON)
            .body("{\"productId\": " + cheapId + ", \"rawMaterialId\": " + nailId + ", \"quantity\": 10.00}")
            .when().post("/api/product-raw-materials")
            .then().statusCode(201);

        // Get suggestions - greedy algorithm should prioritize Expensive Chair
        given()
            .when().get("/api/production-suggestions")
            .then()
            .statusCode(200)
            .body("suggestedProducts.size()", greaterThanOrEqualTo(1))
            .body("totalValue", greaterThan(0f));
    }
}
