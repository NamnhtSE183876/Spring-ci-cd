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
    }

    @GetMapping("/health/socket")
    public String checkSocketIOHealth() {
        int connectedClients = socketIOServer.getAllClients().size(); // Get all connected clients
        if (connectedClients > 0) {
            return "Socket.IO server is running with " + connectedClients + " active connections.";
        } else {
            return "Socket.IO server is running but no active connections.";
        }
    }
}
