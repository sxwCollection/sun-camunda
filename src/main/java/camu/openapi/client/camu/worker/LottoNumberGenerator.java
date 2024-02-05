package camu.openapi.client.camu.worker;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.springframework.beans.factory.annotation.Value;

import java.util.Collections;
import java.util.Random;

@Slf4j
@RequiredArgsConstructor
public class LottoNumberGenerator implements ExternalTaskHandler {

    @Value("${app.camunda.var-key.lotto-number}")
    private String lottoVarKey;

    @Override
    public void execute(ExternalTask task, ExternalTaskService service) {
        Random random = new Random();
        int number = random.nextInt(3);
        log.info("lotto number {} is generated",
                number);
        service.complete(task, Collections.singletonMap(lottoVarKey, number));
    }

}
