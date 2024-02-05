package camu.openapi.client.service;

import camu.openapi.client.message.LottoMessage;
import camu.openapi.client.message.MessageHead;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.openapitools.client.api.MessageApi;
import org.openapitools.client.model.CorrelationMessageDto;
import org.openapitools.client.model.VariableValueDto;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CamundaClientService {

    private @NonNull MessageApi messageApi;

    private final ObjectMapper mapper = new ObjectMapper();

    public Optional<String> sendMsg(String msgKey, LottoMessage msg) {

        MessageHead head = msg.getHead();
        try {

            String jsonMsg = mapper.writeValueAsString(msg);
            Map<String, VariableValueDto> var = Collections.singletonMap(msgKey, new VariableValueDto()
                    .type("String").value(jsonMsg));
            CorrelationMessageDto correlationMessageDto = new CorrelationMessageDto().messageName(head.messageName())
                    .businessKey(head.businessKey()).processVariables(var);
            messageApi.deliverMessage(correlationMessageDto);
            log.info("send msg: {}", msg);
        } catch (Exception e) {
            e.printStackTrace();
            return Optional.of(e.getMessage());
        }
        return Optional.empty();
    }
}
