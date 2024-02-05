package camu.openapi.client.config;

import camu.openapi.client.camu.worker.CheckWinning;
import camu.openapi.client.camu.worker.LottoNumberGenerator;
import camu.openapi.client.camu.worker.WinningNumberGenerator;
import camu.openapi.client.service.CamundaClientService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.camunda.bpm.client.spring.annotation.ExternalTaskSubscription;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class CamuTaskHandlerConfig {
    private @NonNull CamundaClientService camundaClientService;

    @Value("${app.camunda.var-key.winning-number}")
    private String winningVarKey;

    @Value("${app.camunda.var-key.lotto-number}")
    private String lottoVarKey;

    @Value("${app.camunda.var-key.customer")
    private final String customerVarKey;

    @Bean
    @ExternalTaskSubscription(processDefinitionKey = "lottery.process", topicName = "generateLottoNumber", lockDuration = 3000)
    public ExternalTaskHandler sayHelloTaskHandler() {
        return new LottoNumberGenerator();
    }

    @Bean
    @ExternalTaskSubscription(processDefinitionKey = "lottery.process", topicName = "generateWinningNumber", lockDuration = 3000)
    public ExternalTaskHandler winningNumberHandler() {
        return new WinningNumberGenerator();
    }

    @Bean
    @ExternalTaskSubscription(processDefinitionKey = "lottery.process", topicName = "checkWinning", lockDuration = 3000)
    public ExternalTaskHandler checkWinningHandler() {
        return new CheckWinning(camundaClientService, winningVarKey, lottoVarKey, customerVarKey);
    }
}
