package javelin.service;

import javelin.bot.msg.template.MessageTemplateContext;
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
            Дякую за замовлення 1.
            Очікуйте на підтвердження""";

        var acceptedMsg = """
            Вже готую твоє замовлення 1.
            Зазвичай це займає 10-15хв.""";

        var cookingMsg = """
            Вже готую твоє замовлення 1.
            Зазвичай це займає 10-15хв.""";

        var cookedMsg = """
            Твоє замовлення готово.""";

        var deliveringMsg = """
            Замовлення 1 передали курʼєру. Очікуйте на доставочку за адресою -.""";
        return Stream.of(
            Arguments.of(Order.Status.NEW, newMsg),
            Arguments.of(Order.Status.ACCEPTED, acceptedMsg),
            Arguments.of(Order.Status.COOKING, cookingMsg),
            Arguments.of(Order.Status.COOKED, cookedMsg),
            Arguments.of(Order.Status.DELIVERING, deliveringMsg),
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