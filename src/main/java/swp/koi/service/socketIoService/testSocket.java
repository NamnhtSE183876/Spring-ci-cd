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
    private final SocketIOServer socketIOServer;

//    @GetMapping("/create")
//    public void createSocketIo(@RequestParam String port) {
//        eventListenerFactory.createDataListener(socketIOServer,port);
//    }

    private boolean isServerRunning = false;

    public testSocket(SocketIOServer socketIOServer) {
        this.socketIOServer = socketIOServer;
        if (socketIOServer != null) {
            isServerRunning = true; // Assuming server starts successfully
        }
    }

    @GetMapping("/health/socket")
    public String checkSocketIOHealth() {
        if (isServerRunning) {
            return "Socket.IO server is running";
        } else {
            return "Socket.IO server is not running";
        }
    }
}
