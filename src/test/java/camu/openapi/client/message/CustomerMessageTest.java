package camu.openapi.client.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class CustomerMessageTest {

    private final String CUSTOMER_JSON = """
            {"head":{"businessKey":"Pokeman-12345"},"customerName":"Pokeman"}""";

    @Test
    public void testMessageSerialization() throws JsonProcessingException {

        MessageHead head = new MessageHead("customerBuyLotto", "Pokeman-12345");
        CustomerMessage msg = new CustomerMessage(head, "Pokeman");
        ObjectMapper mapper = new ObjectMapper();
        //when
        String jsonStr = mapper.writeValueAsString(msg);

        assertThat(jsonStr).isEqualTo(CUSTOMER_JSON);
    }

}