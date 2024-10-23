package swp.koi.service.socketIoService;

import com.corundumstudio.socketio.SocketIOServer;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/test")
public class testSocket {

//    private final EventListenerFactoryImpl eventListenerFactory;
    private final SocketClient socketClient;

//    @GetMapping("/create")
//    public void createSocketIo(@RequestParam String port) {
//        eventListenerFactory.createDataListener(socketIOServer,port);
//    }

    @PostMapping("/send")
    public String sendMessage(@RequestBody String message) {
        socketClient.getSocket().emit("message", message);
        return "Message sent: " + message;
    }
}
