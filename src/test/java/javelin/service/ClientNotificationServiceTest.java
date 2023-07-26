package javelin.service;

import javelin.bot.client.msg.template.MessageTemplateContext;
import javelin.config.TemplateConfig;
import javelin.entity.Client;
import javelin.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
class ClientNotificationServiceTest {

    private ClientNotificationService target;

    @BeforeEach
    public void initTargetAndMocks() {
        var templateContext = new MessageTemplateContext(new TemplateConfig().templateConfiguration());
        var employeeNotificationService = Mockito.mock(EmployeeNotificationService.class);

        this.target = new ClientNotificationService(
            new MessageQService(),
            templateContext,
            employeeNotificationService
        );
    }

    static Stream<Arguments> clientOrdersStatuses() {
        var newMsg = """
            Супер!
            Замовлення створене, чекаю на підтвердження від адміна!""";

        return Stream.of(
            Arguments.of(Order.Status.NEW, newMsg),
            Arguments.of(Order.Status.ACCEPTED, "Єєс! Твоє замовлення прийнято і вже полетіло готуватись на кухню!"),
            Arguments.of(Order.Status.COOKING, "Зазвичай наша кухня видає замовлення за 15-20 хв, чекаю на твоє, щоб швидше відправити його тобі."),
            Arguments.of(Order.Status.COOKED, "Замовлення готово!"),
            Arguments.of(Order.Status.DELIVERING, "Кур’єр вже везе твоє замовлення! Очікуй доставочку і смачного тобі, друг!"),
            Arguments.of(Order.Status.CANCELLED, null),
            Arguments.of(Order.Status.DONE, null),
            Arguments.of(Order.Status.OUT, null)
        );
    }

    @ParameterizedTest
    @MethodSource("clientOrdersStatuses")
    void When_(Order.Status status, String expected) {
        var c = new Client();
        c.setId(1L);
        var o = new Order();
        o.setId(1L);
        o.setStatus(status);
        o.setAddress("-");

        var msg = target.notify(c, o);
        assertEquals(expected, msg);
    }

}