package camu.openapi.client.camu.worker;

import camu.openapi.client.message.CustomerMessage;
import camu.openapi.client.message.LottoMessage;
import camu.openapi.client.message.MessageFactory;
import camu.openapi.client.message.MessageType;
import camu.openapi.client.service.CamundaClientService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.task.ExternalTaskService;

@Slf4j
@RequiredArgsConstructor
public class CheckWinning implements ExternalTaskHandler {

    private @NonNull CamundaClientService camundaClientService;

    private @NonNull String winningVarKey;

    private @NonNull String lottoVarKey;

    private @NonNull String customerVarKey;

    @Override
    public void execute(ExternalTask task, ExternalTaskService service) {

        String customer = task.getVariable(customerVarKey);
        log.info("checking winning for customer {}", customer);
        int winningNumber = task.getVariable(winningVarKey);
        int lottoNumber = task.getVariable(lottoVarKey);
        service.complete(task);

        try {
            ObjectMapper mapper = new ObjectMapper();
            CustomerMessage customerMessage = mapper
                    .readerFor(CustomerMessage.class)
                    .readValue(customer);

            int check = Math.abs(winningNumber - lottoNumber);
            Thread.sleep(2000);
            LottoMessage resultMsg = null;
            switch (check) {
                case 0:
                    resultMsg = MessageFactory.getMessageInstance(MessageType.WIN, customerMessage.customerName(), "you win 2 Dollars!!");
                    break;
                case 1:
                    resultMsg = MessageFactory.getMessageInstance(MessageType.LOSE, "you didn't win, try next, be lucky!");
                    break;
                case 2:
                    resultMsg = MessageFactory.getMessageInstance(MessageType.PUNISH, "you must be pushed with 10000 Dollars!!!");
                    break;
            }
            camundaClientService.sendMsg("winning", resultMsg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
