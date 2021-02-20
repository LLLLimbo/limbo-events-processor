package cn.limbo.event.handler;

import cn.limbo.App;
import java.net.URI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;

@SpringBootTest(classes = App.class,
    webEnvironment = WebEnvironment.RANDOM_PORT)
public class TestWebEventHandler {

  @Autowired
  private TestRestTemplate rest;

  @Test
  public void testTriggerEventUsingREST() throws Exception {
    ResponseEntity<String> result = this.rest.exchange(
        RequestEntity.post(new URI("/triggerEvent"))
            .header("Content-Type","application/cloudevents+json")
            .header("tenant","1azipgi")
            .body("{\n"
                + "  \"specversion\" : \"1.0\",\n"
                + "  \"type\" : \"org.springframework\",\n"
                + "  \"source\" : \"https://spring.io/\",\n"
                + "  \"id\" : \"A234-1234-1234\",\n"
                + "  \"datacontenttype\" : \"application/json\",\n"
                + "  \"data\" : {\n"
                + "    \"phone\": \"17682453276\"\n"
                + "  }\n"
                + "}"), String.class);
    System.out.println(result.getBody());
  }
}
