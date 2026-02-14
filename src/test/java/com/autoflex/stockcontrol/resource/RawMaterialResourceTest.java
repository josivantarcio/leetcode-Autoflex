package com.autoflex.stockcontrol.resource;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

@QuarkusTest
class RawMaterialResourceTest {

    @Test
    void testListAllRawMaterials() {
        given()
            .when().get("/api/raw-materials")
            .then()
            .statusCode(200)
            .contentType(ContentType.JSON);
    }

    @Test
    void testCreateAndGetRawMaterial() {
        Long id = given()
            .contentType(ContentType.JSON)
            .body("{\"name\": \"Wood\", \"stockQuantity\": 500.00}")
            .when().post("/api/raw-materials")
            .then()
            .statusCode(201)
            .body("name", equalTo("Wood"))
            .body("stockQuantity", equalTo(500.00f))
            .extract().jsonPath().getLong("id");

        given()
            .when().get("/api/raw-materials/{id}", id)
            .then()
            .statusCode(200)
            .body("name", equalTo("Wood"))
            .body("id", equalTo(id.intValue()));
    }

    @Test
    void testUpdateRawMaterial() {
        Long id = given()
            .contentType(ContentType.JSON)
            .body("{\"name\": \"Steel\", \"stockQuantity\": 200.00}")
            .when().post("/api/raw-materials")
            .then()
            .statusCode(201)
            .extract().jsonPath().getLong("id");

        given()
            .contentType(ContentType.JSON)
            .body("{\"name\": \"Steel\", \"stockQuantity\": 350.00}")
            .when().put("/api/raw-materials/{id}", id)
            .then()
            .statusCode(200)
            .body("stockQuantity", equalTo(350.00f));
    }

    @Test
    void testDeleteRawMaterial() {
        Long id = given()
            .contentType(ContentType.JSON)
            .body("{\"name\": \"Plastic\", \"stockQuantity\": 100.00}")
            .when().post("/api/raw-materials")
            .then()
            .statusCode(201)
            .extract().jsonPath().getLong("id");

        given()
            .when().delete("/api/raw-materials/{id}", id)
            .then()
            .statusCode(204);

        given()
            .when().get("/api/raw-materials/{id}", id)
            .then()
            .statusCode(404);
    }
}
