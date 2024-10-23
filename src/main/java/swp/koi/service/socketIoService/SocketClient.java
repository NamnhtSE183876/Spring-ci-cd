package swp.koi.service.socketIoService;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.URISyntaxException;

@Component
public class SocketClient {

    private Socket socket;

    @PostConstruct
    public void init() {
        try {
            socket = IO.socket("https://54.255.138.0:8081");
            socket.on("message", onNewMessage);
            socket.connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
    public Socket getSocket() {
        return socket;
    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            System.out.println("New message: " + args[0]);
        }
    };

    @PreDestroy
    public void cleanup() {
        socket.disconnect();
        socket.off("message", onNewMessage);
    }
}
