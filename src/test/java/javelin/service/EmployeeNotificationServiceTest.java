package javelin.service;

import javelin.bot.msg.template.MessageTemplateContext;
import javelin.config.TemplateConfig;
import javelin.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EmployeeNotificationServiceTest {

    private EmployeeService employeeService;
    private EmployeeNotificationService target;

    @BeforeEach
    public void initTargetAndMocks() {
        this.employeeService = Mockito.mock(EmployeeService.class);
        var templateContext = new MessageTemplateContext(new TemplateConfig().templateConfiguration());

        this.target = new EmployeeNotificationService(
            employeeService,
            new MessageQService(),
            templateContext
        );
    }

    static Stream<Arguments> serviceOrdersStatuses() {
        var acceptedMsg = """
            Замовлення 1 передано на кухню.
                                                      
            Коли замовлення винесуть нажміть: /ready_1""";

        return Stream.of(
            Arguments.of(Order.Service.DINEIN, Order.Status.ACCEPTED, acceptedMsg),
            Arguments.of(Order.Service.DELIVERY, Order.Status.ACCEPTED, null),
            Arguments.of(Order.Service.DINEIN, Order.Status.COOKING, null),
            Arguments.of(Order.Service.DINEIN, Order.Status.COOKED, "Замовлення 1 приготовлене. Чекаємо на клієнта."),
            Arguments.of(Order.Service.DINEIN, Order.Status.DELIVERING, null),
            Arguments.of(Order.Service.DINEIN, Order.Status.CANCELLED, null),
            Arguments.of(Order.Service.DINEIN, Order.Status.DONE, null),
            Arguments.of(Order.Service.DINEIN, Order.Status.OUT, null)
        );
    }

    @ParameterizedTest
    @MethodSource("serviceOrdersStatuses")
    void When_(Order.Service service, Order.Status status, String expected) {
        var o = new Order();
        o.setId(1L);
        o.setStatus(status);
        o.setService(service);

        var msg = target.notify(o);

        assertEquals(expected, msg);
    }

}