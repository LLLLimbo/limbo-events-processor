package cn.limbo.event.handler;

import cn.limbo.common.BasicData;
import io.zeebe.client.ZeebeClient;
import io.zeebe.client.api.response.WorkflowInstanceEvent;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.function.cloudevent.CloudEventMessageBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.messaging.Message;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EventHandler {

  private final ZeebeClient zeebeClientLifecycle;

  public EventHandler(ZeebeClient zeebeClientLifecycle) {
    this.zeebeClientLifecycle = zeebeClientLifecycle;
  }

  @Bean
  public Function<Message<BasicData>, Message<String>> triggerEvent() {
    return message -> {
      log.info("A request has arrived . Request body: {}", message);

      String tenant = message.getHeaders().get("tenant", String.class);
      String eventCode=message.getHeaders().get("event",String.class);
      BasicData basicData = message.getPayload();
//Create instance.
      final WorkflowInstanceEvent workflowInstanceEvent =
          zeebeClientLifecycle.newCreateInstanceCommand()
              .bpmnProcessId("Process_" + eventCode + "_" + tenant)
              .latestVersion()
              .variables(basicData)
              .send()
              .join();

      log.info(
          "Started instance for workflowKey='{}', bpmnProcessId='{}', version='{}' with workflowInstanceKey='{}'",
          workflowInstanceEvent.getWorkflowKey(), workflowInstanceEvent.getBpmnProcessId(),
          workflowInstanceEvent.getVersion(),
          workflowInstanceEvent.getWorkflowInstanceKey()
      );
      return CloudEventMessageBuilder.withData("SUCCESS").setSource("/triggerEvent").build();
    };
  }
}
