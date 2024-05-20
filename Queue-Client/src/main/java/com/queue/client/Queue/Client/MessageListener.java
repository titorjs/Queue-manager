package com.queue.client.Queue.Client;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MessageListener {
    Logger log = LoggerFactory.getLogger(getClass());

    @PostMapping("/message")
    public String mensajeRecibido(@RequestBody String message){
        try {
            log.info("Message received: " + message);
        } catch (Exception e){
            log.error(String.valueOf(e));
        }
        return "Mensaje recibido";
    }
}
