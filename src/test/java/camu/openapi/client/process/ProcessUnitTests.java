package camu.openapi.client.process;

import camu.openapi.client.message.LottoMessage;
import camu.openapi.client.message.MessageFactory;
import camu.openapi.client.message.MessageType;
import org.camunda.bpm.engine.ProcessEngine;
import org.camunda.bpm.engine.externaltask.ExternalTask;
import org.camunda.bpm.engine.history.HistoricActivityInstance;
import org.camunda.bpm.engine.history.HistoricProcessInstance;
import org.camunda.bpm.engine.runtime.MessageCorrelationResult;
import org.camunda.bpm.engine.runtime.ProcessInstance;
import org.camunda.bpm.engine.test.Deployment;
import org.camunda.bpm.extension.junit5.test.ProcessEngineExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ProcessEngineExtension.class)
public class ProcessUnitTests {
    public ProcessEngine processEngine;

    private static final String LOTTO_PROCESS_BPMN = "lottery.bpmn";
    private static final String WORKER_ID = "StrongWoman";

    private final String customerName = "Pokeman";

    private final String CUSTOMER_JSON = """
            {"head":{"businessKey":"Pokeman-12345"},"customerName":"Pokeman"}""";

    @Test
    public void testShouldWork() {
        assertThat(processEngine).isNotNull();
    }

    @Test
    @Deployment(resources = LOTTO_PROCESS_BPMN)
    public void testLottoProcessWin() throws InterruptedException {

        //starting process with message
        LottoMessage msg = MessageFactory.getMessageInstance(MessageType.NEW_CUSTOMER, customerName);
        ProcessInstance processInstance = processEngine.getRuntimeService().createMessageCorrelation(msg.getHead().messageName())
                .setVariable("customer", CUSTOMER_JSON)
                .processInstanceBusinessKey(msg.getHead().businessKey()).correlateWithResult().getProcessInstance();
        assertThat(processInstance).isNotNull();
        // wait for processing
        Thread.sleep(1000);

        //externalTask for generating lotto number is created.
        ExternalTask externalTaskLottoGen = processEngine.getExternalTaskService().createExternalTaskQuery()
                .processInstanceId(processInstance.getProcessInstanceId()).topicName("generateLottoNumber").singleResult();
        assertThat(externalTaskLottoGen).isNotNull();
        // winning number generation task is created.
        ExternalTask externalTaskWinningGen = processEngine.getExternalTaskService().createExternalTaskQuery()
                .processInstanceId(processInstance.getProcessInstanceId()).topicName("generateWinningNumber").singleResult();
        assertThat(externalTaskWinningGen).isNotNull();
        //complete externalTasks

        int count = processEngine.getExternalTaskService().fetchAndLock(10, WORKER_ID).topic(externalTaskLottoGen.getTopicName(), 100000).execute().size();
        assertThat(count).isEqualTo(1);
        count = processEngine.getExternalTaskService().fetchAndLock(10, WORKER_ID).topic(externalTaskWinningGen.getTopicName(), 100000).execute().size();
        assertThat(count).isEqualTo(1);
        processEngine.getExternalTaskService().complete(externalTaskLottoGen.getId(), WORKER_ID, Collections.singletonMap("lottoNumber", 0));
        processEngine.getExternalTaskService().complete(externalTaskWinningGen.getId(), WORKER_ID, Collections.singletonMap("winningNumber", 2));

        ExternalTask externalTaskCheckWinning = processEngine.getExternalTaskService().createExternalTaskQuery()
                .processInstanceId(processInstance.getProcessInstanceId()).topicName("checkWinning").singleResult();
        assertThat(externalTaskCheckWinning).isNotNull();

        count = processEngine.getExternalTaskService().fetchAndLock(10, WORKER_ID).topic(externalTaskCheckWinning.getTopicName(), 100000).execute().size();
        assertThat(count).isEqualTo(1);
        processEngine.getExternalTaskService().complete(externalTaskCheckWinning.getId(), WORKER_ID);

        Thread.sleep(1000);
        MessageCorrelationResult result = processEngine.getRuntimeService().createMessageCorrelation(MessageType.PUNISH.getValue())
                .setVariable("result", "you will get punished with 10000 Dollars!!")
                .processInstanceBusinessKey(msg.getHead().businessKey()).correlateWithResult();

        HistoricProcessInstance historicProcessInstance = processEngine.getHistoryService()
                .createHistoricProcessInstanceQuery().processInstanceId(processInstance.getProcessInstanceId())
                .singleResult();
        assertThat(historicProcessInstance).isNotNull();
        HistoricActivityInstance historicActivityInstance = processEngine.getHistoryService()
                .createHistoricActivityInstanceQuery().processInstanceId(processInstance.getProcessInstanceId())
                .activityType("noneEndEvent").singleResult();
        assertThat(historicActivityInstance).isNotNull();
        assertThat(historicActivityInstance.getActivityId()).isEqualTo("happyEnd");
    }

}
