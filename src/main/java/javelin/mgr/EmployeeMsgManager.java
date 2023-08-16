package javelin.mgr;

import javelin.bot.boss.BossBot;
import javelin.service.EmployeeMessageQService;
import javelin.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EmployeeMsgManager {

    private final BossBot bot;
    private final EmployeeMessageQService qService;
    private final EmployeeService employeeService;

    public void sendCommunications() {
        for (var msg = qService.poll(); msg != null; msg = qService.poll()) {
            bot.sendNew(msg.getKey(), msg.getValue());
        }
        for (var msg = qService.pollCommunication(); msg != null; msg = qService.pollCommunication()) {
            var allAdmins = employeeService.findAllAdmins();
            for (var admin : allAdmins) {
                bot.sendNew(admin.getId(), msg.getTxt());
            }
        }
    }

}
