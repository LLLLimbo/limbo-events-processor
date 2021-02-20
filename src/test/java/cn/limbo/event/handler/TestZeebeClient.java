package cn.limbo.event.handler;

import cn.limbo.App;
import cn.limbo.common.BasicData;
import cn.limbo.config.ZeebeConfiguration;
import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.response.WorkflowInstanceEvent;
import io.zeebe.test.ZeebeTestRule;
import org.junit.Rule;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;

@SpringBootTest(classes = {App.class, ZeebeConfiguration.class},
    webEnvironment = WebEnvironment.RANDOM_PORT)
public class TestZeebeClient {

  @Rule
  public final ZeebeTestRule testRule = new ZeebeTestRule();

  @Autowired
  private ZeebeClient zeebeClientLifecycle;

  @Test
  public void test() {
    BasicData basicData = new BasicData();
    basicData.setContractId("defaultId");
    basicData.setPhone("17682453276");

    zeebeClientLifecycle
        .newDeployCommand()
        .addResourceFromClasspath("ContractSignEvent.bpmn")
        .send()
        .join();

    final WorkflowInstanceEvent workflowInstance =
        zeebeClientLifecycle
            .newCreateInstanceCommand()
            .bpmnProcessId("Process_100_1azipgi")
            .latestVersion()
            .variables(basicData)
            .send()
            .join();

    ZeebeTestRule.assertThat(workflowInstance)
        .isEnded();
  }

}
