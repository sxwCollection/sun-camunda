package camu.openapi.client.config;

import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.ApiClient;
import org.openapitools.client.api.MessageApi;
import org.openapitools.client.api.ProcessDefinitionApi;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class CamundaOpenApiConfig {

    @Value("${camunda.bpm.client.base-url}")
    private String camundaBaseUri;

    @Bean("apiClient")
    public ApiClient apiClient() {
        return new ApiClient().setBasePath(camundaBaseUri);
    }

    @Bean
    public ProcessDefinitionApi processDefinitionApi(@Qualifier("apiClient") ApiClient apiClient) {
        return new ProcessDefinitionApi(apiClient);
    }


    @Bean
    public MessageApi messageApi(@Qualifier("apiClient") ApiClient apiClient) {
        log.info("camunda base url: {}", camundaBaseUri);
        return new MessageApi(apiClient);

    }
}
