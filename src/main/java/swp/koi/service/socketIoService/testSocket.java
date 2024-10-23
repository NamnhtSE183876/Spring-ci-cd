package swp.koi.service.socketIoService;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
//@RequiredArgsConstructor
@RequestMapping("/test")
public class testSocket {

//    private final EventListenerFactoryImpl eventListenerFactory;
    private final SocketIOConfig socketIOConfig;

//    @GetMapping("/create")
//    public void createSocketIo(@RequestParam String port) {
//        eventListenerFactory.createDataListener(socketIOServer,port);
//    }

    private boolean isServerRunning = false;

    public testSocket(SocketIOConfig socketIOConfig) {
        this.socketIOConfig = socketIOConfig;
    }

    @GetMapping("/send")
    public String sendMessage() {
        socketIOConfig.sendMessage("Hello from Spring Boot!");
        return "Message sent!";
    }
}
