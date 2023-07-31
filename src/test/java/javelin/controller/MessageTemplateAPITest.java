package javelin.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import javelin.AbstractApiTest;
import javelin.bot.boss.msg.BossMsgName;
import javelin.bot.client.msg.ClientMsgName;
import javelin.dto.BossMessageTemplateRequest;
import javelin.dto.ClientMessageTemplateRequest;
import org.apache.http.HttpStatus;
import org.hamcrest.Matchers;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

class MessageTemplateAPITest extends AbstractApiTest {

    @Autowired
    private ObjectMapper om;

    public static Stream<BossMessageTemplateRequest> provideBossMsgBodies() {
        return Arrays.stream(BossMsgName.values())
            .map(name -> new BossMessageTemplateRequest(name, "value"));
    }

    @ParameterizedTest
    @MethodSource("provideBossMsgBodies")
    void When_CreateMsgForBossWithCorrectBody_Then_Success(BossMessageTemplateRequest r)
        throws JsonProcessingException {

        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(om.writeValueAsString(r))
            .post("/msgs/boss")
            .then()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .statusCode(HttpStatus.SC_OK)
            .body("id", Matchers.is(r.name().toString()))
            .body("txt", Matchers.is(r.txt()))
            .body("created", notNullValue())
            .body("updated", notNullValue());
    }

    public static Stream<ClientMessageTemplateRequest> provideClientMsgBodies() {
        return Arrays.stream(ClientMsgName.values())
            .map(name -> new ClientMessageTemplateRequest(name, "value"));
    }

    @ParameterizedTest
    @MethodSource("provideClientMsgBodies")
    void When_CreateMsgForClientWithCorrectBody_Then_Success(ClientMessageTemplateRequest r)
        throws JsonProcessingException {

        given()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(om.writeValueAsString(r))
            .post("/msgs/client")
            .then()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .statusCode(HttpStatus.SC_OK)
            .body("id", Matchers.is(r.name().toString()))
            .body("txt", Matchers.is(r.txt()))
            .body("created", notNullValue())
            .body("updated", notNullValue());
    }
}