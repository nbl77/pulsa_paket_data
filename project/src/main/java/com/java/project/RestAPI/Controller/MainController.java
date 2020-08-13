package com.java.project.RestAPI.Controller;

import com.java.project.RestAPI.Service.MessageService;
import com.java.project.RestAPI.Service.SendService;
import com.java.project.RestAPI.Temp.AuthTemp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

@RestController
public class MainController {
    @Autowired
    AuthTemp authTemp;
    @Autowired
    SendService sendService;


    @PostMapping("/")
    public ResponseEntity<?> SendData(@RequestBody String data) {
        MessageService messageService = new MessageService();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-Type", "application/json");
        String[] result = sendService.sendData(data);
        HttpStatus hts = null;
        if (result[0].equals("200")||result[0].equals("201")) {
            hts = HttpStatus.OK;
        } else {
            hts = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(messageService.getResponse(result) , headers, hts);
    }
}
