package com.ubb59.proiectcolectiv.Licitatie.Server.Licitatie.controller;

import org.apache.logging.log4j.message.Message;
import org.apache.logging.log4j.message.SimpleMessageFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
public class WebSocketController {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @SendTo("/topic/auction/bid-changed/{auctionId}")
    public Double sendPriceChangeMessage(Integer auctionId, Double newBidOffer){
        this.simpMessagingTemplate.convertAndSend("topic/auction/bid-changed/"+auctionId, newBidOffer);
        return newBidOffer;
    }
}
