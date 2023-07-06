package javelin.controller;

import javelin.AbstractApiTest;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;


import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

class ProductApiTest extends AbstractApiTest {

    @Test
    @Sql({"/sql/product/999.sql", "/sql/category/1000.sql"})
    @Sql(value = {"/sql/product/delete_all.sql", "/sql/category/delete_all.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void When_GetAll_Then_ExpectCorrectEntities() {
        given()
            .when()
            .get("/product")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("[0].id", equalTo(999))
            .body("[0].title", equalTo("999"))
            .body("[0].external_id", equalTo("external_id"));
    }

    @Test
    @Sql({"/sql/product/999.sql", "/sql/category/1000.sql"})
    @Sql(value = {"/sql/product/delete_all.sql", "/sql/category/delete_all.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void When_GetById_Then_ExpectCorrectEntity() {
        given()
            .when()
            .get("/product/999")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("id", equalTo(999))
            .body("title", equalTo("999"))
            .body("external_id", equalTo("external_id"));
    }

    @Test
    void When_GetById_Then_ExpectNotFound() {
        given()
            .when()
            .get("/product/1000")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    @Sql("/sql/category/1000.sql")
    @Sql(value = {"/sql/product/delete_all.sql", "/sql/category/delete_all.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void When_PostValidData_Then_ExpectCreated() {
        var body = """
            {
                "external_id": "id",
                "title": "p1",
                "categoryIds": [1000]
            }
            """;

        given()
            .body(body)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/product")
            .then()
            .assertThat()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .statusCode(HttpStatus.SC_CREATED)
            .body("id", Matchers.notNullValue())
            .body("title", equalTo("p1"))
            .body("external_id", equalTo("id"))
            .body("categories[0].id",  equalTo(1000));
    }

    @Test
    @Sql({"/sql/product/999.sql", "/sql/category/1000.sql"})
    @Sql(value = {"/sql/product/delete_all.sql", "/sql/category/delete_all.sql"},
        executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void When_DeleteById_Then_ExpectNoContent() {
        given()
            .when()
            .delete("/product/999")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    void When_DeleteById_Then_ExpectNotFound() {
        given()
            .when()
            .delete("/product/1000")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_NO_CONTENT);
    }
}
