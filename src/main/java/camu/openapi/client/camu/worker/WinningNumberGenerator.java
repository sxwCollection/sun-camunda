package camu.openapi.client.camu.worker;

import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;
import java.util.Map;
import java.util.Random;

@Slf4j
public class WinningNumberGenerator implements ExternalTaskHandler {

    @Value("${app.camunda.var-key.winning-number}")
    private String winningVarKey;

    @Override
    public void execute(ExternalTask task, ExternalTaskService service) {

        System.out.println("winning number generating..");
        Random random = new Random();
        int number = random.nextInt(3);
        Map<String, Object> var = Collections.singletonMap(winningVarKey, number);
        log.info("winning number is {} generated.",
                number);
        service.complete(task, var);
    }

}
