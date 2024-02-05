package camu.openapi.client.process;

import camu.openapi.client.camu.worker.CheckWinning;
import camu.openapi.client.message.LottoMessage;
import camu.openapi.client.message.MessageFactory;
import camu.openapi.client.message.MessageType;
import camu.openapi.client.service.CamundaClientService;
import org.camunda.bpm.client.task.ExternalTask;
import org.camunda.bpm.client.task.ExternalTaskService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
public class ExternalTaskUnitTests {

    private final String WIN_VAR_KEY = "winningNumber";
    private final String LOTTO_VAR_KEY = "lottoNumber";
    private final String CUSTOMER_VAR_KEY = "customer";
    private final String CUSTOMER_JSON = """
            {"head":{"businessKey":"Pokeman-12345"},"customerName":"Pokeman"}""";

    @Test
    @DisplayName("test the external task checkWinning.")
    public void testCheckWinning() {
        //given conditions
        CamundaClientService clientService = mock(CamundaClientService.class);
        ExternalTask externalTask = mock(ExternalTask.class);
        ExternalTaskService externalTaskService = mock(ExternalTaskService.class);
        given(externalTask.getVariable(CUSTOMER_VAR_KEY)).willReturn(CUSTOMER_JSON);
        given(externalTask.getVariable(WIN_VAR_KEY)).willReturn(Integer.valueOf(1));
        given(externalTask.getVariable(LOTTO_VAR_KEY)).willReturn(Integer.valueOf(1));
        CheckWinning checkWinning = new CheckWinning(clientService, WIN_VAR_KEY, LOTTO_VAR_KEY, CUSTOMER_VAR_KEY);
        LottoMessage resultMsg = MessageFactory.getMessageInstance(MessageType.WIN, "Pokeman", "you win 2 Dollars!!");
        // when
        checkWinning.execute(externalTask, externalTaskService);

        // then expectations
        then(externalTask).should().getVariable(CUSTOMER_VAR_KEY);
        then(externalTask).should().getVariable(WIN_VAR_KEY);
        then(externalTask).should().getVariable(LOTTO_VAR_KEY);
        then(externalTask).shouldHaveNoMoreInteractions();
        then(externalTaskService).should().complete(externalTask);
        then(clientService).should().sendMsg("winning", resultMsg);
        then(clientService).shouldHaveNoMoreInteractions();
    }
}
