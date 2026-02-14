package com.autoflex.stockcontrol.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class ProductResourceTest {

    @Test
    void testListAllProducts() {
        given()
            .when().get("/api/products")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    void testCreateAndGetProduct() {
        // Create
        Long id = given()
            .contentType(ContentType.JSON)
            .body("{\"name\": \"Chair\", \"value\": 150.00}")
            .when().post("/api/products")
            .then()
            .statusCode(201)
            .body("name", equalTo("Chair"))
            .body("value", equalTo(150.00f))
            .extract().jsonPath().getLong("id");

        // Get by ID
        given()
            .when().get("/api/products/{id}", id)
            .then()
            .statusCode(200)
            .body("name", equalTo("Chair"))
            .body("id", equalTo(id.intValue()));
    }

    @Test
    void testUpdateProduct() {
        Long id = given()
            .contentType(ContentType.JSON)
            .body("{\"name\": \"Table\", \"value\": 300.00}")
            .when().post("/api/products")
            .then()
            .statusCode(201)
            .extract().jsonPath().getLong("id");

        given()
            .contentType(ContentType.JSON)
            .body("{\"name\": \"Large Table\", \"value\": 450.00}")
            .when().put("/api/products/{id}", id)
            .then()
            .statusCode(200)
            .body("name", equalTo("Large Table"))
            .body("value", equalTo(450.00f));
    }

    @Test
    void testDeleteProduct() {
        Long id = given()
            .contentType(ContentType.JSON)
            .body("{\"name\": \"Shelf\", \"value\": 100.00}")
            .when().post("/api/products")
            .then()
            .statusCode(201)
            .extract().jsonPath().getLong("id");

        given()
            .when().delete("/api/products/{id}", id)
            .then()
            .statusCode(204);

        given()
            .when().get("/api/products/{id}", id)
            .then()
            .statusCode(404);
    }

    @Test
    void testGetNonExistentProduct() {
        given()
            .when().get("/api/products/{id}", 99999)
            .then()
            .statusCode(404);
    }

    @Test
    void testCreateProductValidation() {
        given()
            .contentType(ContentType.JSON)
            .body("{\"name\": \"\", \"value\": -1}")
            .when().post("/api/products")
            .then()
            .statusCode(400);
    }
}
