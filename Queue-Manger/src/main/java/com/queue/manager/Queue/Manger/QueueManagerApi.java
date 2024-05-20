package com.queue.manager.Queue.Manger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;

@RestController
public class QueueManagerApi{
    private List<String> suscribers;
    public Logger log;

    public QueueManagerApi(){
        suscribers = new ArrayList<>();
        log = LoggerFactory.getLogger(getClass());
    }

    @PostMapping("/suscribe")
    public String SuscribeMe(@RequestBody String endpoint) {
        try{
            if(suscribers.contains(endpoint)){
                return "Already suscribed!!!";
            }
            suscribers.add(endpoint);
            log.info("Suscriber added: " + endpoint);
            return "suscribed correctly!!!";
        } catch (Exception e){
            log.error(String.valueOf(e));
        }
        return "Error de suscripción!!!";
    }

    @PostMapping("/unsuscribe")
    public String Unsuscribe(@RequestBody String endpoint){
        try{
            if(suscribers.contains(endpoint)){
                suscribers.remove(endpoint);
                log.info("Suscriber deleted: " + endpoint);
                return "Suscriber deleted!!!";
            }
            return "No such suscriber";
        } catch (Exception e){
            log.error(String.valueOf(e));
        }
        return "Error unsuscribing!!!";
    }

    @PostMapping("/message")
    public void AddMessage(@RequestBody String message) {
        try{
            log.info("Incoming message: " + message);
            URI uri;
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request;
            for (String end: suscribers) {
                HttpResponse response = null;
                try {
                    log.info("Sending message to: " + end);
                    request = HttpRequest.newBuilder()
                            .uri(URI.create(end))
                            .header("Content-Type", "text/plain")
                            .POST(HttpRequest.BodyPublishers.ofString(message))
                            .build();
                    log.info("Getting response...");
                    response = client.send(request, HttpResponse.BodyHandlers.ofString());
                } catch (Exception e) {
                    log.error("Got next error trying to send message to: " + end);
                    log.error("Response: " + response);
                    log.error(String.valueOf(e));
                }
            }
        } catch (Exception e){
            log.error("ERROR ON MESSAGE SENDING!!!");
            log.error(String.valueOf(e));
        }
    }
}
