package javelin.controller;

import javelin.AbstractApiTest;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;

class EmployeeApiTest extends AbstractApiTest {


    @Test
    @Sql(scripts = "/sql/employee/delete_all.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void When_CreateAdminEmployee_Then_ExpectItsCorrectState() {
        var body = """
            {"id":1, "role": "ADMIN"}
            """;

        given()
            .when()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .post("/employee")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("id", Matchers.is(1))
            .body("role", Matchers.is("ADMIN"))
            .body("status", Matchers.is("ENABLED"));
    }

    @Test
    @Sql(scripts = "/sql/employee/999.sql")
    @Sql(scripts = "/sql/employee/delete_all.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void When_UpdateEmployeeStatus_Then_ExpectItsCorrectState() {
        var body = """
            {"id":999,"role":"BOSS","status": "DISABLED"}
            """;

        given()
            .when()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(body)
            .post("/employee")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("id", Matchers.is(999))
            .body("role", Matchers.is("BOSS"))
            .body("status", Matchers.is("DISABLED"));
    }

    @Test
    @Sql(scripts = "/sql/employee/999.sql")
    @Sql(scripts = "/sql/employee/delete_all.sql", executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void When_GetEmployees_Then_SeeAll() {

        given()
            .when()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .get("/employee")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("$", Matchers.hasSize(1))
            .body("[0].id", Matchers.is(999))
            .body("[0].role", Matchers.is("BOSS"))
            .body("[0].status", Matchers.is("ENABLED"));
    }

    @Test
    @Sql(scripts = "/sql/employee/999.sql")
    void When_DeleteById_Then_SeeEmployeeDeleted() {

        given()
            .when()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .delete("/employee/999")
            .then()
            .statusCode(HttpStatus.SC_NO_CONTENT);

        given()
            .when()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .get("/employee")
            .then()
            .statusCode(HttpStatus.SC_OK)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body("$", Matchers.hasSize(0));
    }
}