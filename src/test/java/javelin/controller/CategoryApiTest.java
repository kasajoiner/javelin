package javelin.controller;

import javelin.AbstractApiTest;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

@Disabled
class CategoryApiTest extends AbstractApiTest {

    @Test
    @Sql("/sql/category/999.sql")
    @Sql(value = "/sql/category/delete_all.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void When_GetAll_Then_ExpectCorrectEntities() {
        given()
            .when()
            .get("/category")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_OK)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("[0].id", equalTo(999))
            .body("[0].title", equalTo("999"))
            .body("[0].external_id", equalTo("external_id"));
    }

    @Test
    @Sql("/sql/category/999.sql")
    @Sql(value = "/sql/category/delete_all.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void When_GetById_Then_ExpectCorrectEntity() {
        given()
            .when()
            .get("/category/999")
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
            .get("/category/1000")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_NOT_FOUND);
    }

    @Test
    void When_PostValidData_Then_ExpectBadRequest() {
        var body = """
            {
                "external_id": "id",
                "title": "c1"
            }
            """;

        given()
            .body(body)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/category")
            .then()
            .assertThat()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .statusCode(HttpStatus.SC_CREATED)
            .body("id", Matchers.notNullValue())
            .body("title", equalTo("c1"))
            .body("external_id", equalTo("id"));
    }

    @Test
    @Sql("/sql/category/999.sql")
    void When_DeleteById_Then_ExpectNoContent() {
        given()
            .when()
            .delete("/category/999")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_NO_CONTENT);
    }

    @Test
    void When_DeleteById_Then_ExpectNotFound() {
        given()
            .when()
            .delete("/category/1000")
            .then()
            .assertThat()
            .statusCode(HttpStatus.SC_NO_CONTENT);
    }
}
