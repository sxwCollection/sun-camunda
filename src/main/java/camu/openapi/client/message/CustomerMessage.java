package camu.openapi.client.message;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonAutoDetect(fieldVisibility = JsonAutoDetect.Visibility.ANY)
@JsonSerialize
public record CustomerMessage(MessageHead head, String customerName) implements LottoMessage {
    @Override
    public MessageHead getHead() {
        return head;
    }
}
