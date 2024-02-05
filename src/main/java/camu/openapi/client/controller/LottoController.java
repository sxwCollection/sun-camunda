package camu.openapi.client.controller;

import camu.openapi.client.message.LottoMessage;
import camu.openapi.client.message.MessageFactory;
import camu.openapi.client.message.MessageType;
import camu.openapi.client.service.CamundaClientService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
public class LottoController {
    private @NonNull CamundaClientService camundaClientService;
    @Value("${app.camunda.var-key.customer")
    private final String customerVarKey;

    @GetMapping("/lotto/{customer}/buy")
    public String startLottery(@PathVariable("customer") String customerName) {

        LottoMessage msg = MessageFactory.getMessageInstance(MessageType.NEW_CUSTOMER, customerName);
        camundaClientService.sendMsg(customerVarKey, msg);
        return msg.toString();
    }

}
