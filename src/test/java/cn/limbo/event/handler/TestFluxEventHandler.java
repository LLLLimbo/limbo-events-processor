package cn.limbo.event.handler;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

import cn.limbo.common.BasicData;
import java.util.function.Function;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.function.cloudevent.CloudEventMessageBuilder;
import org.springframework.cloud.function.context.FunctionCatalog;
import org.springframework.cloud.function.context.test.FunctionalSpringBootTest;
import org.springframework.messaging.Message;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@FunctionalSpringBootTest
public class TestFluxEventHandler {

  @Autowired
  private FunctionCatalog catalog;

  @Test
  public void words() {
    Function<Message<BasicData>, Message<String>> function = catalog.lookup(Function.class,
        "triggerEvent");
    assertThat(function.apply(input()).getPayload()).isEqualTo(out().getPayload());
  }

  private Message<BasicData> input() {
    BasicData basicData = new BasicData();
    basicData.setContractId("defaultId");
    basicData.setPhone("17682453276");
    return CloudEventMessageBuilder
        .withData(basicData)
        .setHeader("tenant","1azipgi")
        .setSpecVersion("1.0")
        .setType("org.springframework")
        .setId("A234-1234-1234")
        .setDataContentType("application/json")
        .setSource("https://spring.io/")
        .build();
  }

  private Message<String> out() {
    return CloudEventMessageBuilder
        .withData("SUCCESS")
        .setSource("/triggerEvent")
        .build();
  }
}
