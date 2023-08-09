package javelin.mgr;

import javelin.bot.boss.BossBot;
import javelin.entity.Employee;
import javelin.service.EmployeeMessageQService;
import javelin.service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmployeeMsgManager {

    private final BossBot bot;
    private final EmployeeMessageQService qService;
    private final EmployeeService employeeService;

    public void sendCommunications() {
        for (var msg = qService.pollCommunication(); msg != null; msg = qService.pollCommunication()) {
            var allAdmins = employeeService.findAllAdmins();
            for (var admin : allAdmins) {
                bot.sendNew(admin.getId(), msg.getTxt());
            }
        }
    }

}
