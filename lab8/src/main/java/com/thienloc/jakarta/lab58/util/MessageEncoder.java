package com.thienloc.jakarta.lab58.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.thienloc.jakarta.lab58.entity.Message;
import jakarta.websocket.EncodeException;
import jakarta.websocket.Encoder;
import jakarta.websocket.EndpointConfig;

public class MessageEncoder implements Encoder.Text<Message> {
    private ObjectMapper mapper = new ObjectMapper();
    @Override
    public void destroy() {}
    @Override
    public void init(EndpointConfig config) {}
    @Override
    public String encode(Message message) throws EncodeException {
        try {
            return mapper.writeValueAsString(message);
        } catch (JsonProcessingException e) {
            throw new EncodeException(message, "Unable to encode");
        }
    }
}