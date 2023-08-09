package javelin.scheduler;

import javelin.mgr.ClientMsgManager;
import javelin.mgr.EmployeeMsgManager;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@Component
@RequiredArgsConstructor
public class MessageQScheduler {

    private final TaskScheduler taskScheduler;
    private final EmployeeMsgManager employeeMsgManager;
    private final ClientMsgManager clientMsgManager;

    @Scheduled(fixedRate = 5, timeUnit = TimeUnit.SECONDS)
    public void sendMsgs() {
        taskScheduler.schedule(employeeMsgManager::sendCommunications, new Date(System.currentTimeMillis() + 100));
        clientMsgManager.sendCommunications();
    }

}
