package camu.openapi.client.message;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;

@JsonSerialize
@JsonIgnoreProperties("messageName")
public record MessageHead(String messageName, String businessKey) {
}
