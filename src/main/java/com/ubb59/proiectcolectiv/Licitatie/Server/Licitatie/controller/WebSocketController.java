package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.controller;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.SimpleMessageFactory;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    private final SimpleMessageFactory simpleMessageFactory = new SimpleMessageFactory();

    @MessageMapping("/new_message")
    @SendTo("/topic/test_example")
    public Message sendMessage() throws Exception{
        return this.simpleMessageFactory.newMessage("Hello world!");
    }
}
