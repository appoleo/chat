package com.ami.controller;

import com.ami.websocket.SocketService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;

/**
 * @author wangchendong@innjoy.me
 */
@RestController
@RequestMapping("/send")
public class WebsocketController {

    @Resource
    private SocketService socketService;

    @GetMapping("/message/{message}")
    public void send(@PathVariable("message") String message) {
        try {
            socketService.send(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
